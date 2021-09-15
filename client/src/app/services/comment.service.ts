import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Commentaire } from '../interfaces/commentaire';

@Injectable({
  providedIn: 'root'
})
export class CommentService {

  private API: string = 'http://localhost:8081/api/comment';

  constructor(private http: HttpClient) { }

  async getPostComments(id:number, page: number, size: number, sort: string) {
    return this.http.get<any>(`${this.API}/all/${id}?page=${page}&size=${size}&sort=${sort}`);
  }

  async submitComment(comment: Commentaire) {
    return this.http.post(this.API + '/add', comment);
  }

  async deleteComment(id: number) {
    return this.http.delete(`${this.API}/delete/${id}`);
  }
}
