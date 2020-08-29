import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { Address } from 'src/app/model/adress.model';
import { City } from 'src/app/model/city.model';
import { Company } from 'src/app/model/company.model';
import { Contact } from 'src/app/model/contact.model';
import { Phone } from 'src/app/model/phone.model';
import { State } from 'src/app/model/state.model';
import { ViaCEP } from 'src/app/model/via-cep.model';
import { CompanyService } from 'src/app/services/company.service';
import { ViaCEPService } from 'src/app/services/via-cep.service';
declare var $: any;

@Component({
    selector: 'app-company-form',
    templateUrl: './company-form.component.html',
    styleUrls: ['./company-form.component.css'],
})
export class CompanyFormComponent implements OnInit {

    public form: FormGroup;
    public title: string;
    public currentId: string;
    public saveButtonVerify = false;
    public globalViaCEP: ViaCEP;
    public globalCompany: Company;

    constructor(
        private loader: NgxUiLoaderService,
        private formBuilder: FormBuilder,
        private companyService: CompanyService,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private toast: ToastrService,
        private viaCEPService: ViaCEPService,
    ) { }

    public ngOnInit() {
        this.form = this.formBuilder.group({
            cnpj: ['', [Validators.required]],
            fantasyName: ['', [Validators.required]],
            name: ['', [Validators.required]],
            cep: ['', [Validators.required]],
            street: ['', [Validators.required]],
            neighborhood: ['', [Validators.required]],
            complement: ['', [Validators.required]],
            number: ['', [Validators.required]],
            city: ['', [Validators.required]],
            state: ['', [Validators.required]],
            cantact: ['', [Validators.required]],
            phone: ['', [Validators.required]],
        });

        this.activatedRoute.queryParams.subscribe((params) => {
            if (params.id) {
                this.title = 'Empresa #'.concat(params.id);
                this.currentId = params.id;
                this.edit(params.id);
            } else {
                this.title = 'Nova Empresa';
            }
        });
    }

    public edit(id: number) {
        this.loader.startBackground();
        this.companyService.findOne(id).subscribe((response) => {
            this.globalCompany = response;
            this.form.get('cnpj').setValue(response.cnpj);
            this.form.get('fantasyName').setValue(response.nome);
            this.form.get('name').setValue(response.companyName);
            this.form.get('cep').setValue(response.addressJson.cep);
            this.form.get('street').setValue(response.addressJson.street);
            this.form.get('neighborhood').setValue(response.addressJson.neighborhood);
            this.form.get('complement').setValue(response.addressJson.complement);
            this.form.get('number').setValue(response.addressJson.number);
            this.form.get('city').setValue(response.addressJson.cityJson.nome);
            this.form.get('state').setValue(response.addressJson.cityJson.Estado.uf);
            this.form.get('cantact').setValue(response.contactJson.name);
            this.form.get('phone').setValue(response.contactJson.phoneJson.phoneNumber);
            this.loader.stopBackground();
        }, () => {
            this.loader.stopBackground();
        });
    }

    public cancel() {
        this.returnDashboard();
    }

    public returnDashboard() {
        this.router.navigate(['app/dashboard']);
    }

    public mountModel() {
        this.loader.startBackground();

        this.globalCompany.cnpj = this.form.get('cnpj').value;
        this.globalCompany.nome = this.form.get('fantasyName').value;
        this.globalCompany.companyName = this.form.get('name').value;

        this.globalCompany.addressJson.cep = this.form.get('cep').value;
        this.globalCompany.addressJson.street = this.form.get('street').value;
        this.globalCompany.addressJson.neighborhood = this.form.get('neighborhood').value;
        this.globalCompany.addressJson.complement = this.form.get('complement').value;
        this.globalCompany.addressJson.number = this.form.get('number').value;

        if (this.globalViaCEP) {
            this.globalCompany.addressJson.cityJson.codMunicipio = this.globalViaCEP.ibge;
        }

        this.globalCompany.contactJson.name = this.form.get('cantact').value;
        this.globalCompany.contactJson.phoneJson.phoneNumber = this.form.get('phone').value;

        return this.globalCompany;
    }

    public save() {
        this.saveButtonVerify = true;
        const company = this.mountModel();
        if (this.currentId) {
            company.idEmpresa = parseInt(this.currentId, 0);
            this.companyService.edit(company).subscribe(
                () => {
                    this.toast.success('Empresa: ' + company.nome + ' - ' + ' editado com sucesso!');
                    this.loader.stopBackground();
                },
                (err) => {
                    this.toast.error('Erro ao editar empresa: ' + err.error.message);
                    this.loader.stopBackground();
                },
            );
        }
    }

    public changeCep() {
        const cep = this.form.get('cep').value;
        if (cep) {
            this.viaCEPService.getCEP(cep).subscribe(
                (viaCEP) => {
                    this.globalViaCEP = viaCEP;
                    this.form.get('street').setValue(viaCEP.logradouro);
                    this.form.get('neighborhood').setValue(viaCEP.bairro);
                    this.form.get('complement').setValue(viaCEP.complemento);

                    this.form.get('city').setValue(viaCEP.localidade);
                    this.form.get('state').setValue(viaCEP.uf);

                    this.toast.success('Retornado busca para o CEP: ' + cep);
                },
                (err) => {
                    this.toast.error('Erro ao buscar CEP: ' + err.error.message);
                    this.loader.stopBackground();
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
