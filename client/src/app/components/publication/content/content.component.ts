import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PostService } from 'src/app/services/post.service';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { TokenStorageService } from 'src/app/services/token-storage.service';
import { SharedService } from 'src/app/services/shared.service';
import { LoginComponent } from '../../login/login.component';
import { Commentaire } from 'src/app/interfaces/commentaire';
import { Publication } from 'src/app/interfaces/publication';
import { ConfirmationDialogComponent } from '../../userpage/dialogs/confirmation/confirmation-dialog.component';
import { lastValueFrom } from 'rxjs';

@Component({
  selector: 'app-publication',
  templateUrl: './content.component.html',
  styleUrls: ['./content.component.css'],
})
export class ContentComponent implements OnInit {
  
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
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    private dialog: MatDialog
  ) { 
      this.post = this.router.getCurrentNavigation()?.extras.state?.data;
  }

  ngOnInit() {
    
    if (this.post) { /* https://stackoverflow.com/a/44017547 */
      this.isContent = true;
      return;
    }

    this.route.paramMap.subscribe((paramMap) => {
      const param = paramMap.get('id');
      if (param) {
        this.id = parseInt(param);
        this.get_post_by_id(this.id);
      }
    });
  }

  get_post_by_id(id: number) {
    this.postService.getPostById(id).subscribe({
        next: (data) => {
          this.post = data;

          if (this.post.utilisateur.username === this.tokenService.getUser().username) {
            this.isMyPost = true;
          }
        },
        error: (e) => console.error(e),
      })
      .add(() => this.isContent = true);
  }

  async like(id: number): Promise<void> {
    if (this.tokenService.getToken() == null) {
      this.dialog.open(LoginComponent);
    }
    lastValueFrom(await this.postService.likePost(id))
      .then(
        (_data) => this.post.liked = true
      )
      .catch(
        (e) => console.error(e)
      );
  }

  async dislike(id: number): Promise<void> {
    lastValueFrom(await this.postService.dislikePost(id))
      .then(
        (_data) => this.post.liked = false
      )
      .catch(
        (e) => console.error(e)
      );
  }

  async remove_publication(id: number) {
    lastValueFrom(await this.postService.removePost(id))
      .then(
        (_data) => this.router.navigate([`/profile/${this.tokenService.getUser().username}`])
      )
      .catch((e) => {
        console.error(e);
        this.sharedService.showSnackbar(
          'Request failed with status code ' + e.status,
          'Dismiss',
          5000
        );
      });
  }

  open_image(url: string) {
    window.location.href = url;
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
