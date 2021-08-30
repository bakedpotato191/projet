import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private AUTH_API: string = 'http://localhost:8081/api/auth/';

  constructor(private http: HttpClient) { }

  login(formData: FormData): Observable<any> {
    return this.http.post(this.AUTH_API + 'signin', formData);
  }

  register(formData: FormData): Observable<any> {
    return this.http.post(this.AUTH_API + 'signup', formData);
  }

  restore(formData: FormData): Observable<any> {
    return this.http.post(this.AUTH_API + 'restore', formData);
  }

  verifyToken(json: any): Observable<any> {
    return this.http.post(this.AUTH_API + 'verify_token', json);
  }

  reset(formData: FormData): Observable<any> {
    return this.http.post(this.AUTH_API + 'reset_password', formData);
  }

}