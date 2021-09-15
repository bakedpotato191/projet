import { Component, Inject, OnInit } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { lastValueFrom } from 'rxjs';
import { LoginComponent } from 'src/app/components/login/login.component';
import { User } from 'src/app/interfaces/user';
import { TokenStorageService } from 'src/app/services/token-storage.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-follower',
  templateUrl: './follower.component.html',
  styleUrls: ['../css/styles.css'],
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

  async ngOnInit() {
    this.canLoad = true;
    await this.get_subscribers();
  }

  close_dialog(): void {
    this.followingRef.close();
  }

  async get_subscribers() {
    this.isLoading = true;
    lastValueFrom(await this.userService
      .getSubscribers(this.username, this.page, this.size, this.sort))
      .then(
        (response) => {
          this.followers = response;
        },
        (error) => {
          console.log(error);
        }
      )
      .finally(() => {
        this.isLoading = false;
      });
  }

  async follow(following: any) {
    if (this.tokenService.getToken() == null) {
      this.dialog.open(LoginComponent);
      return;
    }
    this.isOverlayed = true;
    lastValueFrom(await this.userService.follow(following.username))
      .then(
        (_response) => {
          following.followed = true;
        },
        (error) => {
          console.log(error);
        }
      )
      .finally(() => {
        this.isOverlayed = false;
      });
  }

  async unfollow(follower: any) {
    if (this.tokenService.getToken() == null) {
      this.dialog.open(LoginComponent);
      return;
    }
    this.isOverlayed = true;
    lastValueFrom(await this.userService.unfollow(follower.username))
      .then(
        (_data) => {
          follower.followed = false;
        },
        (error) => {
          console.log(error);
        }
      )
      .finally(() => {
        this.isOverlayed = false;
      });
  }
}
