import { HttpClient, HttpHeaders } from '@angular/common/http';
import { analyzeAndValidateNgModules } from '@angular/compiler';
import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Observable } from 'rxjs';
import { Comment } from '../class/comment';
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

  constructor(private http: HttpClient, private tokenStorage: TokenStorageService) { }

  public getUser(username: String): Observable<any>{
    return this.http.get(`${this.baseUrl}/${username}`);
  }

  public postPhoto(form: FormData): Observable<any>{
    return this.http.post(this.baseUrl+ '/upload', form);
  }

  public getPostById(id: any): Observable<any>{
    return this.http.get(`${this.baseUrl}/p/${id}`);
  }

  public submitComment(comment: Comment): Observable<any>{
    return this.http.post(this.baseUrl + '/addcomment', comment);
  }
}
