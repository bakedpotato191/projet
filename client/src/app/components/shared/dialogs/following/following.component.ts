import { Component, Inject, OnInit } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { LoginComponent } from 'src/app/components/login/login.component';
import { User } from 'src/app/interfaces/user';
import { TokenStorageService } from 'src/app/services/token-storage.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'shared-following',
  templateUrl: './following.component.html',
  styleUrls: ['../css/styles.css']
})
export class FollowingComponent implements OnInit {
  private page: number = 0;
  private readonly size: number = 9;
  private readonly sort: string = 'date';
  canLoad: boolean = false;
  isLoading: boolean = false;
  isOverlayed: boolean = false;
  
  followings: User[] = [];
  following!: any;

  loginRef!: MatDialogRef<LoginComponent>;

  constructor(
    private readonly userService: UserService,
    private readonly tokenService: TokenStorageService,
    public dialog: MatDialog,
    public followingRef: MatDialogRef<FollowingComponent>,
    @Inject(MAT_DIALOG_DATA) public username: string
  ) {}

  ngOnInit(): void {
    this.canLoad = true;
    this.get_subscriptions();
  }

  close_dialog(): void {
    this.followingRef.close();
  }

  get_subscriptions(): void {
    this.isLoading = true;
      this.userService
        .getSubscriptions(this.username, this.page, this.size, this.sort)
        .subscribe(
          (data) => {
            this.followings = data;
          },
          (error) => {
            console.log(error);
          }
        )
        .add(() => {
          this.isLoading = false;
        });
  }

  follow(following: any) {
    if (this.tokenService.getToken() == null) {
      return this.dialog.open(LoginComponent);
    }
    this.isOverlayed = true;
    console.log(following.to.username);
    setTimeout(() => {
        return this.userService.follow(following.to.username).subscribe(
            (_data) => {
              following.to.followed = true;
            },
            (error) => {
              console.log(error);
            }
          ).add(
              () => {
                this.isOverlayed = false;
              }
          );
      }, 1000);
      return;
  }

  unfollow(following: any) {
    if (this.tokenService.getToken() == null) {
      return this.dialog.open(LoginComponent);
    }
    console.log(following.to.username);
    this.isOverlayed = true;
    setTimeout(() => {
        return this.userService.unfollow(following.to.username).subscribe(
            (_data) => {
              following.to.followed = false;
            },
            (error) => {
              console.log(error);
            }
          ).add(
          () => {
            this.isOverlayed = false;
          }
          );
      }, 1000);
      return;
  }
}
