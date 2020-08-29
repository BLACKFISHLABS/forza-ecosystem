import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { PoBreadcrumbItem, PoTableAction, PoTableColumn } from '@po-ui/ng-components';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { Product } from 'src/app/model/product.model';
import { ProductService } from 'src/app/services/product.service';
declare var $: any;

@Component({
    selector: 'app-product-list',
    templateUrl: './product-list.component.html',
    styleUrls: ['./product-list.component.css'],
})
export class ProductListComponent implements OnInit {

    public products: Product[];
    public form: FormGroup;

    public columns: PoTableColumn[] = [
        { property: 'codigo', label: 'Código' },
        { property: 'codigoBarras', label: 'Codigo de Barras' },
        { property: 'descricao', label: 'Descrição' },
        { property: 'un', label: 'Unidade' },
        { property: 'ultimaAlteracao', label: 'Última Alteração', type: 'date' },
    ];

    public breadcrumbItems: Array<PoBreadcrumbItem> = [
        { label: 'Painel', link: '/dashboard' },
        { label: 'Produtos', link: 'product' },
    ];

    public readonly actions: Array<PoTableAction> = [
        { action: this.details.bind(this), icon: 'po-icon-edit', label: 'Detalhes' },
    ];

    constructor(
        private productService: ProductService,
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

        this.productService.search(cnpj, description).subscribe((res) => {
            this.products = res;
            this.loader.stopBackground();
        }, () => {
            this.loader.stopBackground();
        });
    }

    public details(row: Product) {
        this.router.navigate(['app/product/view'], { queryParams: { code: row.codigo, idProduc: row.idProduto } });
    }
}
