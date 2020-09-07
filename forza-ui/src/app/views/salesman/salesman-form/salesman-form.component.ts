import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { SalesMan } from 'src/app/model/salesman.model';
import { SalesManService } from 'src/app/services/salesman.service';
declare var $: any;

@Component({
    selector: 'app-salesman-form',
    templateUrl: './salesman-form.component.html',
    styleUrls: ['./salesman-form.component.css'],
})
export class SalesManFormComponent implements OnInit {

    public form: FormGroup;
    public title: string;
    public currentId: string;
    public saveButtonVerify = false;
    public showLoading = false;

    public userLogged = JSON.parse(localStorage.getItem('authDetails'));

    constructor(
        private formBuilder: FormBuilder,
        private salesmanService: SalesManService,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private toast: ToastrService,
    ) { }

    public ngOnInit() {
        this.form = this.formBuilder.group({
            code: ['', [Validators.required]],
            description: ['', [Validators.required]],
            cpf: ['', [Validators.required]],
            password: ['', [Validators.required]],
            email: ['', []],
            phone: ['', [Validators.required]],
            applyDiscount: ['', []],
            active: ['', []],
        });

        this.form.get('applyDiscount').setValue(true);
        this.form.get('active').setValue(true);

        this.activatedRoute.queryParams.subscribe((params) => {
            if (params.id) {
                this.title = 'Vendedor #'.concat(params.id);
                this.currentId = params.id;
                this.edit(params.id);
            } else {
                this.title = 'Novo Vendedor';
            }
        });
    }

    public edit(idSalesman: number) {
        this.showLoading = true;
        this.salesmanService.findOne(idSalesman).subscribe((response) => {
            this.form.get('code').setValue(response.codigo);
            this.form.get('description').setValue(response.nome);
            this.form.get('cpf').setValue(response.cpfCnpj);
            this.form.get('password').setValue(response.senha);
            this.form.get('email').setValue(response.email);
            this.form.get('phone').setValue(response.telefone);

            this.form.get('applyDiscount').setValue(response.aplicaDesconto);
            this.form.get('active').setValue(response.ativo);

            this.showLoading = false;
        }, () => {
            this.showLoading = false;
        });
    }

    public cancel() {
        this.returnListSalesman();
    }

    public returnListSalesman() {
        this.router.navigate(['app/salesman']);
    }

    public mountModel() {
        this.showLoading = true;
        const salesman = new SalesMan();
        salesman.codigo = this.form.get('code').value;
        salesman.nome = this.form.get('description').value;
        salesman.cpfCnpj = this.form.get('cpf').value;
        salesman.senha = this.form.get('password').value;
        salesman.email = this.form.get('email').value;
        salesman.telefone = this.form.get('phone').value;
        salesman.aplicaDesconto = this.form.get('applyDiscount').value;
        salesman.ativo = this.form.get('active').value;

        salesman.Empresas = [this.userLogged.company];

        return salesman;
    }

    public save() {
        this.saveButtonVerify = true;
        const salesman = this.mountModel();
        if (this.currentId) {
            salesman.idVendedor = parseInt(this.currentId, 0);
            this.salesmanService.edit(salesman).subscribe(
                () => {
                    this.toast.success('Vendedor: ' + salesman.nome + ' - ' + ' editado com sucesso!');
                    this.showLoading = false;
                    this.returnListSalesman();
                },
                (err) => {
                    this.toast.error('Erro ao editar vendedor: ' + err.error.message);
                    this.showLoading = false;
                },
            );
        } else {
            this.salesmanService.create(salesman).subscribe(
                () => {
                    this.toast.success('Vendedor: ' + salesman.nome + ' - ' + ' criado com sucesso!');
                    this.showLoading = false;
                    this.returnListSalesman();
                },
                (err) => {
                    this.toast.error('Erro ao criar vendedor: ' + err.error.message);
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
