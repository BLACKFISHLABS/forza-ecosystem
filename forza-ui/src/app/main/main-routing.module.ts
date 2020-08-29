import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AccessDaniedComponent } from '../core/views/access-danied/access-danied.component';
import { PageNotFoundComponent } from '../core/views/page-not-found/page-not-found.component';
import { AuthGuard } from '../security/auth.guard';
import { MainComponent } from './main.component';

const routes: Routes = [
    {
        path: '', component: MainComponent, children: [
            {
                path: 'dashboard',
                canActivate: [AuthGuard],
                loadChildren: () => import('../views/dashboard/dashboard.module').then((m) => m.DashboardModule),
            },
            {
                path: 'order',
                canActivate: [AuthGuard],
                loadChildren: () => import('../views/order/order.module').then((m) => m.OrderModule),
            },
            {
                path: 'product',
                canActivate: [AuthGuard],
                loadChildren: () => import('../views/product/product.module').then((m) => m.ProductModule),
            },
            {
                path: 'pricetable',
                canActivate: [AuthGuard],
                loadChildren: () => import('../views/pricetable/pricetable.module').then((m) => m.PriceTableModule),
            },
            {
                path: 'customer',
                canActivate: [AuthGuard],
                loadChildren: () => import('../views/customer/customer.module').then((m) => m.CustomerModule),
            },
            {
                path: 'route',
                canActivate: [AuthGuard],
                loadChildren: () => import('../views/route/route.module').then((m) => m.RouteModule),
            },
            {
                path: 'salesman',
                canActivate: [AuthGuard],
                loadChildren: () => import('../views/salesman/salesman.module').then((m) => m.SalesManModule),
            },
            {
                path: 'vehicle',
                canActivate: [AuthGuard],
                loadChildren: () => import('../views/vehicle/vehicle.module').then((m) => m.VehicleModule),
            },
            {
                path: 'charge',
                canActivate: [AuthGuard],
                loadChildren: () => import('../views/charge/charge.module').then((m) => m.ChargeModule),
            },
            {
                path: 'payment',
                canActivate: [AuthGuard],
                loadChildren: () => import('../views/payment/payment.module').then((m) => m.PaymentModule),
            },
            {
                path: 'state',
                canActivate: [AuthGuard],
                loadChildren: () => import('../views/state/state.module').then((m) => m.StateModule),
            },
            {
                path: 'city',
                canActivate: [AuthGuard],
                loadChildren: () => import('../views/city/city.module').then((m) => m.CityModule),
            },
            {
                path: 'company',
                canActivate: [AuthGuard],
                loadChildren: () => import('../views/company/company.module').then((m) => m.CompanyModule),
            },

            /** DEFAULT */
            { path: 'acesso-negado', component: AccessDaniedComponent, canActivate: [AuthGuard] },
            { path: 'pagina-nao-encontrada', component: PageNotFoundComponent, canActivate: [AuthGuard] },
            { path: '**', redirectTo: 'pagina-nao-encontrada', canActivate: [AuthGuard] },

        ],
    },
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule],
})
export class HomeRoutingModule { }
