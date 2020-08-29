import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { City } from '../model/city.model';

@Injectable({
  providedIn: 'root',
})
export class CityService {

  private apiUrl: string;
  private apiUsername: string;
  private apiPassword: string;

  constructor(
    private http: HttpClient,
  ) {
    this.apiUsername = localStorage.getItem('username');
    this.apiPassword = localStorage.getItem('password');
    this.apiUrl = `${environment.apiUrl}` + '/city';
  }

  public getAll(): Observable<City[]> {
    const headers = new HttpHeaders()
      .append('Authorization', 'Basic ' + btoa(this.apiUsername + ':' + this.apiPassword));
    return this.http.get<City[]>(this.apiUrl, { headers });
  }

  public search(description: any, status: any): Observable<City> {
    const params = { description, status };
    const headers = new HttpHeaders()
      .append('Authorization', 'Basic ' + btoa(this.apiUsername + ':' + this.apiPassword));
    return this.http.get<City>(this.apiUrl + '/search', { headers, params });
  }

}
