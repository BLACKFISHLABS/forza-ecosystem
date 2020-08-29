import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PaymentFormComponent } from './payment-form/payment-form.component';
import { PaymentListComponent } from './payment-list/payment-list.component';
import { PaymentViewComponent } from './payment-view/payment-view.component';

const routes: Routes = [
  { path: '', component: PaymentListComponent },
  { path: 'edit', component: PaymentFormComponent },
  { path: 'new', component: PaymentFormComponent },
  { path: 'view', component: PaymentViewComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class PaymentRoutingModule { }
