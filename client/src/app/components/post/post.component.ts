import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Post } from 'src/app/class/post';
import { Commentaire } from 'src/app/class/commentaire';
import { UserService } from 'src/app/services/user.service';
import { PostService } from 'src/app/services/post.service';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmationDialogComponent } from '../shared/confirmation-dialog/confirmation-dialog.component';
import { AuthService } from 'src/app/services/auth.service';
import { TokenStorageService } from 'src/app/services/token-storage.service';

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.css']
})
export class PostComponent implements OnInit {

  id!: Number;
  post: Post = new Post();
  comment: Commentaire = new Commentaire();
  liked!: boolean;

  constructor(private userService: UserService,
    private postService: PostService,
    private tokenService: TokenStorageService,
    private route: ActivatedRoute,
    private router: Router,
    public dialog: MatDialog) { }

  ngOnInit(): void {
    this.route.paramMap.subscribe(paramMap => {
      var a = paramMap.get('id');
      if (a != null) {
        this.id = parseInt(a);
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
    this.postService.submitComment(this.comment).subscribe(
      _data => {
        this.userService.reloadPage();
      },
      _error => {
        return this.userService.showSnackbar("Unknown error occured", 'Dismiss', 7000);
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

  remove(){
    this.postService.removePost(this.id).subscribe(
      _data => {
        console.log(_data);
      }
    )
  }

  openDialog(): void {
    const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
      width: '350px',
      data: "Êtes-vous sûr de vouloir supprimer ce post?",
      panelClass: ["dialog-style"]
    });
    dialogRef.afterClosed().subscribe(result => {
      if(result) {
        this.postService.removePost(this.id).subscribe(
          _data => {
            this.router.navigate([`/profile/${this.tokenService.getUser().username}`]);
          },
          error => {
            return this.userService.showSnackbar("Error occured", 'Dismiss', 7000);
          }
        )
      }
    });
  }
}
