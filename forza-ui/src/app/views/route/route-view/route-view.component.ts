import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PoBreadcrumbItem, PoTableColumn, PoDialogService } from '@po-ui/ng-components';
import { RouteCustomer } from 'src/app/model/route-customer.model';
import { Route } from 'src/app/model/route.model';
import { Utils } from 'src/app/model/utils/Utils';
import { RouteService } from 'src/app/services/route.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-route-view',
  templateUrl: './route-view.component.html',
  styleUrls: ['./route-view.component.css'],
})
export class RouteViewComponent implements OnInit {

  public title: string;
  public obj: Route;
  public customers: RouteCustomer[];
  public currentId: string;
  public utils: Utils = new Utils();
  public showLoading = false;

  public breadcrumbItems: Array<PoBreadcrumbItem> = [
    { label: 'Painel', link: '/dashboard' },
    { label: 'Rotas', link: '/route' },
    { label: 'View', link: '' },
  ];

  public columns: PoTableColumn[] = [
    { property: 'auxCodigo', label: 'Código', width: '10%' },
    { property: 'auxNomeFantasia', label: 'Cliente' },
    { property: 'auxAddress', label: 'Endereço' },
  ];

  constructor(
    private activatedRoute: ActivatedRoute,
    private routeService: RouteService,
    private router: Router,
    private toast: ToastrService,
    private poAlert: PoDialogService,
  ) { }

  public ngOnInit() {
    this.obj = new Route();
    this.activatedRoute.queryParams.subscribe((params) => {
      if (params) {
        this.title = 'Rota #'.concat(params.code);
        this.currentId = params.idRoute;
        this.view(this.currentId);
      }
    });
  }

  public view(idRoute: string) {
    this.showLoading = true;
    this.routeService.findOne(idRoute).subscribe((response) => {
      this.setModel(response);
      this.showLoading = false;
    }, () => {
      this.showLoading = false;
    });
  }

  public setModel(route: Route) {
    this.obj = route;
    this.obj.auxAtivo = this.utils.translateStatus(route.ativo);
    this.obj.ultimaAlteracao = this.utils.formatterDate(route.ultimaAlteracao);
    this.customers = this.feedTable(route.Clientes);
  }

  public feedTable(customers: RouteCustomer[]) {
    customers.forEach((element) => {
      element.auxNomeFantasia = element.Cliente.nomeFantasia;
      element.auxCodigo = element.Cliente.codigo;
      element.auxAddress = element.Cliente.cep.concat(' - ')
        .concat(element.Cliente.endereco).concat(' , ').concat(element.Cliente.bairro).concat(' - ').concat(element.Cliente.Cidade.nome);
    });
    return customers;
  }

  public delete() {
    this.poAlert.confirm({
      title: 'Excluir Registro',
      message: 'Ao clicar em confirmar, o sistema irá excluir definitivamente o registro',
      confirm: () => {
        this.routeService.delete(this.obj.routeId).subscribe((response) => {
          this.toast.success('Removido com sucesso!', 'Registro: ' + this.obj.routeId);
          this.returnListRoute();
        }, () => {
          this.toast.error('Ocorreu um erro ao tentar remover o registro!', 'Registro: ' + this.obj.routeId);
        });
      },
      cancel: () => {

      },
    });
  }

  public cancel() {
    this.returnListRoute();
  }

  public returnListRoute() {
    this.router.navigate(['app/route']);
  }

}
