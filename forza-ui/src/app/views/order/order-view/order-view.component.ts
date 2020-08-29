import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PoBreadcrumbItem, PoDialogService, PoTableColumn } from '@po-ui/ng-components';
import { ToastrService } from 'ngx-toastr';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { Item } from 'src/app/model/item.model';
import { Order } from 'src/app/model/order.model';
import { Utils } from 'src/app/model/utils/Utils';
import { OrderService } from 'src/app/services/order.service';

@Component({
  selector: 'app-order-view',
  templateUrl: './order-view.component.html',
  styleUrls: ['./order-view.component.css'],
})
export class OrderViewComponent implements OnInit {

  public title: string;
  public itens: Item[];
  public obj: Order;
  public currentId: string;
  public utils: Utils = new Utils();

  public breadcrumbItems: Array<PoBreadcrumbItem> = [
    { label: 'Painel', link: '/dashboard' },
    { label: 'Vendas', link: '/order' },
    { label: 'View', link: '' },
  ];

  public columns: PoTableColumn[] = [
    { property: 'productCode', label: 'Código', width: '10%' },
    { property: 'produto', label: 'Produto' },
    { property: 'quantidade', label: 'Quantidade' },
    { property: 'prcVenda', label: 'Valor Unitário', type: 'currency', format: 'BRL' },
    { property: 'subTotal', label: 'Sub Total', type: 'currency', format: 'BRL' },
  ];

  constructor(
    private activatedRoute: ActivatedRoute,
    private orderService: OrderService,
    private loader: NgxUiLoaderService,
    private router: Router,
    private toast: ToastrService,
    private poAlert: PoDialogService,
  ) { }

  public ngOnInit() {
    this.obj = new Order();

    this.activatedRoute.queryParams.subscribe((params) => {
      if (params) {
        this.title = 'Venda #'.concat(params.id);
        this.currentId = params.id;
        this.view(this.currentId);
      }
    });
  }

  public delete() {
    this.poAlert.confirm({
      title: 'Excluir Registro',
      message: 'Ao clicar em confirmar, o sistema irá excluir definitivamente o registro',
      confirm: () => {
        this.orderService.delete(this.obj.id).subscribe((response) => {
          this.toast.success('Removido com sucesso!', 'Registro: ' + this.obj.id);
          this.returnListOrder();
        }, () => {
          this.toast.error('Ocorreu um erro ao tentar remover o registro!', 'Registro: ' + this.obj.id);
        });
      },
      cancel: () => {

      },
    });
  }

  public view(idOrder: string) {
    this.loader.startBackground();
    this.orderService.findOne(idOrder).subscribe((response) => {
      this.setModel(response);
      this.loader.stopBackground();
    }, () => {
      this.loader.stopBackground();
    });
  }

  public setModel(order: Order) {
    this.obj = order;
    this.obj.auxType = this.translateType(this.obj.tipo);
    this.obj.auxStatus = this.translateStatus(this.obj.status);
    this.obj.auxVendedor = this.obj.vendedor.nome;
    this.obj.dtEmissao = this.utils.formatterDate(this.obj.dtEmissao);
    this.obj.ultimaAlteracao = this.utils.formatterDate(this.obj.ultimaAlteracao);
    this.itens = order.Itens;
  }

  public translateType(status: number) {
    switch (status) {
      case 0:
        return 'Normal';
        break;
      case 1:
        return 'Devolução';
        break;
      default:
        return '';
        break;
    }
  }

  public translateActive(active: number) {
    return active === 0 ? 'Sim' : 'Não';
  }

  public translateStatus(status: number) {
    switch (status) {
      case 0:
        return 'Criado';
        break;
      case 1:
        return 'Modificado';
        break;
      case 2:
        return 'Sincronizado';
        break;
      case 3:
        return 'Cancelado';
        break;
      case 5:
        return 'Faturado';
        break;
      default:
        return '';
        break;
    }
  }

  public cancel() {
    this.returnListOrder();
  }

  public returnListOrder() {
    this.router.navigate(['app/order']);
  }
}
