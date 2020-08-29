import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { PoModule, PoTabsModule } from '@po-ui/ng-components';
import { CompanyFormComponent } from './company-form/company-form.component';
import { CompanyListComponent } from './company-list/company-list.component';
import { CompanyRoutingModule } from './company-routing.module';
import { CompanyViewComponent } from './company-view/company-view.component';

@NgModule({
  declarations: [
    CompanyListComponent,
    CompanyFormComponent,
    CompanyViewComponent,
  ],
  imports: [
    FormsModule,
    ReactiveFormsModule,
    CommonModule,
    PoTabsModule,
    PoModule,
    CompanyRoutingModule,
  ],
})
export class CompanyModule { }
