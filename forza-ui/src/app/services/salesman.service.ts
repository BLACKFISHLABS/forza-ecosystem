import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { SalesMan } from '../model/salesman.model';

@Injectable({
  providedIn: 'root',
})
export class SalesManService {

  private apiUrl: string;
  private apiUsername: string;
  private apiPassword: string;

  constructor(
    private http: HttpClient,
  ) {
    this.apiUsername = localStorage.getItem('username');
    this.apiPassword = localStorage.getItem('password');
    this.apiUrl = `${environment.apiUrl}` + '/salesman';
  }

  public create(salesMan: SalesMan): Observable<SalesMan> {
    const headers = new HttpHeaders()
      .append('Authorization', 'Basic ' + btoa(this.apiUsername + ':' + this.apiPassword));
    return this.http.post<SalesMan>(this.apiUrl, salesMan, { headers });
  }

  public edit(salesMan: SalesMan): Observable<SalesMan> {
    const headers = new HttpHeaders()
      .append('Authorization', 'Basic ' + btoa(this.apiUsername + ':' + this.apiPassword));
    return this.http.put<SalesMan>(this.apiUrl, salesMan, { headers });
  }

  public delete(id: any) {
    const headers = new HttpHeaders()
      .append('Authorization', 'Basic ' + btoa(this.apiUsername + ':' + this.apiPassword));
    return this.http.delete<SalesMan>(this.apiUrl + '/' + id, { headers });
  }

  public findOne(id: any): Observable<SalesMan> {
    const headers = new HttpHeaders()
      .append('Authorization', 'Basic ' + btoa(this.apiUsername + ':' + this.apiPassword));
    return this.http.get<SalesMan>(this.apiUrl + '/' + id, { headers });
  }

  public search(cnpj: any): Observable<SalesMan[]> {
    const params = { cnpj };
    const headers = new HttpHeaders()
      .append('Authorization', 'Basic ' + btoa(this.apiUsername + ':' + this.apiPassword));
    return this.http.get<SalesMan[]>(this.apiUrl + '/search', { headers, params });
  }

}
