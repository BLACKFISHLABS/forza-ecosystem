import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PoBreadcrumbItem, PoDialogService } from '@po-ui/ng-components';
import { ToastrService } from 'ngx-toastr';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { SalesMan } from 'src/app/model/salesman.model';
import { Utils } from 'src/app/model/utils/Utils';
import { SalesManService } from 'src/app/services/salesman.service';

@Component({
  selector: 'app-salesman-view',
  templateUrl: './salesman-view.component.html',
  styleUrls: ['./salesman-view.component.css'],
})
export class SalesManViewComponent implements OnInit {

  public title: string;
  public obj: SalesMan;
  public currentId: string;
  public utils: Utils = new Utils();

  public breadcrumbItems: Array<PoBreadcrumbItem> = [
    { label: 'Painel', link: '/dashboard' },
    { label: 'Vendedor', link: '/salesman' },
    { label: 'View', link: '' },
  ];

  constructor(
    private activatedRoute: ActivatedRoute,
    private salesManService: SalesManService,
    private loader: NgxUiLoaderService,
    private router: Router,
    private toast: ToastrService,
    private poAlert: PoDialogService,
  ) { }

  public ngOnInit() {
    this.obj = new SalesMan();
    this.activatedRoute.queryParams.subscribe((params) => {
      if (params) {
        this.title = 'Vendedor #'.concat(params.code);
        this.currentId = params.idVendedor;
        this.view(this.currentId);
      }
    });

  }

  public view(idProduct: string) {
    this.loader.startBackground();
    this.salesManService.findOne(idProduct).subscribe((response) => {
      this.setModel(response);
      this.loader.stopBackground();
    }, () => {
      this.loader.stopBackground();
    });
  }

  public setModel(salesman: SalesMan) {
    this.obj = salesman;
    this.obj.auxAtivo = this.utils.translateStatus(salesman.ativo);
    this.obj.ultimaAlteracao = this.utils.formatterDate(salesman.ultimaAlteracao);
    this.obj.auxAplicaDesconto = this.translateApplyDiscount(salesman.aplicaDesconto);
  }

  public translateApplyDiscount(status: boolean) {
    return status ? 'Sim' : 'Não';
  }

  public delete() {
    this.poAlert.confirm({
      title: 'Excluir Registro',
      message: 'Ao clicar em confirmar, o sistema irá excluir definitivamente o registro',
      confirm: () => {
        this.salesManService.delete(this.obj.idVendedor).subscribe((response) => {
          this.toast.success('Removido com sucesso!', 'Registro: ' + this.obj.idVendedor);
          this.returnListSalesMan();
        }, () => {
          this.toast.error('Ocorreu um erro ao tentar remover o registro!', 'Registro: ' + this.obj.idVendedor);
        });
      },
      cancel: () => {

      },
    });
  }

  public edit() {
    this.router.navigate(['app/salesman/edit'], { queryParams: { id: this.obj.idVendedor } });
  }

  public cancel() {
    this.returnListSalesMan();
  }

  public returnListSalesMan() {
    this.router.navigate(['app/salesman']);
  }

}
