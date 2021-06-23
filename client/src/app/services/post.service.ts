import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Commentaire } from '../class/commentaire';

@Injectable({
  providedIn: 'root'
})
export class PostService {

  baseUrl: String = 'http://localhost:8081/api/post';

  constructor(private http: HttpClient) { }

  public postPhoto(form: FormData): Observable<any>{
    return this.http.post(this.baseUrl+ '/create', form);
  }

  public getPostById(id: Number): Observable<any>{
    return this.http.get(`${this.baseUrl}/${id}`);
  }

  public submitComment(comment: Commentaire): Observable<any>{
    return this.http.post(this.baseUrl + '/addcomment', comment);
  }

  public likePost(id: Number): Observable<any>{
    return this.http.post(this.baseUrl + '/like', id);
  }
}
