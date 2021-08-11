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

  constructor(
    private readonly router: Router,
    private userService: UserService) { }

  ngOnInit(): void {
    this.userService.getFavoritePosts().subscribe(
      data => {
        console.log(data);
        this.favorites = data;
      },
      _error => {
        console.log(_error);

      }
    )
  }

  openPostPage(id: number) {
    this.router.navigate(['p', id]);
  }

}

