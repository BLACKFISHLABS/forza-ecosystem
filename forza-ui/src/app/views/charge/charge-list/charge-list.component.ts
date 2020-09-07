import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { PoBreadcrumbItem, PoTableAction, PoTableColumn } from '@po-ui/ng-components';
import { Charge } from 'src/app/model/charge.model';
import { ChargeService } from 'src/app/services/charge.service';
declare var $: any;

@Component({
    selector: 'app-charge-list',
    templateUrl: './charge-list.component.html',
    styleUrls: ['./charge-list.component.css'],
})
export class ChargeListComponent implements OnInit {

    public charges: Charge[];
    public form: FormGroup;
    public showLoading = false;

    public breadcrumbItems: Array<PoBreadcrumbItem> = [
        { label: 'Painel', link: '/dashboard' },
        { label: 'Resumo de Cargas', link: '/charge' },
    ];

    public columns: PoTableColumn[] = [
        { property: 'codigo', label: 'Código' },
        { property: 'emissao', label: 'Emissão', type: 'date' },
        { property: 'auxStatus', label: 'Status' },
        { property: 'vendedor', label: 'Vendedor' },
        { property: 'auxVehicle', label: 'Veículo' },
    ];

    public readonly actions: Array<PoTableAction> = [
        { action: this.details.bind(this), icon: 'po-icon-edit', label: 'Detalhes' },
    ];

    constructor(
        private formBuilder: FormBuilder,
        private chargeService: ChargeService,
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

        this.chargeService.search(cnpj, description).subscribe((res) => {
            this.charges = this.feedTable(res);
            this.showLoading = false;
        }, () => {
            this.showLoading = false;
        });
    }

    public feedTable(charges: Charge[]) {
        charges.forEach((element) => {
            element.auxVehicle = element.veiculo.placa;
            element.auxStatus = this.translateStatus(element.status);
        });
        return charges;
    }

    public translateStatus(status: number) {
        switch (status) {
            case 0:
                return 'Criado';
                break;
            case 1:
                return 'Sincronizado';
                break;
            case 2:
                return 'Finalizado';
                break;
            default:
                return '';
                break;
        }
    }

    public details(row: Charge) {
        this.router.navigate(['app/charge/view'], { queryParams: { code: row.codigo, idCharge: row.id } });
    }
}
