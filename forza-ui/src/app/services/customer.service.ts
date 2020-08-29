import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Customer } from '../model/customer.model';

@Injectable({
  providedIn: 'root',
})
export class CustomerService {

  private apiUrl: string;
  private apiUsername: string;
  private apiPassword: string;

  constructor(
    private http: HttpClient,
  ) {
    this.apiUsername = localStorage.getItem('username');
    this.apiPassword = localStorage.getItem('password');
    this.apiUrl = `${environment.apiUrl}` + '/customer';
  }

  public getCustomerByCNPJ(cnpj: any): Observable<Customer[]> {
    const params = { cnpj };
    const headers = new HttpHeaders()
      .append('Authorization', 'Basic ' + btoa(this.apiUsername + ':' + this.apiPassword));
    return this.http.get<Customer[]>(this.apiUrl + '/search', { headers, params });
  }

  public create(customer: Customer): Observable<Customer> {
    const headers = new HttpHeaders()
      .append('Authorization', 'Basic ' + btoa(this.apiUsername + ':' + this.apiPassword));
    return this.http.post<Customer>(this.apiUrl, customer, { headers });
  }

  public edit(customer: Customer): Observable<Customer> {
    const headers = new HttpHeaders()
      .append('Authorization', 'Basic ' + btoa(this.apiUsername + ':' + this.apiPassword));
    return this.http.put<Customer>(this.apiUrl, customer, { headers });
  }

  public delete(id: any) {
    const headers = new HttpHeaders()
      .append('Authorization', 'Basic ' + btoa(this.apiUsername + ':' + this.apiPassword));
    return this.http.delete<Customer>(this.apiUrl + '/' + id, { headers });
  }

  public findOne(id: any): Observable<Customer> {
    const headers = new HttpHeaders()
      .append('Authorization', 'Basic ' + btoa(this.apiUsername + ':' + this.apiPassword));
    return this.http.get<Customer>(this.apiUrl + '/' + id, { headers });
  }

  public getCustomerByChargeMobile(cnpj: any, code: any): Observable<Customer> {
    const params = { cnpj, code };
    const headers = new HttpHeaders()
      .append('Authorization', 'Basic ' + btoa(this.apiUsername + ':' + this.apiPassword));
    return this.http.get<Customer>(this.apiUrl + '/mobile', { headers, params });
  }

  public search(cnpj: any, description: any): Observable<Customer[]> {
    const params = { cnpj, description };
    const headers = new HttpHeaders()
      .append('Authorization', 'Basic ' + btoa(this.apiUsername + ':' + this.apiPassword));
    return this.http.get<Customer[]>(this.apiUrl + '/search', { headers, params });
  }

  public searchCustomerByFilter(cnpj: any, description: any, status: any): Observable<Customer[]> {
    const params = { cnpj, description, status };
    const headers = new HttpHeaders()
      .append('Authorization', 'Basic ' + btoa(this.apiUsername + ':' + this.apiPassword));
    return this.http.get<Customer[]>(this.apiUrl + '/search/filter', { headers, params });
  }

  public getCustomersUpdates(cnpj: any, ultimaAtualizacao: any): Observable<Customer> {
    const params = { cnpj, ultimaAtualizacao };
    const headers = new HttpHeaders()
      .append('Authorization', 'Basic ' + btoa(this.apiUsername + ':' + this.apiPassword));
    return this.http.get<Customer>(this.apiUrl + '/update', { headers, params });
  }

}
