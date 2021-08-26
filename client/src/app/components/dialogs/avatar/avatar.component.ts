import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-avatar',
  templateUrl: './avatar.component.html',
  styleUrls: ['./avatar.component.css']
})
export class AvatarComponent implements OnInit {

  constructor(public dialogRef: MatDialogRef<AvatarComponent>,
              @Inject(MAT_DIALOG_DATA) public input: any) { }

  ngOnInit(): void {
  }

  closeDialog(){
    this.dialogRef.close();
  }

  addPhoto() {
    this.input.nativeElement.click();
  }

  resetPhoto() {
    this.dialogRef.close('delete');
  }

}
