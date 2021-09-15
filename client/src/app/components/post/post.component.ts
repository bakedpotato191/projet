import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { PostService } from 'src/app/services/post.service';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { TokenStorageService } from 'src/app/services/token-storage.service';
import { SharedService } from 'src/app/services/shared.service';
import { LoginComponent } from '../login/login.component';
import { Commentaire } from 'src/app/interfaces/commentaire';
import { Publication } from 'src/app/interfaces/publication';
import { ConfirmationDialogComponent } from '../shared/dialogs/confirmation-dialog/confirmation-dialog.component';
import { lastValueFrom } from 'rxjs';

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.css'],
})
export class PostComponent implements OnInit {

  id!: number;
  post!: Publication;
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

  async ngOnInit(): Promise<void> {
    this.activatedRoute.paramMap.subscribe((paramMap) => {
      var id = paramMap.get('id');
      if (id != null) {
        this.id = parseInt(id);
      }
    });
    lastValueFrom(await this.postService.getPostById(this.id))
    .then(
      (response) => {
        this.post = response;
        this.isContent = true;
        if (this.post.utilisateur.username === this.tokenService.getUser().username) {
          this.isMyPost = true;
        }
      },
      (error) => {
        console.log(error);
      }
    )
    .finally(() => {
      this.isContent = true;
    });
  }

  async like(): Promise<void> {
    if (this.tokenService.getToken() == null) {
      this.dialog.open(LoginComponent);
    }
    lastValueFrom(await this.postService.likePost(this.id))
    .then(
      (_response) => {
        this.post.liked = true;
      },
      (error) => {
        console.log(error);
      });
  }

  async dislike(): Promise<void> {

    lastValueFrom(await this.postService.dislikePost(this.id)).then(
      (_data) => {
        this.post.liked = false;
      },
      (error) => {
        console.log(error);
      });
  }

  open_image(url: string) {
    window.location.href=url;
  }

  async remove_publication(id: number){
    lastValueFrom(await this.postService.removePost(id))
    .then(
      (_data) => {
        this.router.navigate([
          `/profile/${this.tokenService.getUser().username}`,
        ]);
      },
      (error) => {
        console.log(error);
        this.sharedService.showSnackbar(
          'Request failed with status code ' + error.status,
          'Dismiss',
          5000
        );
      }
    );
  }

  open_delete_post_dialog(id: number): void {
    const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
      width: '350px',
      data: 'Êtes-vous sûr de vouloir supprimer ce post?',
      panelClass: ['dialog-style'],
    });
    dialogRef.afterClosed().subscribe(async (result) => {
      if (result) {
        await this.remove_publication(id);
      }
    });
  }
}
