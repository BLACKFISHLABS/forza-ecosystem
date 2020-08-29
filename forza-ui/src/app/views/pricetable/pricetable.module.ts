import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { PoModule, PoTabsModule } from '@po-ui/ng-components';
import { PriceTableFormComponent } from './pricetable-form/pricetable-form.component';
import { PriceTableListComponent } from './pricetable-list/pricetable-list.component';
import { PriceTableRoutingModule } from './pricetable-routing.module';
import { PriceTableViewComponent } from './pricetable-view/pricetable-view.component';

@NgModule({
  declarations: [
    PriceTableListComponent, PriceTableFormComponent, PriceTableViewComponent
  ],
  imports: [
    FormsModule,
    ReactiveFormsModule,
    CommonModule,
    PoTabsModule,
    PoModule,
    PriceTableRoutingModule,
  ]
})
export class PriceTableModule { }
