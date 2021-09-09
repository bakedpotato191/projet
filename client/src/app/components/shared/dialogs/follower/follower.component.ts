import { Component, Inject, OnInit } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { LoginComponent } from 'src/app/components/login/login.component';
import { User } from 'src/app/interfaces/user';
import { TokenStorageService } from 'src/app/services/token-storage.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-follower',
  templateUrl: './follower.component.html',
  styleUrls: ['../css/styles.css']
})
export class FollowerComponent implements OnInit {

  private page: number = 0;
  private readonly size: number = 9;
  private readonly sort: string = 'date';
  canLoad: boolean = false;
  isLoading: boolean = false;
  isOverlayed: boolean = false;
  
  followers: User[] = [];
  follower!: any;

  loginRef!: MatDialogRef<LoginComponent>;

  constructor(
    private readonly userService: UserService,
    private readonly tokenService: TokenStorageService,
    public dialog: MatDialog,
    public followingRef: MatDialogRef<FollowerComponent>,
    @Inject(MAT_DIALOG_DATA) public username: string
  ) {}

  ngOnInit(): void {
    this.canLoad = true;
    this.get_subscribers();
  }

  close_dialog(): void {
    this.followingRef.close();
  }

  get_subscribers(): void {
    this.isLoading = true;
      this.userService
        .getSubscribers(this.username, this.page, this.size, this.sort)
        .subscribe(
          (data) => {
            this.followers = data;
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
    console.log(following.from.username);
    setTimeout(() => {
        return this.userService.follow(following.from.username).subscribe(
            (_data) => {
              following.from.followed = true;
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

  unfollow(follower: any) {
    if (this.tokenService.getToken() == null) {
      return this.dialog.open(LoginComponent);
    }
    console.log(follower.to.username);
    this.isOverlayed = true;
    setTimeout(() => {
        return this.userService.unfollow(follower.to.username).subscribe(
            (_data) => {
              follower.to.followed = false;
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
