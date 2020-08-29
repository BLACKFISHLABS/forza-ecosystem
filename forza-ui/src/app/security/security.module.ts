import { CommonModule } from '@angular/common';
import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { PoContainerModule, PoModule, PoStepperModule } from '@po-ui/ng-components';
import { PoPageLoginModule } from '@po-ui/ng-templates';
import { NgxUiLoaderConfig, NgxUiLoaderModule, POSITION, SPINNER } from 'ngx-ui-loader';
import { CoreModule } from '../core/core.module';
import { LoginPageComponent } from './login-page/login-page.component';
import { RegisterPageComponent } from './register-page/register-page.component';
import { SecurityRoutingModule } from './security-routing.module';
import { UserPublicComponent } from './userpublic-page/user-page.component';

const ngxUiLoaderConfig: NgxUiLoaderConfig = {
  bgsColor: 'black',
  bgsPosition: POSITION.centerCenter,
  bgsSize: 256,
  bgsType: SPINNER.cubeGrid,
  hasProgressBar: false,
};

@NgModule({
  declarations: [
    LoginPageComponent,
    RegisterPageComponent,
    UserPublicComponent,
  ],
  imports: [
    CommonModule,
    FormsModule,
    SecurityRoutingModule,
    CoreModule,
    ReactiveFormsModule,
    PoPageLoginModule,
    PoStepperModule,
    PoContainerModule,
    PoModule,
    NgxUiLoaderModule.forRoot(ngxUiLoaderConfig),
  ],
  providers: [

  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class SecurityModule { }
