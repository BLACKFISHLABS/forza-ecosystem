import { ItemTabela } from './item-tabela.model';

export class PriceTable {
    public ItensTabela: ItemTabela[];
    public ativo: boolean;
    public codigo: string;
    public idTabela: number;
    public idEmpresa: number;
    public nome: string;
    public ultimaAlteracao: string;

    // aux
    public auxAtivo: string;
}
