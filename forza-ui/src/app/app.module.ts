import { CommonModule, registerLocaleData } from '@angular/common';
import localePtBr from '@angular/common/locales/pt';
import { CUSTOM_ELEMENTS_SCHEMA, LOCALE_ID, NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { PoFieldModule, PoModule, PoPageModule, PoTabsModule } from '@po-ui/ng-components';
import { ToastrModule } from 'ngx-toastr';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { CoreModule } from './core/core.module';
import { ModelModule } from './model/model.module';
import { SecurityModule } from './security/security.module';
import { ServicesModule } from './services/services.module';

registerLocaleData(localePtBr, 'pt-BR');

@NgModule({
  declarations: [
    AppComponent,
  ],
  imports: [
    CoreModule,
    CommonModule,
    ModelModule,
    ServicesModule,
    BrowserModule,
    PoModule,
    PoFieldModule,
    PoPageModule,
    PoTabsModule,
    SecurityModule,
    ReactiveFormsModule.withConfig({ warnOnNgModelWithFormControl: 'never' }),
    ToastrModule.forRoot({
      timeOut: 10000,
      preventDuplicates: true,
      positionClass: 'toast-bottom-right',
    }),
    BrowserAnimationsModule,
    AppRoutingModule,
  ],
  providers: [
    {
      provide: LOCALE_ID, useValue: 'pt-BR',
    },
  ],
  bootstrap: [AppComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class AppModule { }
