import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { PoModule, PoTabsModule } from '@po-ui/ng-components';
import { RouteFormComponent } from './route-form/route-form.component';
import { RouteListComponent } from './route-list/route-list.component';
import { RouteRoutingModule } from './route-routing.module';
import { RouteViewComponent } from './route-view/route-view.component';

@NgModule({
  declarations: [
    RouteListComponent, RouteFormComponent, RouteViewComponent,
  ],
  imports: [
    FormsModule,
    ReactiveFormsModule,
    CommonModule,
    PoTabsModule,
    PoModule,
    RouteRoutingModule,
  ]
})
export class RouteModule { }
