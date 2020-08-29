import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { PoBreadcrumbItem, PoTableAction, PoTableColumn } from '@po-ui/ng-components';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { PriceTable } from 'src/app/model/price-table.model';
import { PriceTableService } from 'src/app/services/price-table.service';
declare var $: any;

@Component({
    selector: 'app-pricetable-list',
    templateUrl: './pricetable-list.component.html',
    styleUrls: ['./pricetable-list.component.css'],
})
export class PriceTableListComponent implements OnInit {

    public prices: PriceTable[];
    public form: FormGroup;

    public breadcrumbItems: Array<PoBreadcrumbItem> = [
        { label: 'Painel', link: '/dashboard' },
        { label: 'Tabelas de Preço', link: '/pricetable' },
    ];

    public columns: PoTableColumn[] = [
        { property: 'codigo', label: 'Código' },
        { property: 'nome', label: 'Descrição' },
        { property: 'ultimaAlteracao', label: 'Ultima Alteração', type: 'date' },
    ];

    public readonly actions: Array<PoTableAction> = [
        { action: this.details.bind(this), icon: 'po-icon-edit', label: 'Detalhes' },
    ];

    constructor(
        private priceTablesService: PriceTableService,
        private loader: NgxUiLoaderService,
        private formBuilder: FormBuilder,
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

        this.priceTablesService.search(cnpj, description).subscribe((res) => {
            this.prices = res;
            this.loader.stopBackground();
        },
            () => {
                this.loader.stopBackground();
            },
        );
    }

    public details(row: PriceTable) {
        this.router.navigate(['app/pricetable/view'], { queryParams: { code: row.codigo, idTabela: row.idTabela } });
    }
}
