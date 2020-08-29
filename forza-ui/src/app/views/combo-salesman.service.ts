import { Injectable } from '@angular/core';
import { PoComboFilter, PoComboOption } from '@po-ui/ng-components';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { SalesManService } from '../services/salesman.service';

@Injectable()
export class ComboSalesmanService implements PoComboFilter {

    constructor(private salesmanService: SalesManService) { }

    public getFilteredData(param, filterParams?: any): Observable<Array<PoComboOption>> {
        const cnpj = localStorage.getItem('companyCNPJ');
        return this.salesmanService.search(cnpj).pipe(map((response: any) => {
            return this.convertToArrayComboOption(response);
        }));
    }

    public getObjectByValue(value): Observable<PoComboOption> {
        return this.salesmanService.findOne(value)
            .pipe(map((item) => this.convertToPoComboOption(item)));
    }

    private convertToArrayComboOption(items: Array<any>): Array<PoComboOption> {
        if (items && items.length > 0) {
            return items.map((item) => this.convertToPoComboOption(item));
        }
        return [];
    }

    private convertToPoComboOption(item): PoComboOption {
        item = item || {};

        return {
            value: item.idVendedor || undefined,
            label: item.idVendedor + ' - ' + item.nome || undefined,
        };
    }
}