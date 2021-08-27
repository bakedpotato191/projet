import { Component, Inject } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { PostService } from 'src/app/services/post.service';
import { SharedService } from 'src/app/services/shared.service';
import { UserpostsComponent } from '../userposts/userposts.component';


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
    @Inject(MAT_DIALOG_DATA) public userposts: UserpostsComponent,
    private postService: PostService,
    private sharedService: SharedService) { }

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
    this.dialogRef.close('uploaded');
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
          this.closeDialog();
      },
      error => {
        this.isSending = false;
        this.sharedService.showSnackbar(JSON.stringify("POST request failed with status code " + error.status), 'Dismiss', 7000);
      }
    );
  }
}
