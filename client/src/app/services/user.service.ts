import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Post } from '../interfaces/post';
import { User } from '../interfaces/user';


@Injectable({
  providedIn: 'root'
})
export class UserService {
 
  private API: string = 'http://localhost:8081/api/user';

  constructor(private http: HttpClient) { }

  public getUser(username: string): Observable<User>{
    return this.http.get<User>(`${this.API}/info/${username}`);
  }

  public follow(username: string): Observable<any>{
    return this.http.post(this.API + '/follow', username);
  }

  public unfollow(username: string): Observable<any>{
    return this.http.post(this.API + '/unfollow', username);
  }

  public getFavoritePosts(page: number, size:number, sort: string): Observable<Post[]> {
    return this.http.get<Post[]>(`${this.API}/favorites?page=${page}&size=${size}&sort=${sort}`);
  }

  public getUserPosts(username: string, page: number, size: number, sort: string): Observable<Post[]>{
    return this.http.get<Post[]>(`${this.API}/posts/${username}?page=${page}&size=${size}&sort=${sort}`);
  }

  public getProfilePicture(): Observable<any> {
    return this.http.get(`${this.API}/profile_picture`);
  }

  public setProfilePicture(form: FormData): Observable<any>{
    return this.http.post(this.API + '/profile_picture', form);
  }

  public deleteProfilePicture(): Observable<any> {
    return this.http.delete(this.API + '/reset_profile_picture');
  }

  public getSubscriptions(username: string, page: number, size: number, sort: string): Observable<User[]> {
    return this.http.get<User[]>(`${this.API}/subscriptions/${username}?page=${page}&size=${size}&sort=${sort}`);
  }

  public getSubscribers(username: string, page: number, size: number, sort: string): Observable<User[]> {
    return this.http.get<User[]>(`${this.API}/subscribers/${username}?page=${page}&size=${size}&sort=${sort}`);
  }
}
