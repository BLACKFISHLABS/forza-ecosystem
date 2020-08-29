import { Address } from './adress.model';
import { Contact } from './contact.model';

export class Company {
    public idEmpresa: number;
    public nome: string;
    public companyName: string;
    public cnpj: string;
    public addressJson: Address;
    public contactJson: Contact;
    public companyType: string;
    public idTabela: number;

    // aux
    public auxCep: string;
    public auxCity: string;
    public auxEstado: string;
    public auxcomplement: string;
    public auxNeighborhood: string;
    public auxNumber: string;
    public auxStreet: string;
    public auxPhoneName: string;
    public auxPhoneNumber: string;
}
