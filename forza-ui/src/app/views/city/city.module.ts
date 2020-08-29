import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { PoModule, PoTabsModule } from '@po-ui/ng-components';
import { CityListComponent } from './city-list/city-list.component';
import { CityRoutingModule } from './city-routing.module';

@NgModule({
  declarations: [
    CityListComponent,
  ],
  imports: [
    FormsModule,
    ReactiveFormsModule,
    CommonModule,
    PoTabsModule,
    PoModule,
    CityRoutingModule,
  ],
})
export class CityModule { }
