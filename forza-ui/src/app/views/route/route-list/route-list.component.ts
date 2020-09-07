import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { PoBreadcrumbItem, PoTableAction, PoTableColumn } from '@po-ui/ng-components';
import { Route } from 'src/app/model/route.model';
import { RouteService } from 'src/app/services/route.service';
declare var $: any;

@Component({
    selector: 'app-route-list',
    templateUrl: './route-list.component.html',
    styleUrls: ['./route-list.component.css'],
})
export class RouteListComponent implements OnInit {

    public routes: Route[];
    public form: FormGroup;
    public showLoading = false;

    public breadcrumbItems: Array<PoBreadcrumbItem> = [
        { label: 'Painel', link: '/dashboard' },
        { label: 'Rotas', link: '/route' },
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
        private formBuilder: FormBuilder,
        private routeService: RouteService,
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

        this.routeService.search(cnpj, description).subscribe((res) => {
            this.routes = res;
            this.showLoading = false;
        }, () => {
            this.showLoading = false;
        });
    }

    public details(row: Route) {
        this.router.navigate(['app/route/view'], { queryParams: { code: row.codigo, idRoute: row.routeId } });
    }
}
