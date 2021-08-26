import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-avatar',
  templateUrl: './avatar.component.html',
  styleUrls: ['./avatar.component.css']
})
export class AvatarComponent {

  constructor(public dialogRef: MatDialogRef<AvatarComponent>,
              @Inject(MAT_DIALOG_DATA) public input: any) { }

  closeDialog(): void {
    this.dialogRef.close();
  }

  addPhoto(): void {
    this.input.nativeElement.click();
  }

  resetPhoto(): void {
    this.dialogRef.close('delete');
  }

}
