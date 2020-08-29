import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { PoModule } from '@po-ui/ng-components';
import { AccessDaniedComponent } from './views/access-danied/access-danied.component';
import { ErrorMessageComponent } from './views/error-message/error-message.component';
import { PageNotFoundComponent } from './views/page-not-found/page-not-found.component';

@NgModule({
  declarations: [
    PageNotFoundComponent,
    AccessDaniedComponent,
    ErrorMessageComponent,
  ],
  imports: [
    CommonModule,
    PoModule,
  ],
  exports: [
    ErrorMessageComponent,
  ],
})
export class CoreModule { }
