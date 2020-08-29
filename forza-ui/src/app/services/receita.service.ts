import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { DadosReceita } from '../model/dados-receita.model';

@Injectable({
    providedIn: 'root',
})
export class ReceitaService {
    private apiUrl: string;

    constructor(
        private http: HttpClient,
    ) {
        this.apiUrl = `${environment.apiUrl}` + '/receita/cnpj';
    }

    public getCNPJ(cnpj: any): Observable<DadosReceita> {
        const params = { cnpj };
        return this.http.get<DadosReceita>(this.apiUrl, { params });
    }
}
