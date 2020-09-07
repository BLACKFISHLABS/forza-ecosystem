import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { PoBreadcrumbItem, PoTableColumn } from '@po-ui/ng-components';
import { State } from 'src/app/model/state.model';
import { StateService } from 'src/app/services/state.service';
declare var $: any;

@Component({
    selector: 'app-state-list',
    templateUrl: './state-list.component.html',
    styleUrls: ['./state-list.component.css'],
})
export class StateListComponent implements OnInit {
    public states: State[];
    public form: FormGroup;
    public showLoading = false;

    public columns: PoTableColumn[] = [
        { property: 'code', label: 'Código' },
        { property: 'nome', label: 'Nome' },
        { property: 'uf', label: 'UF' },
    ];

    public breadcrumbItems: Array<PoBreadcrumbItem> = [
        { label: 'Painel', link: '/dashboard' },
        { label: 'Estados', link: '/state' },
    ];

    constructor(
        private stateService: StateService,
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
        this.stateService.getAll().subscribe((res) => {
            this.states = res;
            this.showLoading = false;
        }, () => {
            this.showLoading = false;
        });
    }

}
