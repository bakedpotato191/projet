import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Favorite } from 'src/app/class/favorite';
import { Post } from 'src/app/class/post';
import { UserService } from 'src/app/services/user.service';

@Component({
    selector: 'app-favorites',
    templateUrl: './favorites.component.html',
    styleUrls: ['./favorites.component.css']
})
export class FavoritesComponent implements OnInit {
    favorites: Favorite[] = [];
    post!: Post;
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

    get_favorites(): void {
        this.userService
            .getFavoritePosts(this.page, this.size, this.sort)
            .subscribe((data) => {
                if (data.length) {
                    this.favorites = this.favorites.concat(data);
                    this.page++;
                    this.canLoad = true;

                    if (data.length < this.size) {
                        this.canLoad = false;
                    }
                } else {
                    this.canLoad = false;
                }
                this.isLoading = false;
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
