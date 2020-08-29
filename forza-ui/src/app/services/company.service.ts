import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Company } from '../model/company.model';

@Injectable({
  providedIn: 'root',
})
export class CompanyService {

  private apiUrl: string;
  private apiUsername: string;
  private apiPassword: string;

  constructor(
    private http: HttpClient,
  ) {
    this.apiUsername = localStorage.getItem('username');
    this.apiPassword = localStorage.getItem('password');
    this.apiUrl = `${environment.apiUrl}` + '/company';
  }

  public create(company: Company): Observable<Company> {
    const headers = new HttpHeaders()
      .append('Authorization', 'Basic ' + btoa(this.apiUsername + ':' + this.apiPassword));
    return this.http.post<Company>(this.apiUrl, company, {});
  }

  public edit(company: Company): Observable<Company> {
    const headers = new HttpHeaders()
      .append('Authorization', 'Basic ' + btoa(this.apiUsername + ':' + this.apiPassword));
    return this.http.put<Company>(this.apiUrl, company, { headers });
  }

  public findOne(id: any): Observable<Company> {
    const headers = new HttpHeaders()
      .append('Authorization', 'Basic ' + btoa(this.apiUsername + ':' + this.apiPassword));
    return this.http.get<Company>(this.apiUrl + '/' + id, {});
  }

  public getSearch(cnpj: any): Observable<Company[]> {
    const params = { cnpj };
    const headers = new HttpHeaders()
      .append('Authorization', 'Basic ' + btoa(this.apiUsername + ':' + this.apiPassword));
    return this.http.get<Company[]>(this.apiUrl, { params });
  }

  public getAll(): Observable<Company[]> {
    const headers = new HttpHeaders()
      .append('Authorization', 'Basic ' + btoa(this.apiUsername + ':' + this.apiPassword));
    return this.http.get<Company[]>(this.apiUrl, {});
  }
}
