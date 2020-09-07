import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { PoMenuModule, PoToolbarModule } from '@po-ui/ng-components';
import { CoreModule } from '../core/core.module';
import { ModelModule } from '../model/model.module';
import { ServicesModule } from '../services/services.module';
import { ViewsModule } from '../views/views.module';
import { HomeRoutingModule } from './main-routing.module';
import { MainComponent } from './main.component';

@NgModule({
  declarations: [MainComponent],
  imports: [
    CoreModule,
    CommonModule,
    PoToolbarModule,
    PoMenuModule,
    ModelModule,
    ServicesModule,
    ViewsModule,
    HomeRoutingModule,
  ],
  providers: [
  ],
})
export class MainModule { }
