import { Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { Commentaire } from 'src/app/interfaces/commentaire';
import { CommentService } from 'src/app/services/comment.service';
import { SnackBarService } from 'src/app/services/snackbar.service';
import { TokenStorageService } from 'src/app/services/token-storage.service';
import { LoginComponent } from '../../login/login.component';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ConfirmationDialogComponent } from '../../userpage/dialogs/confirmation/confirmation-dialog.component';
import { lastValueFrom } from 'rxjs';
import { ContentComponent } from '../content/content.component';

@Component({
    selector: 'publication-comments',
    templateUrl: './comments.component.html',
    styleUrls: ['./comments.component.css']
})
export class CommentsComponent implements OnInit {
    @Input() param!: number;
    @Input() id!: number;
    @ViewChild('textarea') inputName!: ElementRef;

    current_id!: number;

    options: FormGroup;
    commentForm!: FormGroup;
    hideRequiredControl = new FormControl(false);
    floatLabelControl = new FormControl('auto');

    comments: Commentaire[] = [];
    comment!: Commentaire;
    loginRef!: MatDialogRef<LoginComponent>;

    private page: number = 0;
    private readonly size: number = 9;
    private readonly sort: string = 'date';
    canLoad: boolean = false;
    isLoading = false;

    constructor(
        private readonly commentService: CommentService,
        private readonly sbService: SnackBarService,
        private readonly tokenService: TokenStorageService,
        public dialog: MatDialog,
        private readonly parent: ContentComponent,
        private fb: FormBuilder
    ) {
        this.options = fb.group({
            hideRequired: this.hideRequiredControl,
            floatLabel: this.floatLabelControl,
          });
    }

    async ngOnInit(): Promise<void> {
        this.init_comment_form();

        if (this.id !== null) {
            this.current_id = this.id;
            await this.get_all_comments(this.id);
        }
        else if (this.param !== null){
            this.current_id = this.param;
            await this.get_all_comments(this.param);
        }
    }

    init_comment_form(): void {
        this.commentForm = this.fb.group({
            comment: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(128)]],
            post_id: [this.parent.post.id, [Validators.nullValidator]]
        });
    }

    async submit() {
        if (this.tokenService.getToken() == null) {
          this.dialog.open(LoginComponent);
          return;
        }

        if (this.commentForm.invalid){
            return;
        }

        this.isLoading = true;
        lastValueFrom(await this.commentService.submitComment(this.commentForm.value))
        .then(
          (_response) => 
            this.inputName.nativeElement.value = ''
        )
        .catch(
            (e) => console.error(e)
        )
        .finally(
            () => this.isLoading = false
        )
      }

    async get_all_comments(id: number): Promise<void> {
        lastValueFrom(await this.commentService
            .getPostComments(id, this.page, this.size, this.sort))
            .then(
                (data) => {
                    if (data.length !== 0) {
                        this.comments = this.comments.concat(data);
                        this.page++;
                        this.canLoad = true;
                        if (data.length < this.size) {
                            this.canLoad = false;
                        }

                    } else {
                        this.canLoad = false;
                    }
                this.isLoading = false;
                }
            )
            .catch(
                (e) => console.error(e)
            )
    }

    async handle_scroll_down() {
        if (this.canLoad) {
            this.canLoad = false;
            await this.get_all_comments(this.current_id);
        }
    }

    open_delete_comment_dialog(id: number, ev: Event): void {
        const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
            width: '350px',
            data: 'Êtes-vous sûr de vouloir supprimer ce commentaire?',
            panelClass: ['dialog-style']
        });
        dialogRef.afterClosed().subscribe(async (result) => {
            if (result) {
                lastValueFrom(await this.commentService.deleteComment(id))
                .then(
                    (_data) => window.location.reload() 
                )
                .catch(
                    (e) => {
                        console.error(e);
                        this.sbService.showSnackbar(
                            'La demande a échoué avec le statut http ' + e.status,
                            'Dismiss',
                            5000
                        );
                    }
                )
            }
        });
    }
}
