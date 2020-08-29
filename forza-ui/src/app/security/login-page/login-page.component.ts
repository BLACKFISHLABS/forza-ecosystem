import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { PoPageLoginLiterals } from '@po-ui/ng-templates';
import { ToastrService } from 'ngx-toastr';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { first } from 'rxjs/operators';
import { Login } from 'src/app/model/login.model';
import { AuthService } from 'src/app/services/auth.service';
declare var $: any;

@Component({
    selector: 'app-login-page',
    templateUrl: './login-page.component.html',
    styleUrls: ['./login-page.component.css'],
})
export class LoginPageComponent implements OnInit {

    public login = new Login();

    public customLiterals: PoPageLoginLiterals = {
        attempts: '{0} vez(es) ',
        createANewPasswordNow: 'Melhor criar uma senha nova agora! Você vai poder entrar no sistema logo em seguida.',
        forgotPassword: 'Esqueceu sua senha?',
        forgotYourPassword: 'Esqueceu sua senha?',
        highlightInfo: '',
        welcome: '',
        iForgotMyPassword: 'Esqueci minha senha',
        ifYouTryHarder: 'Se tentar mais ',
        title: 'Forza - Força de Vendas',
        loginErrorPattern: 'Login obrigatório',
        loginHint: 'Caso não possua um usuário faça seu registro',
        loginLabel: 'Insira seu usuário',
        loginPlaceholder: 'Insira seu usuário de acesso',
        passwordErrorPattern: 'Senha obrigatória',
        passwordLabel: 'Insira sua senha',
        passwordPlaceholder: 'Insira sua senha de acesso',
        customFieldErrorPattern: 'Campo customizado inválido',
        customFieldPlaceholder: 'Por favor insira um valor',
        registerUrl: 'Novo Registro',
        rememberUser: 'Lembrar Usuário',
        rememberUserHint: 'Esta opção pode ser desabilitada nas configurações do sistema',
        submitLabel: 'Entrar no Sistema',
        submittedLabel: 'Carregando...',
        titlePopover: 'Opa!',
        yourUserWillBeBlocked: '',
    };

    constructor(
        private authService: AuthService,
        private toast: ToastrService,
        private router: Router,
        private loader: NgxUiLoaderService,
    ) { }

    public ngOnInit() {
    }

    public loginSubmit(event: any) {
        this.login.username = event.login;
        this.login.password = event.password;
        this.loginIn();
    }

    public loginIn() {
        this.loader.startBackground();
        this.authService.login(this.login)
            .pipe(first())
            .subscribe(async (response) => {
                if (response) {
                    localStorage.setItem('username', this.login.username);
                    localStorage.setItem('password', this.login.password);

                    const authDetails = {
                        name: response.name,
                        authenticated: response.authenticated,
                        permissions: response.permissions,
                        company: response.principal.company,
                    };

                    localStorage.setItem('authDetails', JSON.stringify(authDetails) );

                    this.router.navigate(['/app/dashboard']);
                    this.toast.success('Seja bem vindo!', 'Olá');
                    this.loader.stopBackground();
                } else {
                    this.toast.error('login ou senha inválida!');
                }
            }, () => {
                this.loader.stopBackground();
                this.toast.error('login ou senha inválida!', 'Oops!');
            });
    }
}
