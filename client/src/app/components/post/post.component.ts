import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Post } from 'src/app/class/post';
import { Comment } from 'src/app/class/comment';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.css']
})
export class PostComponent implements OnInit {

  id!: Number;
  post: Post = new Post();
  comment: Comment = new Comment();

  constructor(private userService: UserService, 
              private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.route.paramMap.subscribe(paramMap => {
      var a = paramMap.get('id');
      if (a != null){
        this.id = parseInt(a);
      }
    });
    this.userService.getPostById(this.id).subscribe(
      data => {
        this.post = data;
      },
      error => {
        console.log(error);
      }
    );
  }

  onSubmit(){
    this.comment.id = this.id;
    this.userService.submitComment(this.comment).subscribe(
      data => {
        console.log(data);
      },
      error => {
        console.log(error);
      }
    );
  }
 
}
