import { animate, state, style, transition, trigger } from '@angular/animations';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Post } from 'src/app/interfaces/post';
import { UserService } from 'src/app/services/user.service';
import { UserPageComponent } from '../userpage/userpage.component';

@Component({
    selector: 'app-userposts',
    templateUrl: './userposts.component.html',
    styleUrls: ['./userposts.component.css'],
    animations: [
        trigger('inAnimation', [
            state('in', style({ opacity: 1 })),
            transition(':enter', [
                style({ opacity: '0' }),
                animate('.5s ease-out', style({ opacity: '1' }))
            ])
        ])
    ]
})
export class UserpostsComponent implements OnInit {
    posts: Post[] = [];

    private page: number = 0;
    private readonly size: number = 9;
    private readonly sort: string = 'date';

    isLoading: boolean = false;
    canLoad: boolean = false;

    constructor(
        private readonly router: Router,
        private readonly userService: UserService,
        private readonly parent: UserPageComponent
    ) {}

    ngOnInit(): void {
        this.canLoad = true;
        this.get_user_posts();
    }

    open_post_page(id: number) {
        this.router.navigate(['p', id]);
    }

    get_latest_post() {
        this.userService
            .getUserPosts(this.parent.username, 0, 1, this.sort)
            .subscribe((data) => {
                this.posts = data.concat(this.posts);
            });
    }

    get_user_posts() {
        this.userService
            .getUserPosts(this.parent.username, this.page, this.size, this.sort)
            .subscribe(
                (data) => {
                    if (data.length !== 0) {
                        this.posts = this.posts.concat(data);
                        this.page++;
                        this.canLoad = true;

                        if (data.length < this.size) {
                            this.canLoad = false;
                        }
                    } else {
                        this.canLoad = false;
                    }
                },
                (error) => {
                    console.log(error);
                }
            )
            .add(() => {
                this.isLoading = false;
            });
    }

    handle_scroll_down() {
        if (this.canLoad) {
            this.canLoad = false;
            this.isLoading = true;
            setTimeout(() => {
                this.get_user_posts();
            }, 1000);
        }
    }
}
