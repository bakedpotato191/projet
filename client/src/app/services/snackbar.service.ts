import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';

@Injectable({
  providedIn: 'root'
})
export class SnackBarService {

  constructor(private matSnackBar: MatSnackBar) { }

  showSnackbar(content: any, action: any, duration: number): void {
    let sb = this.matSnackBar.open(content, action, {
      duration,
      panelClass: ["custom-style"],
      verticalPosition: 'top', // Allowed values are  'top' | 'bottom'
      horizontalPosition: 'center', // Allowed values are 'start' | 'center' | 'end' | 'left' | 'right');
    });

    sb.onAction().subscribe(() => {
      sb.dismiss();
    });
  }
}
