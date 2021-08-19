import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Post } from 'src/app/class/post';
import { UserService } from 'src/app/services/user.service';
import { HomeComponent } from '../home/home.component';

@Component({
  selector: 'app-userposts',
  templateUrl: './userposts.component.html',
  styleUrls: ['./userposts.component.css']
})
export class UserpostsComponent implements OnInit {

  posts: Post[] = [];
  private page: number = 0;
  private readonly size: number = 9;
  private readonly sort: string = "date";
  isLoading = false;
  canLoad = false;

  constructor(private readonly router: Router,
              private readonly userService: UserService,
              private readonly parent: HomeComponent) { }

  ngOnInit(): void {
      this.canLoad = true;
      this.getUserPosts();
  }

  openPostPage(id: number) {
    this.router.navigate(['p', id]);
  }

  getUserPosts() {
    this.userService.getUserPosts(this.parent.username, this.page, this.size, this.sort).subscribe(data => {
      
      if (data.length !== 0) {
        this.posts = this.posts.concat(data);
        this.page++;
        this.canLoad = true;
        
        if (data.length < this.size) {
          this.canLoad = false;
        }
      }
      else {
        this.canLoad = false;
      }
      this.isLoading = false;
    });
  }

  onScrollDown() {
    if (this.canLoad) {
      this.canLoad = false;
      this.isLoading = true;
      setTimeout(() => {
        this.getUserPosts();
      }, 1000);
    }
  }
}
