import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { PoBreadcrumbItem, PoTableAction, PoTableColumn } from '@po-ui/ng-components';
import { Order } from 'src/app/model/order.model';
import { OrderService } from 'src/app/services/order.service';
declare var $: any;

@Component({
    selector: 'app-order-list',
    templateUrl: './order-list.component.html',
    styleUrls: ['./order-list.component.css'],
})
export class OrderListComponent implements OnInit {

    public orders: Order[];
    public form: FormGroup;
    public showLoading = false;

    public breadcrumbItems: Array<PoBreadcrumbItem> = [
        { label: 'Painel', link: '/dashboard' },
        { label: 'Vendas', link: '/order' },
    ];

    public columns: PoTableColumn[] = [
        { property: 'id', label: 'Código' },
        {
            property: 'auxStatus', label: 'Status', type: 'label', labels: [
                { value: 'Criado', color: 'color-03', label: 'Criado' },
                { value: 'Modificado', color: 'color-03', label: 'Modificado' },
                { value: 'Sincronizado', color: 'color-11', label: 'Sincronizado' },
                { value: 'Cancelado', color: 'color-07', label: 'Cancelado' },
                { value: 'Faturado', color: 'color-08', label: 'Faturado' },
            ],
        },
        { property: 'dtEmissao', label: 'Data de Emissão', type: 'date' },
        { property: 'auxVendedor', label: 'Vendedor' },
        { property: 'resumo', label: 'Resumo' },
        { property: 'customer', label: 'Cliente' },
        { property: 'total', label: 'Total', type: 'currency', format: 'BRL' },
    ];

    public readonly actions: Array<PoTableAction> = [
        { action: this.details.bind(this), icon: 'po-icon-edit', label: 'Detalhes' },
    ];

    constructor(
        private formBuilder: FormBuilder,
        private orderService: OrderService,
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

        this.orderService.searchOrderByFilter(cnpj, description).subscribe((res) => {
            this.orders = this.feedTable(res);
            this.showLoading = false;
        }, () => {
            this.showLoading = false;
        });
    }

    public feedTable(orders: Order[]) {
        orders.forEach((element) => {
            element.auxVendedor = element.vendedor.nome;
            element.auxStatus = this.translateStatus(element.status);
        });
        return orders;
    }

    public translateStatus(status: number) {
        switch (status) {
            case 0:
                return 'Criado';
                break;
            case 1:
                return 'Modificado';
                break;
            case 2:
                return 'Sincronizado';
                break;
            case 3:
                return 'Cancelado';
                break;
            case 5:
                return 'Faturado';
                break;
            default:
                return '';
                break;
        }
    }

    public details(row: Order) {
        this.router.navigate(['app/order/view'], { queryParams: { id: row.id } });
    }
}
