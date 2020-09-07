import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { SalesMan } from 'src/app/model/salesman.model';
import { Vehicle } from 'src/app/model/vehicle.model';
import { VehicleService } from 'src/app/services/vehicle.service';
declare var $: any;

@Component({
    selector: 'app-route-form',
    templateUrl: './route-form.component.html',
    styleUrls: ['./route-form.component.css'],
})
export class RouteFormComponent implements OnInit {

    public vehicles: Vehicle[];
    public form: FormGroup;
    public title: string;
    public currentId: string;
    public saveButtonVerify = false;
    public globalDriveSalesMan: SalesMan[];
    public showLoading = false;

    constructor(
        private formBuilder: FormBuilder,
        private vehicleService: VehicleService,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private toast: ToastrService,
    ) { }

    public ngOnInit() {
        this.form = this.formBuilder.group({
            description: ['', [Validators.required]],
            plate: ['', [Validators.required]],
            driveSalesman: ['', []],
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
        this.showLoading = true;
        this.vehicleService.findOne(idVehicle).subscribe((response) => {
            this.form.get('description').setValue(response.description);
            this.form.get('plate').setValue(response.plate);
            this.form.get('driveSalesman').setValue(this.setDriveSalesMans(response.Vendedor));
            this.globalDriveSalesMan = response.Vendedor;
            this.showLoading = false;
        }, () => {
            this.showLoading = false;
        });
    }

    public setDriveSalesMans(list: SalesMan[]) {
        const notes = [];
        for (const element of list) {
            notes.push(element.idVendedor + ' - ' + element.nome + '\n');
        }
        return notes.join(' - ').trim();
    }

    public cancel() {
        this.returnListVehicle();
    }

    public returnListVehicle() {
        this.router.navigate(['app/vehicle']);
    }

    public mountModel() {
        this.showLoading = true;
        const vehicle = new Vehicle();
        vehicle.description = this.form.get('description').value;
        vehicle.plate = this.form.get('plate').value;
        if (this.currentId) {
            vehicle.Vendedor = this.globalDriveSalesMan;
        }
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
                    this.showLoading = false;
                    this.returnListVehicle();
                },
                (err) => {
                    this.toast.error('Erro ao criar veículo: ' + err.error.message);
                    this.showLoading = false;
                },
            );
        } else {
            this.vehicleService.create(vehicle).subscribe(
                () => {
                    this.toast.success('Veículo: ' + vehicle.description + ' - ' + ' criado com sucesso!');
                    this.showLoading = false;
                    this.returnListVehicle();
                },
                (err) => {
                    this.toast.error('Erro ao criar veículo: ' + err.error.message);
                    this.showLoading = false;
                },
            );
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
