import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Commentaire } from 'src/app/class/commentaire';
import { CommentService } from 'src/app/services/comment.service';
import { SharedService } from 'src/app/services/shared.service';
import { PostComponent } from '../post/post.component';
import { ConfirmationDialogComponent } from '../shared/confirmation-dialog/confirmation-dialog.component';

@Component({
  selector: 'app-comments',
  templateUrl: './comments.component.html',
  styleUrls: ['./comments.component.css']
})
export class CommentsComponent implements OnInit {

  comments!: Commentaire[];
  private page: number = 0;
  private readonly size: number = 10;
  private readonly sort: string = "date";
  isLoading = false;
  canLoad = false;

  constructor(private readonly commentService: CommentService,
              private sharedService: SharedService,
              public dialog: MatDialog,
              private readonly parent: PostComponent) { }

  ngOnInit(): void {
    this.page = 0;
    this.comments = [];
    this.getAllComments();
  }

  getAllComments() {
    this.commentService.getPostComments(this.parent.id, this.page, this.size, this.sort).subscribe(data => {
      if (data.length !== 0) {
        this.comments = this.comments.concat(data);
        this.page++;
        this.canLoad = true;

        if (data.length < this.size) {
          this.canLoad = false;
        }
      }
      else {
        this.canLoad = false;
      }
      this.isLoading = false;
    });
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
            window.location.reload();
          },
          _error => {
            return this.sharedService.showSnackbar("Error occured", 'Dismiss', 5000);
          }
        )
      }
    });
  }

}
