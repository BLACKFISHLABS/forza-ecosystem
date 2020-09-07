import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { PoBreadcrumbItem, PoTableAction, PoTableColumn } from '@po-ui/ng-components';
import { Customer } from 'src/app/model/customer.model';
import { CustomerService } from 'src/app/services/customer.service';
declare var $: any;

@Component({
    selector: 'app-customer-list',
    templateUrl: './customer-list.component.html',
    styleUrls: ['./customer-list.component.css'],
})
export class CustomerListComponent implements OnInit {

    public customers: Customer[];
    public form: FormGroup;
    public moreResults: boolean;
    public showLoading = false;

    public breadcrumbItems: Array<PoBreadcrumbItem> = [
        { label: 'Painel', link: '/dashboard' },
        { label: 'Clientes', link: '/customer' },
    ];

    public columns: PoTableColumn[] = [
        { property: 'codigo', label: 'Código' },
        { property: 'nomeFantasia', label: 'Nome Fantasia' },
        { property: 'cpfCnpj', label: 'CPF/CNPJ' },
        { property: 'ultimaAlteracao', label: 'Ultima Alteração', type: 'date' },
    ];

    public readonly actions: Array<PoTableAction> = [
        { action: this.details.bind(this), icon: 'po-icon-edit', label: 'Detalhes' },
    ];

    constructor(
        private formBuilder: FormBuilder,
        private customerService: CustomerService,
        private router: Router,
    ) { }

    public ngOnInit() {
        this.form = this.formBuilder.group({
            description: ['', []],
        });

        this.moreResults = false;

        this.setPage();
    }

    public setPage() {
        this.showLoading = true;
        const cnpj = localStorage.getItem('companyCNPJ');
        const description = this.form.get('description').value || '';

        this.customerService.searchCustomerByFilter(cnpj, description, '').subscribe((res) => {
            this.customers = res;
            this.showLoading = false;
        }, () => {
            this.showLoading = false;
        });
    }

    public details(row: Customer) {
        this.router.navigate(['app/customer/view'], { queryParams: { code: row.codigo, idCustomer: row.idCliente } });
    }
}
