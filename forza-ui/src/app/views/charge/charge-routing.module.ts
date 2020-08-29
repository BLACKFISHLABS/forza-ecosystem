import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ChargeFormComponent } from './charge-form/charge-form.component';
import { ChargeListComponent } from './charge-list/charge-list.component';
import { ChargeViewComponent } from './charge-view/charge-view.component';

const routes: Routes = [
  { path: '', component: ChargeListComponent },
  { path: 'edit', component: ChargeFormComponent },
  { path: 'new', component: ChargeFormComponent },
  { path: 'view', component: ChargeViewComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ChargeRoutingModule { }
