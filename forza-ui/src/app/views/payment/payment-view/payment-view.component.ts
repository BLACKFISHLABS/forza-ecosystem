import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PoBreadcrumbItem, PoDialogService } from '@po-ui/ng-components';
import { ToastrService } from 'ngx-toastr';
import { Payment } from 'src/app/model/payment.model';
import { Utils } from 'src/app/model/utils/Utils';
import { PaymentService } from 'src/app/services/payment.service';

@Component({
  selector: 'app-payment-view',
  templateUrl: './payment-view.component.html',
  styleUrls: ['./payment-view.component.css'],
})
export class PaymentViewComponent implements OnInit {

  public title: string;
  public obj: Payment;
  public currentId: string;
  public utils: Utils = new Utils();
  public showLoading = false;

  public breadcrumbItems: Array<PoBreadcrumbItem> = [
    { label: 'Painel', link: '/dashboard' },
    { label: 'Formas de Pagamento', link: '/payment' },
    { label: 'View', link: '' },
  ];

  constructor(
    private activatedRoute: ActivatedRoute,
    private paymentService: PaymentService,
    private route: Router,
    private toast: ToastrService,
    private poAlert: PoDialogService,
  ) { }

  public ngOnInit() {
    this.obj = new Payment();
    this.activatedRoute.queryParams.subscribe((params) => {
      if (params) {
        this.title = 'Forma de Pagamento #'.concat(params.idFormPgto);
        this.currentId = params.idFormPgto;
        this.view(this.currentId);
      }
    });
  }

  public view(idPayment: string) {
    this.showLoading = true;
    this.paymentService.findOne(idPayment).subscribe((response) => {
      this.setModel(response);
      this.showLoading = false;
    }, () => {
      this.showLoading = false;
    });
  }

  public setModel(payment: Payment) {
    this.obj = payment;
    this.obj.auxPercDesc = payment.perDesc.toString();
    this.obj.auxAtivo = this.utils.translateStatusForString(payment.ativo);
    this.obj.ultimaAlteracao = this.utils.formatterDate(payment.ultimaAlteracao);
  }

  public delete() {
    this.poAlert.confirm({
      title: 'Excluir Registro',
      message: 'Ao clicar em confirmar, o sistema irá excluir definitivamente o registro',
      confirm: () => {
        this.paymentService.delete(this.obj.idFormPgto).subscribe((response) => {
          this.toast.success('Removido com sucesso!', 'Registro: ' + this.obj.idFormPgto);
          this.returnListPayment();
        }, () => {
          this.toast.error('Ocorreu um erro ao tentar remover o registro!', 'Registro: ' + this.obj.idFormPgto);
        });
      },
      cancel: () => {

      },
    });
  }

  public edit() {
    this.route.navigate(['app/payment/edit'], { queryParams: { code: this.obj.codigo, idFormPgto: this.obj.idFormPgto } });
  }

  public cancel() {
    this.returnListPayment();
  }

  public returnListPayment() {
    this.route.navigate(['app/payment']);
  }
}
