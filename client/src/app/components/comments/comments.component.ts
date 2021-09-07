import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { Commentaire } from 'src/app/interfaces/commentaire';
import { CommentService } from 'src/app/services/comment.service';
import { SharedService } from 'src/app/services/shared.service';
import { PostComponent } from '../post/post.component';
import { ConfirmationDialogComponent } from '../dialogs/confirmation-dialog/confirmation-dialog.component';
import { TokenStorageService } from 'src/app/services/token-storage.service';
import { LoginComponent } from '../login/login.component';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
    selector: 'app-comments',
    templateUrl: './comments.component.html',
    styleUrls: ['./comments.component.css']
})
export class CommentsComponent implements OnInit {
    @ViewChild('textarea') inputName!: ElementRef;

    commentForm!: FormGroup;
    comments!: Commentaire[];
    comment!: Commentaire;
    loginRef!: MatDialogRef<LoginComponent>;

    private page: number = 0;
    private readonly size: number = 9;
    private readonly sort: string = 'date';
    canLoad: boolean = false;
    isLoading = false;

    constructor(
        private readonly commentService: CommentService,
        private readonly sharedService: SharedService,
        private readonly tokenService: TokenStorageService,
        public dialog: MatDialog,
        private readonly parent: PostComponent,
        private fb: FormBuilder
    ) {}

    ngOnInit(): void {
        this.get_all_comments();
        this.init_comment_form();
    }

    init_comment_form(): void {
        this.commentForm = this.fb.group({
            comment: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(128)]],
            post_id: [this.parent.post.id]
        });
    }

    submit() {
        if (this.tokenService.getToken() == null) {
          this.dialog.open(LoginComponent);
          return;
        }

        if (this.commentForm.invalid){
            return;
        }

        this.isLoading = true;
        this.commentService.submitComment(this.commentForm.value).subscribe(
          (_data) => {
            this.comments = [];
            this.isLoading = false;
            this.inputName.nativeElement.value = '';
          },
          (_error) => {
            this.isLoading = false;
            this.sharedService.showSnackbar(
              'Unknown error occured',
              'Dismiss',
              7000
            );
          }
        ).add(
            () => {
                this.ngOnInit();
            }
        );
      }

    get_all_comments(): void {
        this.commentService
            .getPostComments(this.parent.id, this.page, this.size, this.sort)
            .subscribe((data) => {
                console.log(data);
                if (data.length !== 0) {
                    this.comments = data;
                    this.page++;
                    this.canLoad = true;
                    console.log(this.comments);
                    if (data.length < this.size) {
                        this.canLoad = false;
                    }

                } else {
                    this.canLoad = false;
                }
                this.isLoading = false;
            },
            (error) => {
                console.log(error);
            });
    }

    handle_scroll_down() {
        if (this.canLoad) {
            this.canLoad = false;
            this.isLoading = true;
            setTimeout(() => {
                this.get_all_comments();
            }, 1000);
        }
    }

    open_delete_comment_dialog(id: number, ev: Event): void {
        const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
            width: '350px',
            data: 'Êtes-vous sûr de vouloir supprimer ce commentaire?',
            panelClass: ['dialog-style']
        });
        dialogRef.afterClosed().subscribe((result) => {
            if (result) {
                this.commentService.deleteComment(id).subscribe(
                    (_data) => {
                        window.location.reload();
                    },
                    (error) => {
                        return this.sharedService.showSnackbar(
                            'La demande a échoué avec le statut http ' + error.status,
                            'Dismiss',
                            5000
                        );
                    }
                );
            }
        });
    }
}
