import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Observable, throwError } from 'rxjs';
import { User } from '../class/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private API: String = 'http://localhost:8081/api/user';

  constructor(private http: HttpClient, private matSnackBar: MatSnackBar) { }

  public getUser(username: String): Observable<User>{
    return this.http.get<User>(`${this.API}/${username}`);
  }

  public reloadPage(){
    window.location.reload();
  }

  public showSnackbar(content: any, action: any, duration: number) {
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
