import { Component, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { PoMenuComponent, PoMenuItem, PoToolbarAction } from '@po-ui/ng-components';
import { NgxUiLoaderService } from 'ngx-ui-loader';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css'],
})
export class MainComponent implements OnInit {

  @ViewChild(PoMenuComponent, { static: true }) public menu: PoMenuComponent;

  public userLogged = JSON.parse(localStorage.getItem('authDetails'));
  public profile = {
    subtitle: this.userLogged.company.cnpj,
    title: this.userLogged.company.companyName,
  };

  constructor(
    private router: Router,
    private loader: NgxUiLoaderService,
  ) { }

  public profileActions: Array<PoToolbarAction> = [
    // { icon: 'po-icon-user', label: 'Usuários', action: this.onClickUser.bind(this) },
    // { icon: 'po-icon-users', label: 'Perfis', action: this.onClickProfiles.bind(this) },
    { icon: 'po-icon-exit', label: 'Sair', type: 'danger', separator: true, action: this.logout.bind(this) },
  ];

  public menus: Array<PoMenuItem> = [
    { label: 'Painel', icon: 'po-icon-device-desktop', action: this.onClickPanel.bind(this), shortLabel: 'Painel' },
    { label: 'Vendas', icon: 'po-icon-sale', action: this.onClickSales.bind(this), shortLabel: 'Vendas' },
    {
      label: 'Mercadorias', icon: 'po-icon-pushcart', shortLabel: 'Merc.', subItems: [
        { label: 'Lista de Produtos', action: this.onClickProduct.bind(this) },
        { label: 'Tabelas de Preço/Estoque', action: this.onClickPriceTable.bind(this) },
      ],
    },
    {
      label: 'Clientes', icon: 'po-icon-users', shortLabel: 'Clientes', subItems: [
        { label: 'Lista de Clientes', action: this.onClickCustomer.bind(this) },
        { label: 'Rotas/Endereços', action: this.onClickRoutes.bind(this) },
      ],
    },
    { label: 'Vendedores', icon: 'po-icon-device-tablet', action: this.onClickSalesMan.bind(this), shortLabel: 'Vend.' },
    { label: 'Frota', icon: 'po-icon-truck', action: this.onClickVehicle.bind(this), shortLabel: 'Frota' },
    { label: 'Resumo', icon: 'po-icon-upload', action: this.onClickCharge.bind(this), shortLabel: 'Resumo' },
    {
      label: 'Tabelas', icon: 'po-icon-grid', shortLabel: 'Tabelas', subItems: [
        { label: 'Forma de Pagamentos', action: this.onClickPayment.bind(this) },
        // { label: 'Estados', action: this.onClickState.bind(this) },
        // { label: 'Cidades', action: this.onClickCity.bind(this) },
      ],
    },
    {
      label: 'Configurações', icon: 'po-icon-settings', shortLabel: 'Config.', subItems: [
        { label: 'Minha Empresa', action: this.onClickCompany.bind(this) },
        // { label: 'API', action: this.onClickAPI.bind(this) },
      ],
    },
  ];

  public ngOnInit(): void {
  }

  private onClickSales() {
    this.router.navigate(['app/order']);
  }

  private onClickProduct() {
    this.router.navigate(['app/product']);
  }

  private onClickPriceTable() {
    this.router.navigate(['app/pricetable']);
  }

  private onClickCustomer() {
    this.router.navigate(['app/customer']);
  }

  private onClickRoutes() {
    this.router.navigate(['app/route']);
  }

  private onClickPayment() {
    this.router.navigate(['app/payment']);
  }

  private onClickState() {
    this.router.navigate(['app/state']);
  }

  private onClickCity() {
    this.router.navigate(['app/city']);
  }

  private onClickSalesMan() {
    this.router.navigate(['app/salesman']);
  }

  private onClickCharge() {
    this.router.navigate(['app/charge']);
  }

  private onClickVehicle() {
    this.router.navigate(['app/vehicle']);
  }

  private onClickCompany() {
    this.router.navigate(['app/company/view'], {
      queryParams:
      {
        code: this.userLogged.company.idEmpresa,
        idEmpresa: this.userLogged.company.idEmpresa,
      },
    });
  }

  private onClickAPI() {
    window.open('http://forza.blackfishlabs.com.br/swagger-ui.html', '_blank');
  }

  private onClickPanel() {
    this.router.navigate(['app/dashboard']);
  }

  private onClickUser() {
  }

  private onClickProfiles() {
  }

  public async logout(): Promise<void> {
    this.loader.startBackground();
    localStorage.removeItem('authenticated');
    localStorage.removeItem('authDetails');
    localStorage.removeItem('companyCNPJ');
    localStorage.removeItem('password');
    localStorage.removeItem('username');

    this.router.navigate(['login']);
    this.loader.stopBackground();

    const keys = await window.caches.keys();
    await Promise.all(keys.map((key) => caches.delete(key)));
  }
}
