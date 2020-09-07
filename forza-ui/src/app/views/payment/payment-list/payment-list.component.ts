import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { PoBreadcrumbItem, PoTableAction, PoTableColumn } from '@po-ui/ng-components';
import { Payment } from 'src/app/model/payment.model';
import { PaymentService } from 'src/app/services/payment.service';
declare var $: any;

@Component({
    selector: 'app-payment-list',
    templateUrl: './payment-list.component.html',
    styleUrls: ['./payment-list.component.css'],
})
export class PaymentListComponent implements OnInit {

    public payments: Payment[];
    public form: FormGroup;
    public showLoading = false;

    public breadcrumbItems: Array<PoBreadcrumbItem> = [
        { label: 'Painel', link: '/dashboard' },
        { label: 'Formas de Pagamento', link: '/payment' },
    ];

    public columns: PoTableColumn[] = [
        { property: 'codigo', label: 'Código Interno' },
        { property: 'descricao', label: 'Descrição' },
        { property: 'auxPercDesc', label: 'Percentual de Desconto' },
        { property: 'ultimaAlteracao', label: 'Ultima Alteração', type: 'date' },
    ];

    public readonly actions: Array<PoTableAction> = [
        { action: this.details.bind(this), icon: 'po-icon-edit', label: 'Detalhes' },
    ];

    constructor(
        private formBuilder: FormBuilder,
        private paymentService: PaymentService,
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

        this.paymentService.getPaymentByCNPJ(cnpj).subscribe((res) => {
            this.payments = this.feedTable(res);
            this.showLoading = false;
        }, () => {
            this.showLoading = false;
        });
    }

    public feedTable(payments: Payment[]) {
        payments.forEach((element) => {
            element.auxPercDesc = element.perDesc + '%';
        });
        return payments;
    }

    public create() {
        this.router.navigate(['app/payment/new']);
    }

    public details(row: Payment) {
        this.router.navigate(['app/payment/view'], { queryParams: { code: row.codigo, idFormPgto: row.idFormPgto } });
    }
}
