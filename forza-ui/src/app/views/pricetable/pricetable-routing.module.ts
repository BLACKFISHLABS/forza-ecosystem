import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PriceTableFormComponent } from './pricetable-form/pricetable-form.component';
import { PriceTableListComponent } from './pricetable-list/pricetable-list.component';
import { PriceTableViewComponent } from './pricetable-view/pricetable-view.component';

const routes: Routes = [
  { path: '', component: PriceTableListComponent },
  { path: 'edit', component: PriceTableFormComponent },
  { path: 'new', component: PriceTableFormComponent },
  { path: 'view', component: PriceTableViewComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class PriceTableRoutingModule { }
