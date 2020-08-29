import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { PoBreadcrumbItem, PoTableAction, PoTableColumn } from '@po-ui/ng-components';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { Vehicle } from 'src/app/model/vehicle.model';
import { VehicleService } from 'src/app/services/vehicle.service';
declare var $: any;

@Component({
    selector: 'app-vehicle-list',
    templateUrl: './vehicle-list.component.html',
    styleUrls: ['./vehicle-list.component.css'],
})
export class VehicleListComponent implements OnInit {

    public vehicles: Vehicle[];
    public form: FormGroup;

    public breadcrumbItems: Array<PoBreadcrumbItem> = [
        { label: 'Painel', link: '/dashboard' },
        { label: 'Veículos', link: '/vehicle' },
    ];

    public columns: PoTableColumn[] = [
        { property: 'plate', label: 'Placa' },
        { property: 'description', label: 'Descrição' },
    ];

    public readonly actions: Array<PoTableAction> = [
        { action: this.view.bind(this), icon: 'po-icon-edit', label: 'Detalhes' },
    ];

    constructor(
        private loader: NgxUiLoaderService,
        private formBuilder: FormBuilder,
        private vehicleService: VehicleService,
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

        this.vehicleService.search(cnpj).subscribe((res) => {
            this.vehicles = res;
            this.loader.stopBackground();
        },
            () => {
                this.loader.stopBackground();
            },
        );
    }

    public create() {
        this.router.navigate(['app/vehicle/new']);
    }

    public view(row: Vehicle) {
        this.router.navigate(['app/vehicle/view'], { queryParams: { id: row.id } });
    }
}
