import { Company } from './company.model';

export class SalesMan {
    public Empresas: Company[];
    public aplicaDesconto: boolean;
    public ativo: boolean;
    public codigo: string;
    public cpfCnpj: string;
    public email: string;
    public idVendedor: number;
    public nome: string;
    public senha: string;
    public telefone: string;
    public ultimaAlteracao: string;

    public error: boolean;
    public mensagem: string;

    // aux
    public auxAtivo: string;
    public auxAplicaDesconto: string;
}
