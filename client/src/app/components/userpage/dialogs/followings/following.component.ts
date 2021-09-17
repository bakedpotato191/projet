import { Component, Inject, OnInit, Renderer2 } from '@angular/core';
import {
  MatDialog,
  MatDialogRef,
  MAT_DIALOG_DATA,
} from '@angular/material/dialog';
import { firstValueFrom, lastValueFrom } from 'rxjs';
import { LoginComponent } from 'src/app/components/login/login.component';
import { User } from 'src/app/interfaces/user';
import { TokenStorageService } from 'src/app/services/token-storage.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'userpage-shared-following',
  templateUrl: './following.component.html',
  styleUrls: ['../css/styles.css'],
})
export class FollowingComponent implements OnInit {
  private page: number = 0;
  private readonly size: number = 9;
  private readonly sort: string = 'date';
  canLoad: boolean = false;
  isLoading: boolean = false;
  isContent: boolean = false;

  followings: User[] = [];
  following!: any;

  loginRef!: MatDialogRef<LoginComponent>;

  constructor(
    private readonly userService: UserService,
    private readonly tokenService: TokenStorageService,
    public dialog: MatDialog,
    private renderer: Renderer2,
    public followingRef: MatDialogRef<FollowingComponent>,
    @Inject(MAT_DIALOG_DATA) public username: string
  ) { }

  async ngOnInit(): Promise<void> {
    this.canLoad = true;
    await this.get_subscriptions();
  }

  close_dialog(): void {
    this.followingRef.close();
  }

  async get_subscriptions(): Promise<void> {
    this.isLoading = true;
    firstValueFrom(await this.userService.getSubscriptions(this.username, this.page, this.size, this.sort))
      .then(
        (response) => {
          this.followings = this.followings.concat(response);
        },
        (error) => {
          console.log(error);
        })
      .finally(() => {
        this.isLoading = false;
        this.isContent = true;
      });
  }

  async follow(following: User, e: Event) {
    if (this.tokenService.getToken() == null) {
      this.dialog.open(LoginComponent);
      return;
    }
    this.renderer.addClass(e.currentTarget, 'button--loading');
      lastValueFrom(await this.userService
        .follow(following.username))
        .then(
          (_response) => {
            following.followed = true;
          },
          (error) => {
            console.log(error);
          }
        )
        .finally(() => {
          this.renderer.removeClass(e.currentTarget, 'button--loading');
        });
  }

  async unfollow(following: any, e: Event) {
    if (this.tokenService.getToken() == null) {
      this.dialog.open(LoginComponent);
      return;
    }
    this.renderer.addClass(e.currentTarget, 'button--loading');
    lastValueFrom(await this.userService.unfollow(following.username))
        .then(
          (_response) => {
            following.followed = false;
          },
          (error) => {
            console.log(error);
          }
        )
        .finally(() => {
          this.renderer.removeClass(e.currentTarget, 'button--loading');
        });
  }
}
