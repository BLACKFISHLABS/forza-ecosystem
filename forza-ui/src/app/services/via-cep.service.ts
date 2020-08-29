import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { ViaCEP } from '../model/via-cep.model';

@Injectable({
    providedIn: 'root',
})
export class ViaCEPService {
    private apiUrl: string;
    private apiUsername: string;
    private apiPassword: string;

    constructor(
        private http: HttpClient,
    ) {
        this.apiUsername = localStorage.getItem('username');
        this.apiPassword = localStorage.getItem('password');
        this.apiUrl = `${environment.apiUrl}` + '/cep';
    }

    public getCEP(cep: any): Observable<ViaCEP> {
        const params = { cep };
        const headers = new HttpHeaders()
            .append('Authorization', 'Basic ' + btoa(this.apiUsername + ':' + this.apiPassword));
        return this.http.get<ViaCEP>(this.apiUrl + '/viaCep', { params });
    }
}
