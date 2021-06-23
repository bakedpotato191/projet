
import { Injectable } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { UploadComponent } from '../components/upload/upload.component';

@Injectable({
  providedIn: 'root'
})
export class UploadformService {
  
  constructor(private dialog: MatDialog) { }

  dialogRef!: MatDialogRef<UploadComponent>;
  
  public open() {
    this.dialogRef = this.dialog.open(UploadComponent);
  }
}
