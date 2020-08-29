import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { SalesMan } from 'src/app/model/salesman.model';
import { Vehicle } from 'src/app/model/vehicle.model';
import { SalesManService } from 'src/app/services/salesman.service';
import { VehicleService } from 'src/app/services/vehicle.service';
import { isNullOrUndefined } from 'util';
import { ComboSalesmanService } from '../../combo-salesman.service';
declare var $: any;

@Component({
    selector: 'app-vehicle-form',
    templateUrl: './vehicle-form.component.html',
    styleUrls: ['./vehicle-form.component.css'],
    providers: [ComboSalesmanService],
})
export class VehicleFormComponent implements OnInit {

    public vehicles: Vehicle[];
    public form: FormGroup;
    public title: string;
    public currentId: string;
    public saveButtonVerify = false;
    public salesmanSelected: SalesMan;

    constructor(
        private loader: NgxUiLoaderService,
        private formBuilder: FormBuilder,
        private vehicleService: VehicleService,
        private salesmanService: SalesManService,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private toast: ToastrService,
        public comboSalesmanService: ComboSalesmanService,
    ) { }

    public ngOnInit() {
        this.form = this.formBuilder.group({
            description: ['', [Validators.required]],
            plate: ['', [Validators.required]],
            driveSalesman: ['', [Validators.required]],
        });

        this.activatedRoute.queryParams.subscribe((params) => {
            if (params.id) {
                this.title = 'Veículo #'.concat(params.id);
                this.currentId = params.id;
                this.edit(params.id);
            } else {
                this.title = 'Novo Veículo';
            }
        });
    }

    public edit(idVehicle: number) {
        this.loader.startBackground();
        this.vehicleService.findOne(idVehicle).subscribe((response) => {
            this.setModel(response);
            this.loader.stopBackground();
        }, () => {
            this.loader.stopBackground();
        });
    }

    public setModel(vehicle: Vehicle) {
        this.form.get('description').setValue(vehicle.description);
        this.form.get('plate').setValue(vehicle.plate);
        this.form.get('driveSalesman').setValue(vehicle.Vendedor[0].idVendedor);
        this.changeComboSalesman();
    }

    public cancel() {
        this.returnListVehicle();
    }

    public returnListVehicle() {
        this.router.navigate(['app/vehicle']);
    }

    public mountModel() {
        this.loader.startBackground();
        const vehicle = new Vehicle();
        vehicle.description = this.form.get('description').value;
        vehicle.plate = this.form.get('plate').value;
        vehicle.Vendedor = [this.salesmanSelected];

        return vehicle;
    }

    public save() {
        this.saveButtonVerify = true;
        const vehicle = this.mountModel();
        if (this.currentId) {
            vehicle.id = parseInt(this.currentId, 0);
            this.vehicleService.edit(vehicle).subscribe(
                () => {
                    this.toast.success('Veículo: ' + vehicle.description + ' - ' + ' editado com sucesso!');
                    this.loader.stopBackground();
                    this.returnListVehicle();
                },
                (err) => {
                    this.toast.error('Erro ao criar veículo: ' + err.error.message);
                    this.loader.stopBackground();
                },
            );
        } else {
            this.vehicleService.create(vehicle).subscribe(
                () => {
                    this.toast.success('Veículo: ' + vehicle.description + ' - ' + ' criado com sucesso!');
                    this.loader.stopBackground();
                    this.returnListVehicle();
                },
                (err) => {
                    this.toast.error('Erro ao criar veículo: ' + err.error.message);
                    this.loader.stopBackground();
                },
            );
        }
    }

    public changeComboSalesman() {
        const salesmanId = this.form.get('driveSalesman').value;
        if (!isNullOrUndefined(salesmanId)) {
            this.salesmanService.findOne(salesmanId).subscribe((res) => {
                this.salesmanSelected = res;
            });
        }
    }

    public validate() {
        let validate = true;
        if (this.saveButtonVerify === true) {
            validate = true;
        } else if (!this.form.valid) {
            validate = true;
        } else {
            validate = false;
        }
        return validate;
    }

}
