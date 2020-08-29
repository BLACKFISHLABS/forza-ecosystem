import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Route } from '../model/route.model';
@Injectable({
  providedIn: 'root',
})
export class RouteService {

  private apiUrl: string;
  private apiUsername: string;
  private apiPassword: string;

  constructor(
    private http: HttpClient,
  ) {
    this.apiUsername = localStorage.getItem('username');
    this.apiPassword = localStorage.getItem('password');
    this.apiUrl = `${environment.apiUrl}` + '/route';
  }

  public create(route: Route): Observable<Route> {
    const headers = new HttpHeaders()
      .append('Authorization', 'Basic ' + btoa(this.apiUsername + ':' + this.apiPassword));
    return this.http.post<Route>(this.apiUrl, route, { headers });
  }

  public edit(route: Route): Observable<Route> {
    const headers = new HttpHeaders()
      .append('Authorization', 'Basic ' + btoa(this.apiUsername + ':' + this.apiPassword));
    return this.http.put<Route>(this.apiUrl, route, { headers });
  }

  public delete(id: any) {
    const headers = new HttpHeaders()
      .append('Authorization', 'Basic ' + btoa(this.apiUsername + ':' + this.apiPassword));
    return this.http.delete<Route>(this.apiUrl + '/' + id, { headers });
  }

  public findOne(id: any): Observable<Route> {
    const headers = new HttpHeaders()
      .append('Authorization', 'Basic ' + btoa(this.apiUsername + ':' + this.apiPassword));
    return this.http.get<Route>(this.apiUrl + '/' + id, { headers });
  }

  public search(cnpj: any, description: any): Observable<Route[]> {
    const params = { cnpj, description };
    const headers = new HttpHeaders()
      .append('Authorization', 'Basic ' + btoa(this.apiUsername + ':' + this.apiPassword));
    return this.http.get<Route[]>(this.apiUrl + '/search', { headers, params });
  }

  public searchRouteByFilter(cnpj: any, description: any, status: any): Observable<Route[]> {
    const params = { cnpj, description, status };
    const headers = new HttpHeaders()
      .append('Authorization', 'Basic ' + btoa(this.apiUsername + ':' + this.apiPassword));
    return this.http.get<Route[]>(this.apiUrl + '/search', { headers, params });
  }

}
