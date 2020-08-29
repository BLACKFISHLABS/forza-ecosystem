import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

@Injectable({
    providedIn: 'root',
})
export class SwaggerService {

    private apiUrl: string;
    private apiUsername: string;
    private apiPassword: string;

    constructor(
        private http: HttpClient,
    ) {
        this.apiUsername = localStorage.getItem('username');
        this.apiPassword = localStorage.getItem('password');
        this.apiUrl = `${environment.apiUrl}` + '/swagger-ui';
    }

}
