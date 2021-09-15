import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Commentaire } from '../interfaces/commentaire';


@Injectable({
  providedIn: 'root'
})
export class PostService {

  private API: string = 'http://localhost:8081/api/publication';

  constructor(private http: HttpClient) { }

  async postPhoto(form: FormData) {
    return this.http.post(this.API+ '/create', form);
  }

  async getPostById(id: number) {
    return this.http.get<any>(`${this.API}/${id}`);
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

