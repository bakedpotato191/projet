import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Title } from '@angular/platform-browser';
@Injectable({
  providedIn: 'root'
})
export class SharedService {

  constructor(private matSnackBar: MatSnackBar,
              private titleService: Title) { }

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

  async setTitle(newTitle: string) {
    this.titleService.setTitle(newTitle);
  }
}
