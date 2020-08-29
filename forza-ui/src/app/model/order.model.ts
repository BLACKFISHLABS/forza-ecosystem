import { Company } from './company.model';
import { Item } from './item.model';
import { SalesMan } from './salesman.model';

export class Order {
    public Itens: Item[];
    public codigoCliente: string;
    public customer: string;
    public desconto: number;
    public dtEmissao: string;
    public empresa: Company;
    public id: number;
    public idCliente: number;
    public idFormPgto: number;
    public idPedido: number;
    public idTabela: number;
    public observacao: string;
    public payment: string;
    public perDesc: number;
    public resumo: string;
    public status: number;
    public tipo: number;
    public total: number;
    public ultimaAlteracao: string;
    public vendedor: SalesMan;

    // aux
    public auxType: string;
    public auxVendedor: string;
    public auxStatus: string;
}
