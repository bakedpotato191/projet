import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

const AUTH_API = 'http://localhost:8081/api/auth/';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  constructor(private http: HttpClient) { }

  login(formData: FormData): Observable<any> {
    return this.http.post(AUTH_API + 'signin', formData);
  }

  register(formData: FormData): Observable<any> {
    return this.http.post(AUTH_API + 'signup', formData);
  }
}