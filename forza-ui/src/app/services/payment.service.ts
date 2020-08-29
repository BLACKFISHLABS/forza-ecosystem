import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Payment } from '../model/payment.model';

@Injectable({
  providedIn: 'root',
})
export class PaymentService {

  private apiUrl: string;
  private apiUsername: string;
  private apiPassword: string;

  constructor(
    private http: HttpClient,
  ) {
    this.apiUsername = localStorage.getItem('username');
    this.apiPassword = localStorage.getItem('password');
    this.apiUrl = `${environment.apiUrl}` + '/payment';
  }

  public getPaymentByCNPJ(cnpj: any): Observable<Payment[]> {
    const params = { cnpj };
    const headers = new HttpHeaders()
      .append('Authorization', 'Basic ' + btoa(this.apiUsername + ':' + this.apiPassword));
    return this.http.get<Payment[]>(this.apiUrl, { headers, params });
  }

  public create(payment: Payment): Observable<Payment> {
    const headers = new HttpHeaders()
      .append('Authorization', 'Basic ' + btoa(this.apiUsername + ':' + this.apiPassword));
    return this.http.post<Payment>(this.apiUrl, payment, { headers });
  }

  public edit(payment: Payment): Observable<Payment> {
    const headers = new HttpHeaders()
      .append('Authorization', 'Basic ' + btoa(this.apiUsername + ':' + this.apiPassword));
    return this.http.put<Payment>(this.apiUrl, payment, { headers });
  }

  public delete(id: any) {
    const headers = new HttpHeaders()
      .append('Authorization', 'Basic ' + btoa(this.apiUsername + ':' + this.apiPassword));
    return this.http.delete<Payment>(this.apiUrl + '/' + id, { headers });
  }

  public findOne(id: any): Observable<Payment> {
    const headers = new HttpHeaders()
      .append('Authorization', 'Basic ' + btoa(this.apiUsername + ':' + this.apiPassword));
    return this.http.get<Payment>(this.apiUrl + '/' + id, { headers });
  }

  public getPaymentByLastUpdate(cnpj: any, ultimaAtualizacao: any): Observable<Payment> {
    const params = { cnpj, ultimaAtualizacao };
    const headers = new HttpHeaders()
      .append('Authorization', 'Basic ' + btoa(this.apiUsername + ':' + this.apiPassword));
    return this.http.get<Payment>(this.apiUrl + '/update', { headers, params });
  }

}
