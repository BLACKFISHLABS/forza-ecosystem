import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Vehicle } from '../model/vehicle.model';

@Injectable({
  providedIn: 'root',
})
export class VehicleService {

  private apiUrl: string;
  private apiUsername: string;
  private apiPassword: string;

  constructor(
    private http: HttpClient,
  ) {
    this.apiUsername = localStorage.getItem('username');
    this.apiPassword = localStorage.getItem('password');
    this.apiUrl = `${environment.apiUrl}` + '/vehicle';
  }

  public create(vehicle: Vehicle): Observable<Vehicle> {
    const headers = new HttpHeaders()
      .append('Authorization', 'Basic ' + btoa(this.apiUsername + ':' + this.apiPassword));
    return this.http.post<Vehicle>(this.apiUrl, vehicle, { headers });
  }

  public edit(vehicle: Vehicle): Observable<Vehicle> {
    const headers = new HttpHeaders()
      .append('Authorization', 'Basic ' + btoa(this.apiUsername + ':' + this.apiPassword));
    return this.http.put<Vehicle>(this.apiUrl, vehicle, { headers });
  }

  public delete(id: any) {
    const headers = new HttpHeaders()
      .append('Authorization', 'Basic ' + btoa(this.apiUsername + ':' + this.apiPassword));
    return this.http.delete<Vehicle>(this.apiUrl + '/' + id, { headers });
  }

  public findOne(id: any): Observable<Vehicle> {
    const headers = new HttpHeaders()
      .append('Authorization', 'Basic ' + btoa(this.apiUsername + ':' + this.apiPassword));
    return this.http.get<Vehicle>(this.apiUrl + '/' + id, { headers });
  }

  public search(cnpj: any): Observable<Vehicle[]> {
    const params = { cnpj };
    const headers = new HttpHeaders()
      .append('Authorization', 'Basic ' + btoa(this.apiUsername + ':' + this.apiPassword));
    return this.http.get<Vehicle[]>(this.apiUrl + '/search', { headers, params });
  }

}
