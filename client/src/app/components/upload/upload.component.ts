import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { PostService } from 'src/app/services/post.service';

@Component({
  selector: 'app-upload',
  templateUrl: './upload.component.html',
  styleUrls: ['./upload.component.css']
})
export class UploadComponent {

  imgFile!: String;
  fileData!: Blob;
  isSending!: boolean;

  uploadForm = new FormGroup({
    file: new FormControl('', [Validators.required]),
    description: new FormControl('', [Validators.nullValidator])
  });

  constructor(
    public dialogRef: MatDialogRef<UploadComponent>,
    private postService: PostService,
    private matSnackBar: MatSnackBar) { }

  get uf() {
    return this.uploadForm.controls;
  }

  onImageChange(e: any) {
    const reader = new FileReader();

    if (e.target.files && e.target.files.length) {
      this.fileData = e.target.files[0];
      reader.readAsDataURL(this.fileData);

      reader.onload = () => {
        this.imgFile = reader.result as string;
        this.uploadForm.patchValue({
          imgSrc: reader.result
        });

      };
    }
  }

  closeDialog() {
    this.dialogRef.close();
  }

  submit() {
    if (this.uploadForm.invalid) {
      return;
    }
    var formData: any = new FormData();
    formData.append("photo", this.fileData);
    formData.append("description", this.uploadForm.get('description')?.value);
    this.isSending = true;
    this.postService.postPhoto(formData).subscribe(
      _data => {
        this.reloadPage()
      },
      error => {
        this.isSending = false;
        this.showSnackbar(JSON.stringify(error.error.apierror.message), 'Dismiss', 7000);
      }
    );
  }

  reloadPage(): void {
    window.location.reload();
  }

  showSnackbar(content: any, action: any, duration: number) {
    this.matSnackBar.open(content, action, {
      duration,
      panelClass: "custom-modalbox",
      verticalPosition: 'top', // Allowed values are  'top' | 'bottom'
      horizontalPosition: 'center', // Allowed values are 'start' | 'center' | 'end' | 'left' | 'right');
    });
  }

}
