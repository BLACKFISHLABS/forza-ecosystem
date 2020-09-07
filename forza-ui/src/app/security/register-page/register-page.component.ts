import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { PoPageEditLiterals } from '@po-ui/ng-components';
import { ToastrService } from 'ngx-toastr';
import { Address } from 'src/app/model/adress.model';
import { City } from 'src/app/model/city.model';
import { Company } from 'src/app/model/company.model';
import { Contact } from 'src/app/model/contact.model';
import { DadosReceita } from 'src/app/model/dados-receita.model';
import { Phone } from 'src/app/model/phone.model';
import { ViaCEP } from 'src/app/model/via-cep.model';
import { CompanyService } from 'src/app/services/company.service';
import { ReceitaService } from 'src/app/services/receita.service';
import { ViaCEPService } from 'src/app/services/via-cep.service';
declare var $: any;

@Component({
    selector: 'app-register-page',
    templateUrl: './register-page.component.html',
    styleUrls: ['./register-page.component.css'],
})
export class RegisterPageComponent implements OnInit {

    public form: FormGroup;
    public fantasyName: string;
    public street: string;
    public city: string;
    public globalReceita: DadosReceita;
    public globalViaCEP: ViaCEP;
    public loadingCNPJ = false;
    public showLoading = false;

    public customLiterals: PoPageEditLiterals = {
        cancel: 'Voltar',
        save: 'Confirmar e Avançar',
    };

    constructor(
        private toast: ToastrService,
        private router: Router,
        private formBuilder: FormBuilder,
        private receitaService: ReceitaService,
        private companyService: CompanyService,
        private viaCEPService: ViaCEPService,
    ) { }

    public ngOnInit() {
        $('.po-page').removeClass();
        $('#poMenu').hide();
        $('#poToolbar').hide();
        $('#CNPJInfo').hide();

        this.form = this.formBuilder.group({
            companyName: ['', [Validators.required]],
            phone: ['', [Validators.required]],
            document: ['', [Validators.required]],
        });
    }

    public getCNPJ() {
        this.loadingCNPJ = true;
        const cnpj = this.form.get('document').value;

        if (!cnpj) {
            this.toast.error('Passe o numero do cnpj valido para pesquisar!');
            this.showLoading = false;
            return;
        }

        this.receitaService.getCNPJ(this.replaceCNPJ(cnpj)).subscribe((receita) => {
            if (receita) {
                this.globalReceita = receita;
                this.fantasyName = receita.fantasia;
                this.street = receita.logradouro.concat(', ').concat(receita.numero).concat(', ').concat(receita.bairro);
                this.city = receita.cep.concat(' - ').concat(receita.municipio).concat(' - ').concat(receita.bairro);
                this.showLoading = false;
                $('#CNPJInfo').show();
                this.loadingCNPJ = false;
            } else {
                this.toast.error('Erro ao buscar CNPJ');
                this.loadingCNPJ = false;
            }
        }, () => {
            this.showLoading = false;
            this.loadingCNPJ = false;
        });
    }

    public async save() {
        if (this.globalReceita && this.form.valid) {
            this.showLoading = true;
            const company = await this.mountModel();
            this.companyService.create(company).subscribe(
                () => {
                    this.toast.success('Empresa: ' + company.companyName + ' - ' + ' registrado com sucesso!');
                    this.goRegisterUser(company.cnpj);
                    this.showLoading = false;
                },
                (err) => {
                    this.toast.error('Erro ao cadastrar empresa: ' + err.error.message);
                    this.showLoading = false;
                },
            );
        } else {
            this.toast.error('Preencha seus dados e CNPJ!');
        }
    }

    public goRegisterUser(id: string) {
        this.router.navigate(['userpublic'], { queryParams: { cnpj: id } });
    }

    public async mountModel() {
        this.showLoading = true;
        const company = new Company();
        company.nome = this.globalReceita.fantasia;
        company.companyName = this.globalReceita.nome;
        company.cnpj = this.form.get('document').value;

        const contact = new Contact();
        contact.name = this.form.get('companyName').value;

        const phoneJson = new Phone();
        phoneJson.phoneNumber = this.form.get('phone').value;
        phoneJson.phoneType = 'PHONE';
        contact.phoneJson = phoneJson;

        company.contactJson = contact;
        company.companyType = 'COMPANY';

        const addressJson = new Address();
        addressJson.street = this.globalReceita.logradouro;
        addressJson.number = this.globalReceita.numero;
        addressJson.neighborhood = this.globalReceita.bairro;
        addressJson.complement = this.globalReceita.complemento;
        addressJson.cep = this.globalReceita.cep;

        const viaCEP = await this.getCEP(this.globalReceita.cep);

        const city = new City();
        city.id = '';
        city.codMunicipio = viaCEP.ibge;
        addressJson.cityJson = city;
        company.addressJson = addressJson;

        return company;
    }

    public async getCEP(cep: string) {
        return await this.viaCEPService.getCEP(cep).toPromise();
    }

    public cancel() {
        this.router.navigate(['login']);
    }

    public replaceCNPJ(document: string) {
        return document.replace('/./g', '').replace('/-/g', '').replace('/\//g', '');
    }

}
