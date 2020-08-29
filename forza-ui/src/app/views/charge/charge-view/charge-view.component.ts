import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PoBreadcrumbItem, PoDialogService } from '@po-ui/ng-components';
import { ToastrService } from 'ngx-toastr';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { Charge } from 'src/app/model/charge.model';
import { Utils } from 'src/app/model/utils/Utils';
import { ChargeService } from 'src/app/services/charge.service';

@Component({
  selector: 'app-charge-view',
  templateUrl: './charge-view.component.html',
  styleUrls: ['./charge-view.component.css'],
})
export class ChargeViewComponent implements OnInit {

  public title: string;
  public obj: Charge;
  public currentId: string;
  public utils: Utils = new Utils();

  public breadcrumbItems: Array<PoBreadcrumbItem> = [
    { label: 'Painel', link: '/dashboard' },
    { label: 'Resumo', link: '/charge' },
    { label: 'View', link: '' },
  ];

  constructor(
    private activatedRoute: ActivatedRoute,
    private chargeService: ChargeService,
    private loader: NgxUiLoaderService,
    private router: Router,
    private toast: ToastrService,
    private poAlert: PoDialogService,
  ) { }

  public ngOnInit() {
    this.obj = new Charge();
    this.activatedRoute.queryParams.subscribe((params) => {
      if (params) {
        this.title = params.code;
        this.currentId = params.idCharge;
        this.view(this.currentId);
      }
    });
  }

  public view(idCharge: string) {
    this.loader.startBackground();
    this.chargeService.findOne(idCharge).subscribe((response) => {
      this.setModel(response);
      this.loader.stopBackground();
    }, () => {
      this.loader.stopBackground();
    });
  }

  public setModel(charge: Charge) {
    this.obj = charge;
    this.obj.auxVehicle = charge.veiculo.placa;
    this.obj.auxVendedor = charge.vendedor;
    this.obj.emissao = this.utils.formatterDate(charge.emissao);
    this.obj.auxStatus = this.translateChargeStatus(charge.status);
  }

  public translateChargeStatus(status: number) {
    switch (status) {
      case 0:
        return 'Criado';
        break;
      case 1:
        return 'Sincronizado';
        break;
      case 2:
        return 'Finalizado';
        break;
      default:
        return '';
        break;
    }
  }

  public delete() {
    this.poAlert.confirm({
      title: 'Excluir Registro',
      message: 'Ao clicar em confirmar, o sistema irá excluir definitivamente o registro',
      confirm: () => {
        this.chargeService.delete(this.obj.id).subscribe((response) => {
          this.toast.success('Removido com sucesso!', 'Registro: ' + this.obj.id);
          this.returnListCharge();
        }, () => {
          this.toast.error('Ocorreu um erro ao tentar remover o registro!', 'Registro: ' + this.obj.id);
        });
      },
      cancel: () => {

      },
    });
  }

  public cancel() {
    this.returnListCharge();
  }

  public returnListCharge() {
    this.router.navigate(['app/charge']);
  }

}
