import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { AuthDetails } from '../model/authDetails';
import { Login } from '../model/login.model';

@Injectable({
  providedIn: 'root',
})
export class AuthService {

  public urlLogin: string;
  public urlLogout: string;
  public oauthTokenUrlAutenticated: string;
  public oauthTokenUrlRevoke: string;
  public oauthTokenUrl: string;
  public jwtPayload: any;
  public autenticado = false;
  public apiUrlPublic: string;

  constructor(private http: HttpClient) {
    this.oauthTokenUrlAutenticated = `${environment.apiUrl}/token/autenticated`;
    this.oauthTokenUrlRevoke = `${environment.apiUrl}/token/revoke`;
    this.oauthTokenUrl = `${environment.apiUrl}/oauth/token`;
    this.apiUrlPublic = `${environment.apiUrlPublic}`;
  }

  public login(login: Login) {
    const headers = new HttpHeaders()
      .append('Authorization', 'Basic ' + btoa(login.username + ':' + login.password));
    return this.http.get<any>(this.apiUrlPublic + '/login', { headers })
      .pipe(map((response) => this.setAuthDetails(response)));
  }

  public setAuthDetails(data: AuthDetails) {
    if (data && data.name) {
      localStorage.setItem('companyCNPJ', data.principal.company.cnpj);
      localStorage.setItem('authenticated', data.authenticated);
      return data;
    } else {
      return null;
    }
  }

  public isAuthenticated(): boolean {
    const token = localStorage.getItem('authenticated');
    return token !== '';
  }
}
