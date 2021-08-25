import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Post } from 'src/app/class/post';
import { Commentaire } from 'src/app/class/commentaire';
import { PostService } from 'src/app/services/post.service';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { ConfirmationDialogComponent } from '../dialogs/confirmation-dialog/confirmation-dialog.component';
import { TokenStorageService } from 'src/app/services/token-storage.service';
import { CommentService } from 'src/app/services/comment.service';
import { SharedService } from 'src/app/services/shared.service';
import { CommentsComponent } from '../comments/comments.component';
import { LoginComponent } from '../login/login.component';

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.css']
})
export class PostComponent implements OnInit {
  @ViewChild(CommentsComponent) child!: CommentsComponent;
  @ViewChild('textarea') inputName: any;

  id!: Number;
  post!: Post;
  comment: Commentaire = new Commentaire();
  liked!: boolean;
  isLoading = false;
  isMyPost!: boolean;

  loginRef!: MatDialogRef<LoginComponent>;

  constructor ( private readonly postService: PostService,
                private readonly commentService: CommentService,
                private readonly sharedService: SharedService,
                private readonly tokenService: TokenStorageService,
                private readonly route: ActivatedRoute,
                private readonly router: Router,
                private dialog: MatDialog) { }

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
        if (this.post.utilisateur.username === this.tokenService.getUser().username){
          this.isMyPost = true;
        }
      }
    );
  }

  onSubmit(id: number) {
    if (this.tokenService.getToken() == null) {
      this.dialog.open(LoginComponent);
      return;
    }
    this.comment.id = id;
    this.isLoading = true;
      this.commentService.submitComment(this.comment).subscribe(
        _data => {
          this.child.comments = [];
          this.child.ngOnInit();
          this.isLoading = false;
          this.inputName.nativeElement.value='';
        },
        _error => {
          this.isLoading = false;
          return this.sharedService.showSnackbar("Unknown error occured", 'Dismiss', 7000);
        }
      );
  }

  like() {
    if (this.tokenService.getToken() == null) {
      return this.dialog.open(LoginComponent);
    }
    this.post.liked = true;
    return this.postService.likePost(this.id).subscribe();
  }

  dislike() {
    this.post.liked = false;
    this.postService.dislikePost(this.id).subscribe();
  }

  remove() {
    this.postService.removePost(this.id).subscribe();
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
          error => {
            return this.sharedService.showSnackbar("Request failed with status code " + error.status, 'Dismiss', 5000);
          }
        )
      }
    });
  }
}
