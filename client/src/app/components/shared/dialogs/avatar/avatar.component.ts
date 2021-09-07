import { Component, ElementRef, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
    selector: 'app-avatar',
    templateUrl: './avatar.component.html',
    styleUrls: ['./avatar.component.css']
})
export class AvatarComponent {
    constructor(
        public dialogRef: MatDialogRef<AvatarComponent>,
        @Inject(MAT_DIALOG_DATA) public input: ElementRef
    ) {}

    close_dialog(): void {
        this.dialogRef.close();
    }

    import_avatar(): void {
        this.input.nativeElement.click();
    }

    reset_avatar(): void {
        this.dialogRef.close('delete');
    }
}
