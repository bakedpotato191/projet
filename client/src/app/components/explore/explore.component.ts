import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { Publication } from 'src/app/interfaces/publication';
import { PostService } from 'src/app/services/post.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-explore',
  templateUrl: './explore.component.html',
  styleUrls: ['./explore.component.css'],
})
export class ExploreComponent implements OnInit {
  
  isLoading: boolean = false;
  canLoad: boolean = false;
  private page: number = 0;
  private readonly size: number = 9;

  posts: Publication[] = [];

  options: FormGroup;
  hideRequiredControl = new FormControl(false);
  floatLabelControl = new FormControl('auto');

  constructor(private userService: UserService, 
              private postService: PostService,
              private router: Router,
    fb: FormBuilder) {
    this.options = fb.group({
      hideRequired: this.hideRequiredControl,
      floatLabel: this.floatLabelControl,
    });
  }

  ngOnInit(): void {
    this.get_new_publications();
  }

  get_new_publications ():void {
    this.userService.getNewPublications(this.page, this.size).subscribe(
      (data) => {
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
        
      },
      (error) => {
        console.log(error);
      }
    );
  }

  like(post: Publication): void {
    post.liked = true;
    post.countLike += 1;
    this.postService.likePost(post.id).subscribe();
  }

  dislike(post: Publication): void {
    post.liked = false;
    post.countLike -= 1;
    this.postService.dislikePost(post.id).subscribe();
  }

  open_post_page(id: number) {
    this.router.navigate(['p', id]);
  }

  handle_scroll_down() {
    if (this.canLoad) {
        this.canLoad = false;
        this.isLoading = true;
        setTimeout(() => {
            this.get_new_publications();
        }, 1000);
    }
}
}
