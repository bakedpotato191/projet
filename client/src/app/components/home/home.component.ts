import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { User } from 'src/app/class/user';
import { UserService } from 'src/app/services/user.service';
import { Post } from 'src/app/class/post';
import { TokenStorageService } from 'src/app/services/token-storage.service';
import { MatTabChangeEvent } from '@angular/material/tabs';
import { SharedService } from 'src/app/services/shared.service';
import { UploadComponent } from '../upload/upload.component';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { LoginComponent } from '../login/login.component';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Subscription } from 'rxjs';
import { FollowingComponent } from '../dialogs/following/following.component';
import { AvatarComponent } from '../dialogs/avatar/avatar.component';
import { UserpostsComponent } from '../userposts/userposts.component';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  @ViewChild('input') input: any;
  @ViewChild(UserpostsComponent) userposts: any;

  isMyPage!: boolean;
  username!: string;
  isContent: boolean = false;
  user!: User;
  posts!: Post[];

  uploadRef!: MatDialogRef<UploadComponent>;
  loginRef!: MatDialogRef<LoginComponent>;
  avatarRef!: MatDialogRef<AvatarComponent>;

  avatarForm!: FormGroup;
  fileData!: Blob;
  imgFile!: String;

  subscription!: Subscription;

  isOverlayed!: boolean;

  selectedIndex!: number;

  ngOnInit(): void {
    this.activatedRoute.paramMap
    .subscribe(param => {
      this.username = param.get('username')!;
      this.sharedService.setTitle('@' + this.username);
      this.getUserData(this.username);
      this.isMyPage = this.username == this.tokenService.getUser().username;

    })
    this.initAvatarForm();

    this.activatedRoute.firstChild?.paramMap.subscribe(params => {
      const tab = params.get('tabname');
      this.selectedIndex = tab == 'favorites' ? 1 : 0;
  });
  }

  constructor(private userService: UserService,
    private readonly activatedRoute: ActivatedRoute,
    private readonly router: Router,
    private readonly tokenService: TokenStorageService,
    private readonly sharedService: SharedService,
    public dialog: MatDialog,
    private fb: FormBuilder) { }

  getUserData(username: String): void {
    this.userService.getUser(username).subscribe(data => {
      this.user = data;
      this.isContent = true;
    },
    _error => {
      this.isContent = true;
    });
  }

  initAvatarForm() {
    this.avatarForm = this.fb.group({
      avatar: ["", [Validators.nullValidator]]
    });
  }

  openUploadDialog(): void {
    this.uploadRef = this.dialog.open(UploadComponent, {
      data: this.userposts
    });

    this.uploadRef.afterClosed().subscribe(result => {
      if (result) {
        this.userposts.getNewPost();
      }
    });
  }

  follow() {
    if (this.tokenService.getToken() == null) {
      return this.dialog.open(LoginComponent);
    }
    this.user.followed = true;
    return this.userService.follow(this.user.username).subscribe();
  }

  unfollow() {
    if (this.tokenService.getToken() == null) {
      return this.dialog.open(LoginComponent);
    }
    this.user.followed = false;
    return this.userService.unfollow(this.user.username).subscribe();
  }

  onTabChanged(event: MatTabChangeEvent): void {
    switch (event.index) {
      case 0:
        this.router.navigate([''], { relativeTo: this.activatedRoute });
        break;
      case 1:
        this.router.navigate(['favorites'], { relativeTo: this.activatedRoute });
        break;
      default:
        break;
    }
  }

  onFileChange(e: any) {
    if (e.target.files && e.target.files.length) {
      this.fileData = e.target.files[0];
    }

    if (this.avatarRef !== undefined) {
      this.avatarRef.close();
    }

    this.submitAvatar();
  }

  submitAvatar() {
    if (this.avatarForm.invalid){
      return;
    }

    var formData: any = new FormData();
    formData.append("avatar", this.fileData);
    this.isOverlayed = true;
    setTimeout(() => {
      this.userService.setProfilePicture(formData).subscribe(data => {
        this.user.avatar = data.avatar;
        this.user.has_avatar = data.has_avatar;
      },
      error => {
        return this.sharedService.showSnackbar("La demande a échoué avec le statut http " + error.status, 'Dismiss', 7000);
      },
      () => {
        this.isOverlayed = false
      });
    }, 1000);
  }

  openSubbedDialog(): void {
    this.dialog.open(FollowingComponent, {
      height: '450px',
      width: '500px',
      data: this.username,
      panelClass: ["dialog-window-style"]
    });
  }

  click(): void {
    if (!this.isMyPage){
      return;
    }
    if (this.user.has_avatar) {
      this.avatarRef = this.dialog.open(AvatarComponent, {
        width: '400px',
        panelClass: ["dialog-window-style"],
        data: this.input,
        autoFocus: false
      });
      this.avatarRef.afterClosed().subscribe(result => {
        if (result === 'delete') {
          this.isOverlayed = true;
          setTimeout(() => {
            this.userService.deleteProfilePicture().subscribe(data => {
              this.user.has_avatar = data.has_avatar;
              this.user.avatar = data.avatar;
            },
            error => {
              this.sharedService.showSnackbar(JSON.stringify("La requete DELETE a échoué avec le code statut" + error.status), 'Dismiss', 7000);
            },
            () => {
              this.isOverlayed = false;
            });
          }, 1000);
        }
      })
    }
    else {
      this.input.nativeElement.click();
    }
  }

}
