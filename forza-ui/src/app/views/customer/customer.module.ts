import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { PoModule, PoTabsModule } from '@po-ui/ng-components';
import { CustomerListComponent } from './customer-list/customer-list.component';
import { CustomerRoutingModule } from './customer-routing.module';
import { CustomerViewComponent } from './customer-view/customer-view.component';

@NgModule({
  declarations: [
    CustomerListComponent,
    CustomerViewComponent,
  ],
  imports: [
    FormsModule,
    ReactiveFormsModule,
    CommonModule,
    PoTabsModule,
    PoModule,
    CustomerRoutingModule,
  ],
})
export class CustomerModule { }
