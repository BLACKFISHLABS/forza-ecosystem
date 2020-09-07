import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { PoBreadcrumbItem, PoTableAction, PoTableColumn } from '@po-ui/ng-components';
import { SalesMan } from 'src/app/model/salesman.model';
import { SalesManService } from 'src/app/services/salesman.service';
declare var $: any;

@Component({
    selector: 'app-salesman-list',
    templateUrl: './salesman-list.component.html',
    styleUrls: ['./salesman-list.component.css'],
})
export class SalesManListComponent implements OnInit {

    public salesmans: SalesMan[];
    public form: FormGroup;
    public showLoading = false;

    public breadcrumbItems: Array<PoBreadcrumbItem> = [
        { label: 'Painel', link: '/dashboard' },
        { label: 'Vendedores', link: '/salesman' },
    ];

    public columns: PoTableColumn[] = [
        { property: 'codigo', label: 'Código Interno' },
        { property: 'nome', label: 'Nome' },
        { property: 'cpfCnpj', label: 'CPF/CNPJ' },
        { property: 'ultimaAlteracao', label: 'Última Alteração', type: 'date' },
    ];

    public readonly actions: Array<PoTableAction> = [
        { action: this.details.bind(this), icon: 'po-icon-edit', label: 'Detalhes' },
    ];

    constructor(
        private formBuilder: FormBuilder,
        private salesManService: SalesManService,
        private router: Router,
    ) { }

    public ngOnInit() {
        this.form = this.formBuilder.group({
            description: ['', []],
        });

        this.setPage();
    }

    public setPage() {
        this.showLoading = true;
        const cnpj = localStorage.getItem('companyCNPJ');
        const description = this.form.get('description').value || '';

        this.salesManService.search(cnpj).subscribe((res) => {
            this.salesmans = res;
            this.showLoading = false;
        },
            () => {
                this.showLoading = false;
            },
        );
    }

    public create() {
        this.router.navigate(['app/salesman/new']);
    }

    public details(row: SalesMan) {
        this.router.navigate(['app/salesman/view'], { queryParams: { code: row.codigo, idVendedor: row.idVendedor } });
    }
}
