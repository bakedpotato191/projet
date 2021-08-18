import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { User } from 'src/app/class/user';
import { UserService } from 'src/app/services/user.service';
import { Post } from 'src/app/class/post';
import { UploadformService } from 'src/app/services/uploadform.service';
import { TokenStorageService } from 'src/app/services/token-storage.service';
import { MatTabChangeEvent } from '@angular/material/tabs';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {


  mypage!: boolean;
  user!: User;
  posts!: Post[];
  public username!: any;

  ngOnInit(): void {
    this.username = this.activatedRoute.snapshot.paramMap.get('username');
    if (this.username !== null) {
      this.getUserData(this.username);
      if (this.username == this.tokenService.getUser().username) {
        this.mypage = true;
      }
    }
  }

  constructor(private userService: UserService,
    private readonly activatedRoute: ActivatedRoute,
    private readonly router: Router,
    private readonly tokenService: TokenStorageService,
    private uploadService: UploadformService
  ) { }

    getUserData(username: String) {
      this.userService.getUser(username).subscribe(data => {
        this.user = data;
      });
    }

    openDialog(): void {
      this.uploadService.open();
    }

    follow() {
      this.user.followed = true;
      this.userService.follow(this.user.username).subscribe();
    }

    unfollow() {
      this.user.followed = false;
      this.userService.unfollow(this.user.username).subscribe();
    }

    onTabChanged(event: MatTabChangeEvent): void {
      switch(event.index) {
        case 0:
          this.router.navigate([''], {relativeTo: this.activatedRoute, skipLocationChange: true});
          break;
        case 1: 
          this.router.navigate([{ outlets: { favorites: ['favorites'] } }],
          { relativeTo: this.activatedRoute });
          break;
        default:
        break;
    }
  }
}