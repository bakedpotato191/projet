import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Commentaire } from '../interfaces/commentaire';

@Injectable({
  providedIn: 'root'
})
export class CommentService {

  private API: string = 'http://localhost:8081/api/comment';

  constructor(private http: HttpClient) { }

  public getPostComments(id:number, page: number, size: number, sort: string): Observable<any>{
    return this.http.get<any>(`${this.API}/all/${id}?page=${page}&size=${size}&sort=${sort}`);
  }

  public submitComment(comment: Commentaire): Observable<any>{
    return this.http.post(this.API + '/add', comment);
  }

  public deleteComment(id: number):Observable<any>{
    return this.http.delete(`${this.API}/delete/${id}`);
  }
}
