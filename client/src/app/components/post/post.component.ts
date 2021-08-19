import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Post } from 'src/app/class/post';
import { Commentaire } from 'src/app/class/commentaire';
import { UserService } from 'src/app/services/user.service';
import { PostService } from 'src/app/services/post.service';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmationDialogComponent } from '../shared/confirmation-dialog/confirmation-dialog.component';
import { TokenStorageService } from 'src/app/services/token-storage.service';
import { CommentService } from 'src/app/services/comment.service';
import { SharedService } from 'src/app/services/shared.service';
import { CommentsComponent } from '../comments/comments.component';

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.css']
})
export class PostComponent implements OnInit {
  @ViewChild(CommentsComponent)
  child!: CommentsComponent;


  id!: Number;
  post!: Post;
  comment: Commentaire = new Commentaire();
  liked!: boolean;

  constructor ( private readonly postService: PostService,
                private commentService: CommentService,
                private sharedService: SharedService,
                private readonly tokenService: TokenStorageService,
                private readonly route: ActivatedRoute,
                private readonly router: Router,
                public dialog: MatDialog) { }

  ngOnInit(): void {
    this.route.paramMap.subscribe(paramMap => {
      var id = paramMap.get('id');
      if (id != null) {
        this.id = parseInt(id);
      }
    });
    this.postService.getPostById(this.id).subscribe(
      data => {
        this.post = data;
      }
    );
  }

  onSubmit() {
    this.comment.id = this.id;
    this.commentService.submitComment(this.comment).subscribe(
      _data => {
        this.child.comments = [];
        this.child.ngOnInit();
      },
      _error => {
        return this.sharedService.showSnackbar("Unknown error occured", 'Dismiss', 7000);
      }
    );
  }

  like() {
    this.post.liked = true;
    this.postService.likePost(this.id).subscribe();
  }

  dislike() {
    this.post.liked = false;
    this.postService.dislikePost(this.id).subscribe();
  }

  remove() {
    this.postService.removePost(this.id).subscribe(
      _data => {
        console.log(_data);
      }
    )
  }

  openDeletePostDialog(id: Number): void {
    const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
      width: '350px',
      data: "Êtes-vous sûr de vouloir supprimer ce post?",
      panelClass: ["dialog-style"]
    });
    dialogRef.afterClosed().subscribe(result => {
      if(result) {
        this.postService.removePost(id).subscribe(
          _data => {
            this.router.navigate([`/profile/${this.tokenService.getUser().username}`]);
          },
          _error => {
            return this.sharedService.showSnackbar("Error occured", 'Dismiss', 5000);
          }
        )
      }
    });
  }
}
