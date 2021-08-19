import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Commentaire } from '../class/commentaire';

@Injectable({
  providedIn: 'root'
})
export class CommentService {

  private API: String = 'http://localhost:8081/api/comment';

  constructor(private http: HttpClient) { }

  public getPostComments(id:Number, page: Number, size: Number, sort: String): Observable<Commentaire[]>{
    return this.http.get<Commentaire[]>(`${this.API}/all/${id}?page=${page}&size=${size}&sort=${sort}`);
  }

  public submitComment(comment: Commentaire): Observable<any>{
    return this.http.post(this.API + '/add', comment);
  }

  public deleteComment(id: Number):Observable<any>{
    return this.http.delete(`${this.API}/delete/${id}`);
  }
}
