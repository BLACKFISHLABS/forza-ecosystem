import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SalesManFormComponent } from './salesman-form/salesman-form.component';
import { SalesManListComponent } from './salesman-list/salesman-list.component';
import { SalesManViewComponent } from './salesman-view/salesman-view.component';

const routes: Routes = [
  { path: '', component: SalesManListComponent },
  { path: 'edit', component: SalesManFormComponent },
  { path: 'new', component: SalesManFormComponent },
  { path: 'view', component: SalesManViewComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class SalesManRoutingModule { }
