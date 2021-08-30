import { Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { TokenStorageService } from 'src/app/services/token-storage.service';
import { UserService } from 'src/app/services/user.service';

@Component({
    selector: 'app-header',
    templateUrl: './header.component.html',
    styleUrls: ['./header.component.css']
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

    ngOnInit(): void {
        this.isLoggedIn = !!this.tokenStorageService.getToken();

        if (this.isLoggedIn) {
            const user = this.tokenStorageService.getUser();
            this.username = user.username;
            this.userService.getProfilePicture().subscribe(
                (data) => {
                    this.avatar = data.avatar;
                },
                (error) => {
                    console.log(error);
                }
            );
        }
    }

    open_home_page() {
        this.router.navigate(['/profile/' + this.username]).then((_page) => {
            window.location.reload();
        });
    }

    open_favorites_page() {
        this.router
            .navigate(['profile/' + this.username + '/favorites'], {
                relativeTo: this.activatedRoute
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
