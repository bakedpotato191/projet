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

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  @ViewChild('input') input: any;

  mypage!: boolean;
  isContent: boolean = false;
  user!: User;
  posts!: Post[];
  public username!: any;

  uploadRef!: MatDialogRef<UploadComponent>;
  loginRef!: MatDialogRef<LoginComponent>;

  avatarForm!: FormGroup;
  fileData!: Blob;
  imgFile!: String;

  subscription!: Subscription;

  isOverlayed!: boolean;

  ngOnInit(): void {
    this.username = this.activatedRoute.snapshot.paramMap.get('username');
    this.getUserData(this.username);
    this.sharedService.setTitle("@" + this.username);
    if (this.username == this.tokenService.getUser().username) {
      this.mypage = true;
    }
    this.initAvatarForm();
  }

  initAvatarForm() {
    this.avatarForm = this.fb.group({
      avatar: ["", [Validators.nullValidator]]
    });
  }

  constructor(private userService: UserService,
    private readonly activatedRoute: ActivatedRoute,
    private readonly router: Router,
    private readonly tokenService: TokenStorageService,
    private sharedService: SharedService,
    public dialog: MatDialog,
    private fb: FormBuilder) { }

  getUserData(username: String) {
    this.userService.getUser(username).subscribe(data => {
      this.user = data;
      this.isContent = true;
    },
    _error => {
      this.isContent = true;
    });
  }

  openUploadDialog() {
    this.uploadRef = this.dialog.open(UploadComponent);
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
        this.router.navigate([''], { relativeTo: this.activatedRoute, skipLocationChange: true });
        break;
      case 1:
        this.router.navigate([{ outlets: { favorites: ['favorites'] } }], { relativeTo: this.activatedRoute });
        break;
      default:
        break;
    }
  }

  onFileChange(e: any) {

    if (e.target.files && e.target.files.length) {
      this.fileData = e.target.files[0];
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
        this.user.avatar = data;
        this.isOverlayed = false
      },
      error => {
        this.isOverlayed = false
        return this.sharedService.showSnackbar("La demande a échoué avec le statut http " + error.status, 'Dismiss', 7000);
      });
    }, 1000);
  }

  openSubbedDialog() {
    console.log ("clicked subscribbed count");
    this.dialog.open(FollowingComponent, {
      width: '500px',
      data: this.username,
      panelClass: ["dialog-window-style"]
    });

  }

}
