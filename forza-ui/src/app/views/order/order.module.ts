import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { PoModule, PoTabsModule } from '@po-ui/ng-components';
import { OrderListComponent } from './order-list/order-list.component';
import { OrderRoutingModule } from './order-routing.module';
import { OrderViewComponent } from './order-view/order-view.component';

@NgModule({
  declarations: [
    OrderListComponent,
    OrderViewComponent,
  ],
  imports: [
    FormsModule,
    ReactiveFormsModule,
    CommonModule,
    PoTabsModule,
    PoModule,
    OrderRoutingModule,
  ],
})
export class OrderModule { }
