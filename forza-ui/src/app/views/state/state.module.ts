import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { PoModule, PoTabsModule } from '@po-ui/ng-components';
import { StateListComponent } from './state-list/state-list.component';
import { StateRoutingModule } from './state-routing.module';

@NgModule({
  declarations: [
    StateListComponent,
  ],
  imports: [
    FormsModule,
    ReactiveFormsModule,
    CommonModule,
    PoTabsModule,
    PoModule,
    StateRoutingModule,
  ],
})
export class StateModule { }
