import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { Company } from 'src/app/model/company.model';
import { Profile } from 'src/app/model/profile.model';
import { User } from 'src/app/model/user.model';
import { CompanyService } from 'src/app/services/company.service';
import { UserService } from 'src/app/services/user.service';
import { PasswordCheckService, PasswordCheckStrength } from './password-check.service';
declare var $: any;

@Component({
    selector: 'app-user-page',
    templateUrl: './user-page.component.html',
    styleUrls: ['./user-page.component.css'],
})
export class UserPublicComponent implements OnInit {

    public form: FormGroup;
    public strongPassword: string;
    public confirmPassword: string;
    public labelPass = 'Senhas Não Correspondem';
    public globalCompany: Company;
    public firstNameDisable: boolean;
    public secondNameDisable: boolean;

    constructor(
        private toast: ToastrService,
        private router: Router,
        private loader: NgxUiLoaderService,
        private formBuilder: FormBuilder,
        private userService: UserService,
        private activatedRoute: ActivatedRoute,
        private checkPasswordService: PasswordCheckService,
        private companyService: CompanyService,
    ) { }

    public ngOnInit() {
        $('#poMenu').hide();
        $('#poToolbar').hide();

        this.form = this.formBuilder.group({
            firstName: ['', [Validators.required]],
            lastName: ['', [Validators.required]],
            email: ['', [Validators.required]],
            password: ['', [Validators.required]],
            passwordConfirm: ['', [Validators.required]],
        });

        this.activatedRoute.queryParams.subscribe((params) => {
            if (params.cnpj) {
                this.findCompanyByCNPJ(params.cnpj);
            }
        });

        this.checkPasswordStrength();
        this.checkPass();
    }

    public findCompanyByCNPJ(cnpj: string) {
        this.companyService.getSearch(cnpj).subscribe((companys) => {
            companys.forEach((element) => {
                if (element.cnpj === cnpj) {
                    this.globalCompany = element;
                }
            });
        });
    }

    public checkPasswordStrength() {
        const password = this.form.get('password').value;
        this.strongPassword = PasswordCheckStrength.Nul.toString();
        if (password) {
            this.strongPassword = this.checkPasswordService.checkPasswordStrength(password).toString();
        }
    }

    public checkPass() {
        if (!this.form.get('passwordConfirm').value) {
            this.labelPass = 'Senha não inserida';
        } else if (this.form.get('password').value === this.form.get('passwordConfirm').value) {
            this.labelPass = 'Senha Correta';
        } else {
            this.form.setErrors({ invalid: true });
            this.labelPass = 'Senha Incorreta';
        }
    }
    public save() {
        this.loader.startBackground();
        const user = this.mountModel();
        this.userService.create(user).subscribe(
            () => {
                this.toast.success('Usuário: ' + user.email + ' - ' + ' registrado com sucesso!');
                this.loader.stopBackground();
                this.returnLogin();
            },
            (error) => {
                const dup = 'constraint ["PUBLIC.UK_USER_EMAIL_INDEX';
                if (error.error.message.indexOf(dup) > -1) {
                    this.toast.info('Email já cadastrado!');
                } else {
                    this.toast.error('Erro ao cadastrar usuário: ' + error.error.message);
                }
                this.loader.stopBackground();
            },
        );
    }

    public returnLogin() {
        this.router.navigate(['login']);
    }

    public cancel() {
        this.router.navigate(['login']);
    }

    public mountModel() {
        this.loader.startBackground();
        const user = new User();

        user.profiles = this.getDefaultProfile();
        user.email = this.form.get('email').value;
        user.firstName = this.form.get('firstName').value;
        user.lastName = this.form.get('lastName').value;
        user.password = this.form.get('password').value;
        user.companyJson = this.globalCompany;
        user.ssoId = this.makeId();
        user.state = 'Active';

        return user;
    }

    public makeId() {
        let text = '';
        const possible = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
        for (let i = 0; i < 20; i++) {
            text += possible.charAt(Math.floor(Math.random() * possible.length));
        }
        return text;
    }

    public getDefaultProfile() {
        const profiles = [];
        const profile = new Profile();
        profile.id = 3;
        profile.role = 'ROLE_MANAGER';
        profiles.push(profile);
        return profiles;
    }

}
