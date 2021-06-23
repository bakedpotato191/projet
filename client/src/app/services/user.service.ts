import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Observable } from 'rxjs';
import { Commentaire } from '../class/commentaire';
import { TokenStorageService } from './token-storage.service';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  baseUrl: String = 'http://localhost:8081/api/user';
  isLoggedIn = false;
  isLoginFailed = false;
  errorMessage = '';
  roles: string[] = [];

  constructor(private http: HttpClient, private tokenStorage: TokenStorageService, private matSnackBar: MatSnackBar) { }

  public getUser(username: String): Observable<any>{
    return this.http.get(`${this.baseUrl}/${username}`);
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
