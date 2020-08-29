import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Charge } from '../model/charge.model';

@Injectable({
  providedIn: 'root',
})
export class ChargeService {

  private apiUrl: string;
  private apiUsername: string;
  private apiPassword: string;

  constructor(
    private http: HttpClient,
  ) {
    this.apiUsername = localStorage.getItem('username');
    this.apiPassword = localStorage.getItem('password');
    this.apiUrl = `${environment.apiUrl}` + '/charge';
  }

  public create(charge: Charge): Observable<Charge> {
    const headers = new HttpHeaders()
      .append('Authorization', 'Basic ' + btoa(this.apiUsername + ':' + this.apiPassword));
    return this.http.post<Charge>(this.apiUrl, charge, { headers });
  }

  public edit(charge: Charge): Observable<Charge> {
    const headers = new HttpHeaders()
      .append('Authorization', 'Basic ' + btoa(this.apiUsername + ':' + this.apiPassword));
    return this.http.put<Charge>(this.apiUrl, charge, { headers });
  }

  public delete(id: any) {
    const headers = new HttpHeaders()
      .append('Authorization', 'Basic ' + btoa(this.apiUsername + ':' + this.apiPassword));
    return this.http.delete<Charge>(this.apiUrl + '/' + id, { headers });
  }

  public findOne(id: any): Observable<Charge> {
    const headers = new HttpHeaders()
      .append('Authorization', 'Basic ' + btoa(this.apiUsername + ':' + this.apiPassword));
    return this.http.get<Charge>(this.apiUrl + '/' + id, { headers });
  }

  public getChargeByChargeMobile(cnpj: any, code: any): Observable<Charge> {
    const params = { cnpj, code };
    const headers = new HttpHeaders()
      .append('Authorization', 'Basic ' + btoa(this.apiUsername + ':' + this.apiPassword));
    return this.http.get<Charge>(this.apiUrl + '/mobile', { headers, params });
  }

  public search(cnpj: any, description: any): Observable<Charge[]> {
    const params = { cnpj, description };
    const headers = new HttpHeaders()
      .append('Authorization', 'Basic ' + btoa(this.apiUsername + ':' + this.apiPassword));
    return this.http.get<Charge[]>(this.apiUrl + '/search', { headers, params });
  }

  public searchChargeByFilter(cnpj: any, description: any): Observable<Charge[]> {
    const params = { cnpj, description };
    const headers = new HttpHeaders()
      .append('Authorization', 'Basic ' + btoa(this.apiUsername + ':' + this.apiPassword));
    return this.http.get<Charge[]>(this.apiUrl + '/search/filter', { headers, params });
  }

}
