import { Injectable } from '@angular/core';

export const enum PasswordCheckStrength {
    Nul = 'Senha em Branco',
    Short = 'Senha digitada curta',
    Common = 'Senha digitada muito comum',
    Weak = 'Senha digitada fraca',
    Ok = 'Senha digitada boa',
    Strong = 'Senha digitada forte',
}
@Injectable({
    providedIn: 'root',
})
export class PasswordCheckService {
    public static get MinimumLength(): number {
        return 5;
    }

    private commonPasswordPatterns = /passw.*|12345.*|09876.*|qwert.*|asdfg.*|zxcvb.*|footb.*|baseb.*|drago.*/;

    public isPasswordCommon(password: string): boolean {
        return this.commonPasswordPatterns.test(password);
    }

    public checkPasswordStrength(password: string): PasswordCheckStrength {
        let numberOfElements = 0;
        numberOfElements = /.*[a-z].*/.test(password) ? ++numberOfElements : numberOfElements;      // Lowercase letters
        numberOfElements = /.*[A-Z].*/.test(password) ? ++numberOfElements : numberOfElements;      // Uppercase letters
        numberOfElements = /.*[0-9].*/.test(password) ? ++numberOfElements : numberOfElements;      // Numbers
        numberOfElements = /[^a-zA-Z0-9]/.test(password) ? ++numberOfElements : numberOfElements;   // Special characters (inc. space)

        let currentPasswordStrength = PasswordCheckStrength.Short;

        if (password.length === 0) {
            currentPasswordStrength = PasswordCheckStrength.Nul;
        } else if (password === null || password.length < PasswordCheckService.MinimumLength) {
            currentPasswordStrength = PasswordCheckStrength.Short;
        } else if (this.isPasswordCommon(password) === true) {
            currentPasswordStrength = PasswordCheckStrength.Common;
        } else if (numberOfElements === 0 || numberOfElements === 1 || numberOfElements === 2) {
            currentPasswordStrength = PasswordCheckStrength.Weak;
        } else if (numberOfElements === 3) {
            currentPasswordStrength = PasswordCheckStrength.Ok;
        } else {
            currentPasswordStrength = PasswordCheckStrength.Strong;
        }

        return currentPasswordStrength;
    }
}
