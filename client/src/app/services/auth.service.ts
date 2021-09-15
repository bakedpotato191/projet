import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private AUTH_API: string = 'http://localhost:8081/api/auth/';

  constructor(private http: HttpClient) { }

  async login(formData: FormData) {
    return this.http.post(this.AUTH_API + 'signin', formData);
  }

  async register(formData: FormData) {
    return this.http.post(this.AUTH_API + 'signup', formData);
  }

  async restore(formData: FormData) {
    return this.http.post(this.AUTH_API + 'restore', formData);
  }

  async verifyToken(json: any) {
    return this.http.post(this.AUTH_API + 'verify_token', json);
  }

  async reset(formData: FormData) {
    return this.http.post(this.AUTH_API + 'reset_password', formData);
  }

}