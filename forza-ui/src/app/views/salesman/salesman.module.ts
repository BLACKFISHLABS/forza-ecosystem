import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { PoModule, PoTabsModule } from '@po-ui/ng-components';
import { SalesManFormComponent } from './salesman-form/salesman-form.component';
import { SalesManListComponent } from './salesman-list/salesman-list.component';
import { SalesManRoutingModule } from './salesman-routing.module';
import { SalesManViewComponent } from './salesman-view/salesman-view.component';

@NgModule({
  declarations: [
    SalesManListComponent, SalesManFormComponent, SalesManViewComponent,
  ],
  imports: [
    FormsModule,
    ReactiveFormsModule,
    CommonModule,
    PoTabsModule,
    PoModule,
    SalesManRoutingModule,
  ],
})
export class SalesManModule { }
