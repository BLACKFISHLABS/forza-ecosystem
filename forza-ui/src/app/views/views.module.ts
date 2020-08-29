import { NgModule } from '@angular/core';
import { ChargeModule } from './charge/charge.module';
import { CityModule } from './city/city.module';
import { CompanyModule } from './company/company.module';
import { CustomerModule } from './customer/customer.module';
import { DashboardModule } from './dashboard/dashboard.module';
import { OrderModule } from './order/order.module';
import { PaymentModule } from './payment/payment.module';
import { PriceTableModule } from './pricetable/pricetable.module';
import { ProductModule } from './product/product.module';
import { RouteModule } from './route/route.module';
import { SalesManModule } from './salesman/salesman.module';
import { StateModule } from './state/state.module';
import { VehicleModule } from './vehicle/vehicle.module';

@NgModule({
  declarations: [

  ],
  imports: [
    DashboardModule,
    ChargeModule,
    CityModule,
    CompanyModule,
    CustomerModule,
    OrderModule,
    PaymentModule,
    PriceTableModule,
    ProductModule,
    RouteModule,
    SalesManModule,
    StateModule,
    VehicleModule,
  ],
  exports: [
    DashboardModule,
    ChargeModule,
    CityModule,
    CompanyModule,
    CustomerModule,
    OrderModule,
    PaymentModule,
    PriceTableModule,
    ProductModule,
    RouteModule,
    SalesManModule,
    StateModule,
    VehicleModule,
  ],
})
export class ViewsModule { }
