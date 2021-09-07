import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { PostService } from 'src/app/services/post.service';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { ConfirmationDialogComponent } from '../dialogs/confirmation-dialog/confirmation-dialog.component';
import { TokenStorageService } from 'src/app/services/token-storage.service';
import { SharedService } from 'src/app/services/shared.service';
import { LoginComponent } from '../login/login.component';
import { Commentaire } from 'src/app/interfaces/commentaire';
import { Post } from 'src/app/interfaces/post';

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.css'],
})
export class PostComponent implements OnInit {

  id!: number;
  post!: Post;
  comment!: Commentaire;
  isLoading = false;
  isMyPost!: boolean;
  isContent: boolean = false;

  loginRef!: MatDialogRef<LoginComponent>;

  constructor(
    private readonly postService: PostService,
    private readonly sharedService: SharedService,
    private readonly tokenService: TokenStorageService,
    private readonly activatedRoute: ActivatedRoute,
    private readonly router: Router,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.activatedRoute.paramMap.subscribe((paramMap) => {
      var id = paramMap.get('id');
      if (id != null) {
        this.id = parseInt(id);
      }
    });
    this.postService.getPostById(this.id).subscribe(
      (data) => {
        this.post = data;
        this.isContent = true;
        if (this.post.utilisateur.username === this.tokenService.getUser().username) {
          this.isMyPost = true;
        }
      },
      (_error) => {
        this.isContent = true;
      }
    );
  }

  like(): void {
    if (this.tokenService.getToken() == null) {
      this.dialog.open(LoginComponent);
    }
    this.post.liked = true;
    this.postService.likePost(this.id).subscribe();
  }

  dislike(): void {
    this.post.liked = false;
    this.postService.dislikePost(this.id).subscribe();
  }

  remove(): void {
    this.postService.removePost(this.id).subscribe();
  }

  open_delete_post_dialog(id: number): void {
    const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
      width: '350px',
      data: 'Êtes-vous sûr de vouloir supprimer ce post?',
      panelClass: ['dialog-style'],
    });
    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.postService.removePost(id).subscribe(
          (_data) => {
            this.router.navigate([
              `/profile/${this.tokenService.getUser().username}`,
            ]);
          },
          (error) => {
            this.sharedService.showSnackbar(
              'Request failed with status code ' + error.status,
              'Dismiss',
              5000
            );
          }
        );
      }
    });
  }
}
