import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Publication } from '../interfaces/publication';
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

  public async follow(username: string) {
    return this.http.post(this.API + '/follow', username);
  }

  public async unfollow(username: string) {
    return this.http.post(this.API + '/unfollow', username);
  }

  public getFavoritePosts(page: number, size:number, sort: string): Observable<Publication[]> {
    return this.http.get<Publication[]>(`${this.API}/favorites?page=${page}&size=${size}&sort=${sort}`);
  }

  public getUserPosts(username: string, page: number, size: number, sort: string): Observable<Publication[]>{
    return this.http.get<Publication[]>(`${this.API}/posts/${username}?page=${page}&size=${size}&sort=${sort}`);
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

  public async getSubscriptions(username: string, page: number, size: number, sort: string) {
    return this.http.get<User[]>(`${this.API}/subscriptions/${username}?page=${page}&size=${size}&sort=${sort}`);
  }

  public getSubscribers(username: string, page: number, size: number, sort: string): Observable<User[]> {
    return this.http.get<User[]>(`${this.API}/subscribers/${username}?page=${page}&size=${size}&sort=${sort}`);
  }

  public getNewPublications(page: number, size: number): Observable<Publication[]>{
    return this.http.get<Publication[]>(`${this.API}/new?page=${page}&size=${size}`);
  }
}
