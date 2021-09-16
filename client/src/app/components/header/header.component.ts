import { Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { lastValueFrom } from 'rxjs';
import { TokenStorageService } from 'src/app/services/token-storage.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'],
})
export class HeaderComponent implements OnInit {
  isLoggedIn: boolean = false;
  username!: string;
  avatar!: string;

  position = new FormControl('below');

  constructor(
    private readonly tokenStorageService: TokenStorageService,
    private readonly router: Router,
    private readonly activatedRoute: ActivatedRoute,
    private readonly userService: UserService
  ) {}

  async ngOnInit(): Promise<void> {
    this.isLoggedIn = !!this.tokenStorageService.getToken();

    if (this.isLoggedIn) {
      const user = this.tokenStorageService.getUser();
      this.username = user.username;
      lastValueFrom(await this.userService.getProfilePicture())
      .then(
        (response) => this.avatar = response.avatar
      )
      .catch(
        (e) => console.error(e)
      )
    }
  }

  open_home_page() {
    this.router.navigate(['/profile/' + this.username]).then((_page) => {
      window.location.reload();
    });
  }

  open_feed_page() {
    this.router.navigate(['/explore/']).then((_page) => {
      window.location.reload();
    });
  }

  open_favorites_page() {
    this.router
      .navigate(['profile/' + this.username + '/favorites'], {
        relativeTo: this.activatedRoute,
      })
      .then((_page) => {
        window.location.reload();
      });
  }

  open_settings_page(): void {}

  sign_out() {
    this.tokenStorageService.signOut();
  }
}
