import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Observable } from 'rxjs';
import { Favorite } from '../class/favorite';
import { Post } from '../class/post';
import { User } from '../class/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private API: String = 'http://localhost:8081/api/user';

  constructor(private http: HttpClient, private matSnackBar: MatSnackBar) { }

  public getUser(username: String): Observable<User>{
    return this.http.get<User>(`${this.API}/getuser/${username}`);
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

  public follow(username: String): Observable<any>{
    return this.http.post(this.API + '/follow', username);
  }

  public unfollow(username: String): Observable<any>{
    return this.http.post(this.API + '/unfollow', username);
  }

  public getFavoritePosts(): Observable<Favorite[]> {
    return this.http.get<Favorite[]>(`${this.API}/favorites/`);
  }

  public getUserPosts(username: String, page: Number, size: Number, sort: String): Observable<Post[]>{
    return this.http.get<Post[]>(`${this.API}/posts/${username}?page=${page}&size=${size}&sort=${sort}`);
  }
  
}
