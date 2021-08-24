import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { PageEvent } from '@angular/material/paginator';
import { Commentaire } from 'src/app/class/commentaire';
import { CommentService } from 'src/app/services/comment.service';
import { SharedService } from 'src/app/services/shared.service';
import { PostComponent } from '../post/post.component';
import { ConfirmationDialogComponent } from '../dialogs/confirmation-dialog/confirmation-dialog.component';

@Component({
  selector: 'app-comments',
  templateUrl: './comments.component.html',
  styleUrls: ['./comments.component.css']
})
export class CommentsComponent implements OnInit {

  comments!: Commentaire[];

  // MatPaginator page variables
  private pageNumber: number = 0;
  private readonly sort: string = "date";
  length = 100;
  pageSize = 10;
  pageSizeOptions: number[] = [5, 10, 25];

  // MatPaginator get request
  isLoading = false;
  canLoad = false;

  // MatPaginator Output
  pageEvent!: PageEvent;

  constructor(private readonly commentService: CommentService,
              private sharedService: SharedService,
              public dialog: MatDialog,
              private readonly parent: PostComponent) { }

  ngOnInit(): void {
    this.pageNumber = 0;
    this.comments = [];
    this.getAllComments();
  }

  getAllComments() {
    this.commentService.getPostComments(this.parent.id, this.pageNumber, this.pageSize, this.sort).subscribe(data => {
      if (data.length !== 0) {
        this.comments = data.comments;
        this.length = data.total_elements;
        this.canLoad = true;

        if (data.length < this.pageSize) {
          this.canLoad = false;
        }
      }
      else {
        this.canLoad = false;
      }
      this.isLoading = false;
    });
  }

  handlePageEvent(ev: PageEvent) {
    this.pageNumber = ev.pageIndex;
    this.pageSize = ev.pageSize;
    this.canLoad = false;
    this.isLoading = true;
      setTimeout(() => {
        this.getAllComments();
      }, 1000);
  }

  openDeleteCommentDialog(id: Number): void {
    const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
      width: '350px',
      data: "Êtes-vous sûr de vouloir supprimer ce commentaire?",
      panelClass: ["dialog-style"]
    });
    dialogRef.afterClosed().subscribe(result => {
      if(result) {
        this.commentService.deleteComment(id).subscribe(
          _data => {
            this.ngOnInit();
          },
          _error => {
            return this.sharedService.showSnackbar("Request failed with status code" + _error.status, 'Dismiss', 5000);
          }
        )
      }
    });
  }

}
