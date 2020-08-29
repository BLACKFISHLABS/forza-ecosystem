
export class FormValidations {
    static getErrorMsg(fieldName: string, validatorName: string, validatorValue?: any) {
        const config = {
            required: `${fieldName} é obrigatório!`,
            minlength: `${fieldName} precisa ter no mínimo ${validatorValue.requiredLength} caracteres.`,
            maxlength: `${fieldName} precisa ter no máximo ${validatorValue.requiredLength} caracteres.`,
            min: `${fieldName} precisa ter valor mínimo de ${validatorValue.min}.`,
            max: `${fieldName} precisa ter valor máximo de ${validatorValue.max}.`,
            email: `${fieldName} inválido!`,
            cpfInvalido: `CPF inválido!`,
            cnpjInvalido: `CNPJ inválido!`
        };

        return config[validatorName];
    }
}