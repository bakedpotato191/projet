import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { UserService } from 'src/app/services/user.service';

import { TokenStorageService } from 'src/app/services/token-storage.service';
import { MatTabChangeEvent } from '@angular/material/tabs';
import { SharedService } from 'src/app/services/shared.service';
import { UploadComponent } from '../upload/upload.component';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { LoginComponent } from '../login/login.component';
import { FormGroup } from '@angular/forms';
import { lastValueFrom, Subscription } from 'rxjs';
import { UserpostsComponent } from '../userposts/userposts.component';
import { User } from 'src/app/interfaces/user';
import { Publication} from 'src/app/interfaces/publication';
import { AvatarComponent } from '../shared/dialogs/avatar/avatar.component';
import { FollowingComponent } from '../shared/dialogs/following/following.component';
import { FollowerComponent } from '../shared/dialogs/follower/follower.component';

@Component({
  selector: 'app-userpage',
  templateUrl: './userpage.component.html',
  styleUrls: ['./userpage.component.css'],
})
export class UserPageComponent implements OnInit {
  @ViewChild('input') input!: ElementRef;
  @ViewChild(UserpostsComponent) userposts: any;

  isMyPage!: boolean;
  username!: string;
  isContent: boolean = false;
  user!: User;
  posts!: Publication[];

  uploadRef!: MatDialogRef<UploadComponent>;
  loginRef!: MatDialogRef<LoginComponent>;
  avatarRef!: MatDialogRef<AvatarComponent>;

  avatarForm!: FormGroup;
  fileData!: Blob;
  imgFile!: string;

  subscription!: Subscription;

  isOverlayed: boolean =  false;
  isBtnOverlayed: boolean = false;

  selectedIndex!: number;

   async ngOnInit() {
    this.activatedRoute.paramMap.subscribe(async (param) => {
      this.username = param.get('username')!;
      this.sharedService.setTitle('@' + this.username);
      await this.get_user_data(this.username);
      this.isMyPage = this.username == this.tokenService.getUser().username;
    });
    this.activatedRoute.firstChild?.paramMap.subscribe((params) => {
      const tab = params.get('tabname');
      this.selectedIndex = tab == 'favorites' ? 1 : 0;
    });
  }

  constructor(
    private userService: UserService,
    private readonly activatedRoute: ActivatedRoute,
    private readonly router: Router,
    private readonly tokenService: TokenStorageService,
    private readonly sharedService: SharedService,
    public dialog: MatDialog
  ) {}

  async get_user_data(username: string) {
    lastValueFrom(await this.userService.getUser(username)).then(
      (response) => {
        this.user = response;
      },
      (error) => {
        console.log(error);
      }
    ).finally(
      () => {
        this.isContent = true;
      }
    );   
  }

  open_upload_dialog(): void {
    this.uploadRef = this.dialog.open(UploadComponent, {
      data: this.userposts,
      width: '500px',
      autoFocus: false,
    });

    this.uploadRef.afterClosed().subscribe((result) => {
      if (result) {
        this.userposts.get_latest_post();
      }
    });
  }

  async follow() {
    if (this.tokenService.getToken() == null) {
      this.dialog.open(LoginComponent);
      return;
    }
    this.isBtnOverlayed = true;
      lastValueFrom(await this.userService.follow(this.user.username))
      .then(
        (_data) => {
          this.user.followed = true;
        },
        (error) => {
          console.log(error);
        }
      ).finally(() => {
          this.isBtnOverlayed = false;
        }
      );
  }

  async unfollow() {
    if (this.tokenService.getToken() == null) {
      this.dialog.open(LoginComponent);
      return;
    }
    this.isBtnOverlayed = true;
      lastValueFrom(await this.userService.unfollow(this.user.username))
      .then(
        (_response) => {
          this.user.followed = false;
        },
        (error) => {
          console.log(error);
        }
      ).finally(() => {
          this.isBtnOverlayed = false;
        }
      );
  }

  on_tab_change(event: MatTabChangeEvent): void {
    switch (event.index) {
      case 0:
        this.router.navigate(['profile/' + this.username]);
        break;
      case 1:
        this.router.navigate(['favorites'], {
          relativeTo: this.activatedRoute,
        });
        break;
      default:
        break;
    }
  }

  on_file_change(e: any) {
    if (e.target.files && e.target.files.length) {
      this.fileData = e.target.files[0];
    }

    if (this.avatarRef !== undefined) {
      this.avatarRef.close();
    }
    this.submit_avatar();
  }

  async submit_avatar() {
    var formData: any = new FormData();
    formData.append('avatar', this.fileData);
    this.isOverlayed = true;
      lastValueFrom(await this.userService
        .setProfilePicture(formData))
        .then(
          (response: any) => {
            this.user.avatar = response.avatar;
            this.user.has_avatar = response.has_avatar;
          },
          (error) => {
            this.sharedService.showSnackbar(
              'La demande a échoué avec le statut http ' + error.status,
              'Dismiss',
              7000
            );
          }
        )
        .finally(() => {
          this.isOverlayed = false;
        });
  }

  open_subscriptions_dialog(): void {
    this.dialog.open(FollowingComponent, {
      height: '450px',
      width: '500px',
      data: this.username,
      panelClass: ['dialog-window-style'],
      autoFocus: false
    });
  }

  open_subscribers_dialog():void {
    this.dialog.open(FollowerComponent, {
      height: '450px',
      width: '500px',
      data: this.username,
      panelClass: ['dialog-window-style'],
      autoFocus: false
    });
  }

  async on_avatar_click(): Promise<void> {
    if (!this.isMyPage) {
      return;
    }

    if (this.user.has_avatar) {
      this.avatarRef = this.open_avatar_component();
      this.avatarRef.afterClosed().subscribe(async (result) => {
        if (result === 'delete') {
          this.delete_profile_picture();
        }
      });
    } else {
      this.input.nativeElement.click();
    }
  }

  async delete_profile_picture(): Promise<void>{
    this.isOverlayed = true;
    lastValueFrom(await this.userService
      .deleteProfilePicture())
      .then(
        (response) => {
          this.user.has_avatar = response.has_avatar;
          this.user.avatar = response.avatar;
        },
        (error) => {
          this.sharedService.showSnackbar(
            'La requete DELETE a échoué avec le code statut' +
              error.status,
            'Dismiss',
            7000
          );
        }
      )
      .finally(() => {
        this.isOverlayed = false;
      });
  }

  open_avatar_component() {
    return this.dialog.open(AvatarComponent, {
      width: '400px',
      panelClass: ['dialog-window-style'],
      data: this.input,
      autoFocus: false,
    })
  }

  scroll_till_publications(elRef: ElementRef) {
    elRef.nativeElement.scrollIntoView({behavior: 'smooth'});
  }
}
