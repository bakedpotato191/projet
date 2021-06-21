import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

const baseUrl = 'http://localhost:8081/api/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }

  public getUser(username: String): Observable<any>{
    return this.http.get(`${baseUrl}/${username}`);
  }

  public postPhoto(form: FormData): Observable<any>{
    return this.http.post(baseUrl+ '/upload', form);
  }
}
