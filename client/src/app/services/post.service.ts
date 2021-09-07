import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Commentaire } from '../interfaces/commentaire';


@Injectable({
  providedIn: 'root'
})
export class PostService {

  private API: string = 'http://localhost:8081/api/publication';

  constructor(private http: HttpClient) { }

  public postPhoto(form: FormData): Observable<any>{
    return this.http.post(this.API+ '/create', form);
  }

  public getPostById(id: number): Observable<any>{
    return this.http.get(`${this.API}/${id}`);
  }

  public submitComment(comment: Commentaire): Observable<any>{
    return this.http.post(this.API + '/addcomment', comment);
  }

  public likePost(id: number): Observable<any> {
    return this.http.post(this.API + '/like', id);
  }

  public dislikePost(id: number): Observable<any>{
    return this.http.post(this.API + '/dislike', id);
  }

  public removePost(id: number): Observable<any>{
    return this.http.delete(`${this.API}/delete/${id}`);
  }
}

