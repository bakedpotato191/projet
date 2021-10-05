import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { UserService } from 'src/app/services/user.service';

import { TokenStorageService } from 'src/app/services/token-storage.service';
import { MatTabChangeEvent } from '@angular/material/tabs';
import { SnackBarService } from 'src/app/services/snackbar.service';
import { UploadComponent } from './dialogs/upload/upload.component';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { LoginComponent } from '../login/login.component';
import { FormGroup } from '@angular/forms';
import { lastValueFrom, Subscription } from 'rxjs';
import { User } from 'src/app/interfaces/user';
import { Publication} from 'src/app/interfaces/publication';
import { AvatarComponent } from './dialogs/avatar/avatar.component';
import { FollowingComponent } from './dialogs/followings/following.component';
import { FollowerComponent } from './dialogs/followers/follower.component';
import { Title } from '@angular/platform-browser';

export class Notifier {
  valueChanged: (data: string) => void = (d: string) => { };
}

@Component({
  selector: 'app-userpage',
  templateUrl: './userpage.component.html',
  styleUrls: ['./userpage.component.css'],
})
export class UserpageComponent implements OnInit {
  @ViewChild('publications') publications!: ElementRef;
  @ViewChild('input') input!: ElementRef;

  notifyObj = new Notifier();
  tellChild(value: string) {
      this.notifyObj.valueChanged(value);
  }

  isMyPage!: boolean;
  isContent: boolean = false;
  isFavorites: boolean = false;
  isPublications: boolean = false;
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

  constructor(
    private userService: UserService,
    private readonly activatedRoute: ActivatedRoute,
    private readonly router: Router,
    private readonly tokenService: TokenStorageService,
    private readonly sbService: SnackBarService,
    private readonly titleService: Title,
    public dialog: MatDialog
  ) { }

   ngOnInit() {
    this.activatedRoute.paramMap.subscribe((param) => {
      let username = param.get('username')!;
      this.titleService.setTitle('@' + username);
      this.get_user_data(username);
      this.isMyPage = username == this.tokenService.getUser().username;
    });

    if (this.router.url.endsWith('favorites')) {
        this.selectedIndex = 1;
        this.isFavorites = true;
      }
      else {
        this.selectedIndex = 0;
        this.isPublications = true;
      }
  }

  get_user_data(username: string) {
    this.userService.getUser(username).subscribe({
      next: (data) => {
        this.user = data;
      },
      error: (e) => {
        console.error(e);
      }
    }).add(
      () => this.isContent = true 
    );   
  }

  open_upload_dialog(): void {
    this.uploadRef = this.dialog.open(UploadComponent, {
      width: '500px',
      autoFocus: false,
    });

    this.uploadRef.afterClosed().subscribe((result) => {
      if (result) {
        this.tellChild('fetch');
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
        (_data) => this.user.followed = true
      ).catch(
        (e) => console.error(e)
      )
      .finally(
        () => this.isBtnOverlayed = false
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
        (_data) => this.user.followed = false
       ).catch(
        (e) => console.error(e)
      ).finally(() => {
          this.isBtnOverlayed = false;
        }
      );
  }

  on_tab_change(event: MatTabChangeEvent): void {
    switch (event.index) {
      case 0:
        this.isPublications = true;
        this.router.navigate(['profile/' + this.user.username]);
        break;
      case 1:
        this.isFavorites = true;
        this.router.navigate(['favorites'], {
          relativeTo: this.activatedRoute,
        });
        break;
      default:
        break;
    }
  }

  async on_file_change(e: any) {
    if (e.target.files && e.target.files.length) {
      this.fileData = e.target.files[0];
    }

    if (this.avatarRef !== undefined) {
      this.avatarRef.close();
    }
    await this.submit_avatar();
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
          }
        )
        .catch(
          (e) => this.sbService.showSnackbar('La demande a échoué avec le statut http ' + e.status, 'Dismiss', 7000)
        )
        .finally(
          () => this.isOverlayed = false
        );
  }

  open_subscriptions_dialog(): void {
    this.dialog.open(FollowingComponent, {
      height: '450px',
      width: '500px',
      data: this.user.username,
      panelClass: ['dialog-window-style'],
      autoFocus: false
    });
  }

  open_subscribers_dialog():void {
    this.dialog.open(FollowerComponent, {
      height: '450px',
      width: '500px',
      data: this.user.username,
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
          this.sbService.showSnackbar(
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
}
