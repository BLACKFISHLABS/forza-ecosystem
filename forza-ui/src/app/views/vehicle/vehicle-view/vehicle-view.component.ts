import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PoBreadcrumbItem, PoDialogService } from '@po-ui/ng-components';
import { ToastrService } from 'ngx-toastr';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { Vehicle } from 'src/app/model/vehicle.model';
import { VehicleService } from 'src/app/services/vehicle.service';

@Component({
  selector: 'app-vehicle-view',
  templateUrl: './vehicle-view.component.html',
  styleUrls: ['./vehicle-view.component.css'],
})
export class VehicleViewComponent implements OnInit {

  public title: string;
  public obj: Vehicle;
  public currentId: string;

  public breadcrumbItems: Array<PoBreadcrumbItem> = [
    { label: 'Painel', link: '/dashboard' },
    { label: 'Veículos', link: '/vehicle' },
    { label: 'View', link: '' },
  ];

  constructor(
    private activatedRoute: ActivatedRoute,
    private vehicleService: VehicleService,
    private loader: NgxUiLoaderService,
    private route: Router,
    private toast: ToastrService,
    private poAlert: PoDialogService,
  ) { }

  public ngOnInit() {
    this.obj = new Vehicle();
    this.activatedRoute.queryParams.subscribe((params) => {
      if (params) {
        this.title = 'Veículo #'.concat(params.id);
        this.currentId = params.id;
        this.view(this.currentId);
      }
    });
  }

  public view(idVehicle: string) {
    this.loader.startBackground();
    this.vehicleService.findOne(idVehicle).subscribe((response) => {
      this.setModel(response);
      this.loader.stopBackground();
    }, () => {
      this.loader.stopBackground();
    });
  }

  public setModel(vehicle: Vehicle) {
    this.obj = vehicle;
    this.obj.auxVendedor = '';
    vehicle.Vendedor.forEach((element) => {
      if (element !== undefined) {
        this.obj.auxVendedor += element.nome.concat('\n');
      }
    });
  }

  public delete() {
    this.poAlert.confirm({
      title: 'Excluir Registro',
      message: 'Ao clicar em confirmar, o sistema irá excluir definitivamente o registro',
      confirm: () => {
        this.vehicleService.delete(this.obj.id).subscribe((response) => {
          this.toast.success('Removido com sucesso!', 'Registro: ' + this.obj.id);
          this.returnListVehicle();
        }, () => {
          this.toast.error('Ocorreu um erro ao tentar remover o registro!', 'Registro: ' + this.obj.id);
        });
      },
      cancel: () => {

      },
    });
  }

  public edit() {
    this.route.navigate(['app/vehicle/edit'], { queryParams: { id: this.obj.id } });
  }

  public cancel() {
    this.returnListVehicle();
  }

  public returnListVehicle() {
    this.route.navigate(['app/vehicle']);
  }
}
