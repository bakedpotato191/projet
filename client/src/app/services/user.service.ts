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

  getUser(username: string): Observable<any> {
    return this.http.get<User>(`${this.API}/info/${username}`);
  }

  async follow(username: string) {
    return this.http.post(this.API + '/follow', username);
  }

 async unfollow(username: string) {
    return this.http.post(this.API + '/unfollow', username);
  }

  async getFavoritePosts(page: number, size:number, sort: string) {
    return this.http.get<Publication[]>(`${this.API}/favorites?page=${page}&size=${size}&sort=${sort}`);
  }

  async getUserPosts(username: string, page: number, size: number, sort: string) {
    return this.http.get<Publication[]>(`${this.API}/posts/${username}?page=${page}&size=${size}&sort=${sort}`);
  }

  async getProfilePicture() {
    return this.http.get<any>(`${this.API}/profile_picture`);
  }

  async setProfilePicture(form: FormData) {
    return this.http.post<any>(this.API + '/profile_picture', form);
  }

  async deleteProfilePicture() {
    return this.http.delete<any>(this.API + '/reset_profile_picture');
  }

  async getSubscriptions(username: string, page: number, size: number, sort: string) {
    return this.http.get<User[]>(`${this.API}/subscriptions/${username}?page=${page}&size=${size}&sort=${sort}`);
  }

   async getSubscribers(username: string, page: number, size: number, sort: string) {
    return this.http.get<User[]>(`${this.API}/subscribers/${username}?page=${page}&size=${size}&sort=${sort}`);
  }

  async getNewPublications(page: number, size: number) {
    return this.http.get<Publication[]>(`${this.API}/new?page=${page}&size=${size}`);
  }
}
