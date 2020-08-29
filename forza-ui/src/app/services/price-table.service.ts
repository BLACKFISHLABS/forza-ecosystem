import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { PriceTable } from '../model/price-table.model';

@Injectable({
  providedIn: 'root',
})
export class PriceTableService {

  private apiUrl: string;
  private apiUsername: string;
  private apiPassword: string;

  constructor(
    private http: HttpClient,
  ) {
    this.apiUsername = localStorage.getItem('username');
    this.apiPassword = localStorage.getItem('password');
    this.apiUrl = `${environment.apiUrl}` + '/pricetable';
  }

  public getAll(cnpj: any): Observable<PriceTable[]> {
    const params = { cnpj };
    const headers = new HttpHeaders()
      .append('Authorization', 'Basic ' + btoa(this.apiUsername + ':' + this.apiPassword));
    return this.http.get<PriceTable[]>(this.apiUrl, { headers, params });
  }

  public create(priceTable: PriceTable): Observable<PriceTable> {
    const headers = new HttpHeaders()
      .append('Authorization', 'Basic ' + btoa(this.apiUsername + ':' + this.apiPassword));
    return this.http.post<PriceTable>(this.apiUrl, priceTable, { headers });
  }

  public edit(priceTable: PriceTable): Observable<PriceTable> {
    const headers = new HttpHeaders()
      .append('Authorization', 'Basic ' + btoa(this.apiUsername + ':' + this.apiPassword));
    return this.http.put<PriceTable>(this.apiUrl, priceTable, { headers });
  }

  public delete(id: any) {
    const headers = new HttpHeaders()
      .append('Authorization', 'Basic ' + btoa(this.apiUsername + ':' + this.apiPassword));
    return this.http.delete<PriceTable>(this.apiUrl + '/' + id, { headers });
  }

  public findOne(id: any): Observable<PriceTable> {
    const headers = new HttpHeaders()
      .append('Authorization', 'Basic ' + btoa(this.apiUsername + ':' + this.apiPassword));
    return this.http.get<PriceTable>(this.apiUrl + '/' + id, { headers });
  }

  public getPriceTableMobile(cnpj: any, code: any): Observable<PriceTable> {
    const params = { cnpj, code };
    const headers = new HttpHeaders()
      .append('Authorization', 'Basic ' + btoa(this.apiUsername + ':' + this.apiPassword));
    return this.http.get<PriceTable>(this.apiUrl + '/mobile', { headers, params });
  }

  public search(cnpj: any, description: any): Observable<PriceTable[]> {
    const params = { cnpj, description };
    const headers = new HttpHeaders()
      .append('Authorization', 'Basic ' + btoa(this.apiUsername + ':' + this.apiPassword));
    return this.http.get<PriceTable[]>(this.apiUrl + '/search', { headers, params });
  }

  public getPriceTablesUpdates(cnpj: any, ultimaAtualizacao: any): Observable<PriceTable> {
    const params = { cnpj, ultimaAtualizacao };
    const headers = new HttpHeaders()
      .append('Authorization', 'Basic ' + btoa(this.apiUsername + ':' + this.apiPassword));
    return this.http.get<PriceTable>(this.apiUrl + '/update', { headers, params });
  }

}
