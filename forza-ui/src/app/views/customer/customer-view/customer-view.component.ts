import { Component, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { PoBreadcrumbItem, PoDialogService } from '@po-ui/ng-components';
import { ToastrService } from 'ngx-toastr';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { Customer } from 'src/app/model/customer.model';
import { Utils } from 'src/app/model/utils/Utils';
import { CustomerService } from 'src/app/services/customer.service';

@Component({
  selector: 'app-customer-view',
  templateUrl: './customer-view.component.html',
  styleUrls: ['./customer-view.component.css'],
})
export class CustomerViewComponent implements OnInit {

  public title: string;
  public obj: Customer;
  public form: FormGroup;
  public currentId: string;
  public utils: Utils = new Utils();

  public breadcrumbItems: Array<PoBreadcrumbItem> = [
    { label: 'Painel', link: '/dashboard' },
    { label: 'Clientes', link: '/customer' },
    { label: 'View', link: '' },
  ];

  constructor(
    private activatedRoute: ActivatedRoute,
    private customerService: CustomerService,
    private loader: NgxUiLoaderService,
    private router: Router,
    private toast: ToastrService,
    private poAlert: PoDialogService,
  ) { }

  public ngOnInit() {
    this.obj = new Customer();
    this.activatedRoute.queryParams.subscribe((params) => {
      if (params) {
        this.title = 'Cliente #'.concat(params.code);
        this.currentId = params.idCustomer;
        this.view(this.currentId);
      }
    });
  }

  public view(idPriceTable: string) {
    this.loader.startBackground();
    this.customerService.findOne(idPriceTable).subscribe((response) => {
      this.setModel(response);
      this.loader.stopBackground();
    }, () => {
      this.loader.stopBackground();
    });
  }

  public setModel(customer: Customer) {
    this.obj = customer;
    this.obj.auxCity = customer.Cidade.nome;
    this.obj.auxAtivo = this.utils.translateStatus(customer.ativo);
    this.obj.ultimaAlteracao = this.utils.formatterDate(customer.ultimaAlteracao);
    this.obj.auxTypePerson = customer.tipo === 1 ? 'PESSOA JURIDICA' : 'PESSOA FISICA';
  }

  public delete() {
    this.poAlert.confirm({
      title: 'Excluir Registro',
      message: 'Ao clicar em confirmar, o sistema irá excluir definitivamente o registro',
      confirm: () => {
        this.customerService.delete(this.obj.idCliente).subscribe((response) => {
          this.toast.success('Removido com sucesso!', 'Registro: ' + this.obj.idCliente);
          this.returnListCustomer();
        }, () => {
          this.toast.error('Ocorreu um erro ao tentar remover o registro!', 'Registro: ' + this.obj.idCliente);
        });
      },
      cancel: () => {

      },
    });
  }

  public cancel() {
    this.returnListCustomer();
  }

  public returnListCustomer() {
    this.router.navigate(['app/customer']);
  }

}
