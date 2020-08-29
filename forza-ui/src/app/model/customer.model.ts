import { City } from './city.model';

export class Customer {
    public Cidade: City;
    public appKey: number;
    public ativo: boolean;
    public bairro: string;
    public cep: string;
    public codigo: string;
    public complemento: string;
    public comprador: string;
    public contato: string;
    public cpfCnpj: string;
    public email: string;
    public endereco: string;
    public idCliente: number;
    public ie: string;
    public nome: string;
    public nomeFantasia: string;
    public numero: string;
    public tabelaPadrao: string;
    public telefone: string;
    public telefone2: string;
    public tipo: number;
    public ultimaAlteracao: string;

    // aux
    public auxCity: string;
    public auxAtivo: string;
    public auxTypePerson: string;
}
