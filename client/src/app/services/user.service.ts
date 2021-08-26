import { HttpClient, HttpStatusCode } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Favorite } from '../class/favorite';
import { Post } from '../class/post';
import { User } from '../class/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private API: String = 'http://localhost:8081/api/user';

  constructor(private http: HttpClient) { }

  public getUser(username: String): Observable<User>{
    return this.http.get<User>(`${this.API}/info/${username}`);
  }

  public follow(username: String): Observable<any>{
    return this.http.post(this.API + '/follow', username);
  }

  public unfollow(username: String): Observable<any>{
    return this.http.post(this.API + '/unfollow', username);
  }

  public getFavoritePosts(page: Number, size:Number, sort: String): Observable<Favorite[]> {
    return this.http.get<Favorite[]>(`${this.API}/favorites?page=${page}&size=${size}&sort=${sort}`);
  }

  public getUserPosts(username: String, page: Number, size: Number, sort: String): Observable<Post[]>{
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

  public getSubscriptions(username: String, page: Number, size: Number, sort: String): Observable<any> {
    return this.http.get(`${this.API}/subscriptions/${username}?page=${page}&size=${size}&sort=${sort}`);
  }
}
