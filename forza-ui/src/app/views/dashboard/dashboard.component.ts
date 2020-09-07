import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { DashboardService } from 'src/app/services/dashboard.service';
declare var $: any;

@Component({
    selector: 'app-dashboard',
    templateUrl: './dashboard.component.html',
    styleUrls: ['./dashboard.component.css'],
})
export class DashboardComponent implements OnInit {

    public myDate = Date.now();
    public customers: number;
    public products: number;
    public sales: number;
    public salesman: number;
    public showLoading = false;

    constructor(
        private dashboardService: DashboardService,
        private router: Router,
    ) { }

    public ngOnInit() {
        this.setDashboard();
    }

    public async setDashboard() {
        this.showLoading = true;
        const cnpj = localStorage.getItem('companyCNPJ');
        const dashboard = await this.dashboardService.calculateDashboard(cnpj).toPromise();
        this.customers = dashboard.customers;
        this.sales = dashboard.sales;
        this.salesman = dashboard.salesman;
        this.products = dashboard.products;
        this.showLoading = false;
    }

    public openCustomers() {
        this.router.navigate(['app/customer']);
    }

    public openSalesMan() {
        this.router.navigate(['app/salesman']);
    }

    public openProducts() {
        this.router.navigate(['app/product']);
    }

    public openSales() {
        this.router.navigate(['app/order']);
    }
}
