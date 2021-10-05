import { animate, state, style, transition, trigger } from '@angular/animations';
import { Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { lastValueFrom } from 'rxjs';
import { Publication } from 'src/app/interfaces/publication';
import { UserService } from 'src/app/services/user.service';
import { Notifier } from '../userpage.component';

@Component({
    selector: 'userpage-publications',
    templateUrl: './publications.component.html',
    styleUrls: ['./publications.component.css'],
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
export class PublicationsComponent implements OnInit {
    @Input() username!: string;
    @Input() notify = new Notifier();
    @ViewChild("list") postsEl!: ElementRef;

    posts: Publication[] = [];

    private page: number = 0;
    private readonly size: number = 9;
    private readonly sort: string = 'date';

    isLoading: boolean = false;
    canLoad: boolean = false;

    constructor(
        private readonly router: Router,
        private readonly userService: UserService
    ) {}

    async ngOnInit() {
        this.canLoad = true;
        await this.get_user_posts();
        this.notify.valueChanged = async (s: string) => {
            await this.get_latest_post();
        };
    }
    open_post_page(pub: Publication) {
        this.router.navigate(['publication', pub.id], {state: { data: pub }});
    }

    async get_latest_post() {
        lastValueFrom(await this.userService.getUserPosts(this.username, 0, 1, this.sort))
            .then(
                (data) => this.posts = data.concat(this.posts)
            ).catch(
                (e) => console.error(e)
            )
    }

    async get_user_posts() {
        lastValueFrom(await this.userService.getUserPosts(this.username, this.page, this.size, this.sort))
            .then(
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
                }
            )
            .catch(
                (e) => console.error(e)
            )
            .finally(
                () => this.isLoading = false
            );
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
