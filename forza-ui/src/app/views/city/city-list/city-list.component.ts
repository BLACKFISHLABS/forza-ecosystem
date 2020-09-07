import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { PoBreadcrumbItem, PoTableColumn } from '@po-ui/ng-components';
import { City } from 'src/app/model/city.model';
import { CityService } from 'src/app/services/city.service';
declare var $: any;

@Component({
    selector: 'app-city-list',
    templateUrl: './city-list.component.html',
    styleUrls: ['./city-list.component.css'],
})
export class CityListComponent implements OnInit {

    public citys: City[];
    public form: FormGroup;
    public showLoading = false;

    public breadcrumbItems: Array<PoBreadcrumbItem> = [
        { label: 'Painel', link: '/dashboard' },
        { label: 'Cidades', link: '/city' },
    ];

    public columns: PoTableColumn[] = [
        { property: 'codMunicipio', label: 'Código da Cidade' },
        { property: 'nome', label: 'Nome' },
        { property: 'auxCode', label: 'Sigla UF' },
    ];

    constructor(
        private cityService: CityService,
        private formBuilder: FormBuilder,
    ) { }

    public ngOnInit() {
        this.form = this.formBuilder.group({
            description: ['', []],
        });

        this.setPage();
    }

    public setPage() {
        this.showLoading = true;
        this.cityService.getAll().subscribe((response) => {
            this.citys = this.feedTable(response);
            this.showLoading = false;
        }, () => {
            this.showLoading = false;
        });
    }

    public feedTable(citys: City[]) {
        citys.forEach((element) => {
            element.auxCode = element.Estado.uf;
        });
        return citys;
    }
}
