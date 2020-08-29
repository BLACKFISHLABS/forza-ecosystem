import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RouteFormComponent } from './route-form/route-form.component';
import { RouteListComponent } from './route-list/route-list.component';
import { RouteViewComponent } from './route-view/route-view.component';

const routes: Routes = [
  { path: '', component: RouteListComponent },
  { path: 'edit', component: RouteFormComponent },
  { path: 'new', component: RouteFormComponent },
  { path: 'view', component: RouteViewComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class RouteRoutingModule { }
