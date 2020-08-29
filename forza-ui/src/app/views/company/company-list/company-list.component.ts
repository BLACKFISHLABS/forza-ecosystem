import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { PoBreadcrumbItem, PoTableAction, PoTableColumn } from '@po-ui/ng-components';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { Company } from 'src/app/model/company.model';
import { CompanyService } from 'src/app/services/company.service';
declare var $: any;

@Component({
    selector: 'app-company-list',
    templateUrl: './company-list.component.html',
    styleUrls: ['./company-list.component.css'],
})
export class CompanyListComponent implements OnInit {

    public companys: Company[];
    public form: FormGroup;

    public breadcrumbItems: Array<PoBreadcrumbItem> = [
        { label: 'Painel', link: '/dashboard' },
        { label: 'Empresa', link: '/company' },
    ];

    public columns: PoTableColumn[] = [
        { property: 'idEmpresa', label: 'Código' },
        { property: 'nome', label: 'Nome' },
        { property: 'companyName', label: 'Nome Fantasia' },
    ];

    public readonly actions: Array<PoTableAction> = [
        { action: this.details.bind(this), icon: 'po-icon-edit', label: 'Detalhes' },
    ];

    constructor(
        private loader: NgxUiLoaderService,
        private formBuilder: FormBuilder,
        private companyService: CompanyService,
        private router: Router,
    ) { }

    public ngOnInit() {
        this.form = this.formBuilder.group({
            description: ['', []],
        });

        this.setPage();
    }

    public setPage() {
        this.loader.startBackground();
        const cnpj = localStorage.getItem('companyCNPJ');
        const description = this.form.get('description').value || '';

        this.companyService.getSearch(cnpj).subscribe((res) => {
            this.companys = this.feedTable(res);
            this.loader.stopBackground();
        }, () => {
            this.loader.stopBackground();
        });
    }

    public feedTable(companys: Company[]) {
        companys.forEach((element) => {

        });
        return companys;
    }

    public details(row: Company) {
        this.router.navigate(['app/company/view'], { queryParams: { code: row.idEmpresa, idEmpresa: row.idEmpresa } });
    }

}
