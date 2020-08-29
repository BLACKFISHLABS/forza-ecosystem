import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CompanyFormComponent } from './company-form/company-form.component';
import { CompanyListComponent } from './company-list/company-list.component';
import { CompanyViewComponent } from './company-view/company-view.component';

const routes: Routes = [
  { path: '', component: CompanyListComponent },
  { path: 'edit', component: CompanyFormComponent },
  { path: 'new', component: CompanyFormComponent },
  { path: 'view', component: CompanyViewComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class CompanyRoutingModule { }
