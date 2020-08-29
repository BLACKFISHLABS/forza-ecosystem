import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { VehicleFormComponent } from './vehicle-form/vehicle-form.component';
import { VehicleListComponent } from './vehicle-list/vehicle-list.component';
import { VehicleViewComponent } from './vehicle-view/vehicle-view.component';

const routes: Routes = [
  { path: '', component: VehicleListComponent },
  { path: 'edit', component: VehicleFormComponent },
  { path: 'new', component: VehicleFormComponent },
  { path: 'view', component: VehicleViewComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class VehicleRoutingModule { }
