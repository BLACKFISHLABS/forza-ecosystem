import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { User } from '../model/user.model';

@Injectable({
  providedIn: 'root',
})
export class UserService {

  private apiUrl: string;
  private apiUsername: string;
  private apiPassword: string;

  constructor(
    private http: HttpClient,
  ) {
    this.apiUsername = localStorage.getItem('username');
    this.apiPassword = localStorage.getItem('password');
    this.apiUrl = `${environment.apiUrl}` + '/user';
  }

  public create(user: User): Observable<User> {
    const headers = new HttpHeaders()
      .append('Authorization', 'Basic ' + btoa(this.apiUsername + ':' + this.apiPassword));
    return this.http.post<User>(this.apiUrl, user, {});
  }

  public edit(user: User): Observable<User> {
    const headers = new HttpHeaders()
      .append('Authorization', 'Basic ' + btoa(this.apiUsername + ':' + this.apiPassword));
    return this.http.put<User>(this.apiUrl, user, {});
  }

  public delete(user: User): Observable<User> {
    const headers = new HttpHeaders()
      .append('Authorization', 'Basic ' + btoa(this.apiUsername + ':' + this.apiPassword));
    const options = {
      headers,
      body: user,
    };
    return this.http.delete<User>(this.apiUrl, options);
  }

  public findOne(id: any): Observable<User> {
    const headers = new HttpHeaders()
      .append('Authorization', 'Basic ' + btoa(this.apiUsername + ':' + this.apiPassword));
    return this.http.get<User>(this.apiUrl + '/' + id, {});
  }

  public search(cnpj: any, description: any): Observable<User[]> {
    const params = { cnpj, description };
    const headers = new HttpHeaders()
      .append('Authorization', 'Basic ' + btoa(this.apiUsername + ':' + this.apiPassword));
    return this.http.get<User[]>(this.apiUrl + '/search', { params });
  }

  public getUsers(cnpj: any, ultimaAtualizacao: any): Observable<User[]> {
    const params = { cnpj, ultimaAtualizacao };
    const headers = new HttpHeaders()
      .append('Authorization', 'Basic ' + btoa(this.apiUsername + ':' + this.apiPassword));
    return this.http.get<User[]>(this.apiUrl, { params });
  }

}
