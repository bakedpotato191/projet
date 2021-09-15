import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { lastValueFrom } from 'rxjs';
import { Publication } from 'src/app/interfaces/publication';

import { UserService } from 'src/app/services/user.service';

@Component({
    selector: 'app-favorites',
    templateUrl: './favorites.component.html',
    styleUrls: ['./favorites.component.css']
})
export class FavoritesComponent implements OnInit {
    favorites: Publication[] = [];
    post!: Publication;
    canLoad: boolean = false;
    isLoading: boolean = false;

    //paging
    private page: number = 0;
    private readonly size: number = 10;
    private readonly sort: string = 'date';
    //

    constructor(
        private readonly router: Router,
        private readonly userService: UserService
    ) {}

    ngOnInit(): void {
        this.canLoad = true;
        this.get_favorites();
    }

    open_post_page(id: number) {
        this.router.navigate(['p', id]);
    }

    async get_favorites(): Promise<void> {
        lastValueFrom(await this.userService
            .getFavoritePosts(this.page, this.size, this.sort))
            .then(
                (response) => {
                if (response.length) {
                    this.favorites = this.favorites.concat(response);
                    this.page++;
                    this.canLoad = true;

                    if (response.length < this.size) {
                        this.canLoad = false;
                    }
                } else {
                    this.canLoad = false;
                }
                this.isLoading = false;
            },
            (error) => {
                console.log(error);
            });
    }

    on_scroll_down(): void {
        if (this.canLoad) {
            this.canLoad = false;
            this.isLoading = true;
            setTimeout(() => {
                this.get_favorites();
            }, 1500);
        }
    }
}
