import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Commentaire } from '../interfaces/commentaire';
import { Publication } from '../interfaces/publication';


@Injectable({
  providedIn: 'root'
})
export class PostService {

  private API: string = 'http://localhost:8081/api/publication';

  constructor(private http: HttpClient) { }

  async postPhoto(form: FormData) {
    return this.http.post(this.API+ '/create', form);
  }

   getPostById(id: number): Observable<Publication> {
    return this.http.get<Publication>(`${this.API}/${id}`);
  }

  async likePost(id: number) {
    return this.http.post<any>(this.API + '/like', id);
  }

  async dislikePost(id: number) {
    return this.http.post<any>(this.API + '/dislike', id);
  }

  async submitComment(comment: Commentaire) {
    return this.http.post(this.API + '/addcomment', comment);
  }

  async removePost(id: number) {
    return this.http.delete(`${this.API}/delete/${id}`);
  }
}

