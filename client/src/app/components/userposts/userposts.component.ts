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
  private readonly size: number = 10;
  private readonly sort: string = "date";
  isLoading: boolean = false;
  isLastPage: boolean = false;

  constructor(private readonly router: Router,
              private readonly userService: UserService,
              private readonly parent: HomeComponent) { }

  ngOnInit(): void {
        this.getUserPosts();
  }

  openPostPage(id: number) {
    this.router.navigate(['p', id]);
  }

  getUserPosts() {
    this.userService.getUserPosts(this.parent.username, this.page, this.size, this.sort).subscribe(data => {
      
      if (data.length !== 0){
        this.posts = this.posts.concat(data);
        this.page++;
      }
      else {
        this.isLastPage = true;
      }
      this.isLoading = false;
    });
  }

  onScrollDown(ev: any) {
    if (!this.isLastPage){
      this.isLoading = true;
      setTimeout(() => {
        this.getUserPosts();
      }, 1000);
    } 
  }
}
