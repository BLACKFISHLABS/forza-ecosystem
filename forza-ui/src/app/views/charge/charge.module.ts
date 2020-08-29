import { CommonModule } from '@angular/common';
import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { PoModule, PoTabsModule } from '@po-ui/ng-components';
import { ChargeFormComponent } from './charge-form/charge-form.component';
import { ChargeListComponent } from './charge-list/charge-list.component';
import { ChargeRoutingModule } from './charge-routing.module';
import { ChargeViewComponent } from './charge-view/charge-view.component';

@NgModule({
  declarations: [
    ChargeListComponent, ChargeFormComponent, ChargeViewComponent
  ],
  imports: [
    FormsModule,
    ReactiveFormsModule,
    CommonModule,
    PoTabsModule,
    PoModule,
    ChargeRoutingModule,
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class ChargeModule { }
