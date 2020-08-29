import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import * as moment from 'moment';
import 'moment/locale/pt-br';
import { ToastrService } from 'ngx-toastr';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { Payment } from 'src/app/model/payment.model';
import { SalesMan } from 'src/app/model/salesman.model';
import { Vehicle } from 'src/app/model/vehicle.model';
import { PaymentService } from 'src/app/services/payment.service';
declare var $: any;

@Component({
    selector: 'app-payment-form',
    templateUrl: './payment-form.component.html',
    styleUrls: ['./payment-form.component.css'],
})
export class PaymentFormComponent implements OnInit {

    public form: FormGroup;
    public title: string;
    public currentId: string;
    public saveButtonVerify = false;

    public userLogged = JSON.parse(localStorage.getItem('authDetails'));

    constructor(
        private loader: NgxUiLoaderService,
        private formBuilder: FormBuilder,
        private paymentService: PaymentService,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private toast: ToastrService,
    ) { }

    public ngOnInit() {
        this.form = this.formBuilder.group({
            code: ['', [Validators.required]],
            description: ['', [Validators.required]],
            discount: ['0,00', [Validators.required]],
            status: ['', [Validators.required]],
        });

        this.form.get('status').setValue(true);

        this.activatedRoute.queryParams.subscribe((params) => {
            if (params.idFormPgto) {
                this.title = 'Forma de Pagamento #'.concat(params.idFormPgto);
                this.currentId = params.idFormPgto;
                this.edit(params.idFormPgto);
            } else {
                this.title = 'Nova Forma de Pagamento';
            }
        });
    }

    public edit(idFormPgto: number) {
        this.loader.startBackground();
        this.paymentService.findOne(idFormPgto).subscribe((response) => {
            this.setModel(response);
            this.loader.stopBackground();
        }, () => {
            this.loader.stopBackground();
        });
    }

    public setModel(payment: Payment) {
        this.form.get('code').setValue(payment.codigo);
        this.form.get('description').setValue(payment.descricao);
        this.form.get('discount').setValue(payment.perDesc);
        this.form.get('status').setValue(payment.ativo);
    }

    public mountModel() {
        this.loader.startBackground();
        const payment = new Payment();
        payment.descricao = this.form.get('description').value;
        payment.perDesc = parseFloat(this.form.get('discount').value);
        payment.codigo = this.form.get('code').value;
        payment.ativo = this.form.get('status').value;
        payment.ultimaAlteracao = moment(new Date()).format();
        payment.idEmpresa = this.userLogged.company.idEmpresa;
        return payment;
    }

    public save() {
        this.saveButtonVerify = true;
        const payment = this.mountModel();
        if (this.currentId) {
            payment.idFormPgto = parseInt(this.currentId, 0);
            this.paymentService.edit(payment).subscribe(
                () => {
                    this.toast.success('Forma de Pagamento: ' + payment.descricao + ' - ' + ' editado com sucesso!');
                    this.loader.stopBackground();
                    this.returnListPayment();
                },
                (err) => {
                    this.setMessageError(err);
                },
            );
        } else {
            this.paymentService.create(payment).subscribe(
                () => {
                    this.toast.success('Forma de Pagamento: ' + payment.descricao + ' - ' + ' criado com sucesso!');
                    this.loader.stopBackground();
                    this.returnListPayment();
                },
                (err) => {
                    this.setMessageError(err);
                },
            );
        }
    }

    public setMessageError(err: any) {
        this.toast.error('Erro ao criar Forma de Pagamento: ' + err.error.message);
        this.loader.stopBackground();
    }

    public cancel() {
        this.returnListPayment();
    }

    public returnListPayment() {
        this.router.navigate(['app/payment']);
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
