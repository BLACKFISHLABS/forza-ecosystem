import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { PoMenuModule, PoToolbarModule } from '@po-ui/ng-components';
import { NgxUiLoaderConfig, NgxUiLoaderModule, POSITION, SPINNER } from 'ngx-ui-loader';
import { CoreModule } from '../core/core.module';
import { ModelModule } from '../model/model.module';
import { ServicesModule } from '../services/services.module';
import { ViewsModule } from '../views/views.module';
import { HomeRoutingModule } from './main-routing.module';
import { MainComponent } from './main.component';

const ngxUiLoaderConfig: NgxUiLoaderConfig = {
  bgsColor: 'black',
  bgsPosition: POSITION.centerCenter,
  bgsSize: 256,
  bgsType: SPINNER.cubeGrid,
  hasProgressBar: false,
};

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
    NgxUiLoaderModule.forRoot(ngxUiLoaderConfig),
    HomeRoutingModule,
  ],
  providers: [
  ],
})
export class MainModule { }
