import { Product } from './product.model';

export class ItemTabela {
    public Produto: Product;
    public idItensTabela: number;
    public idProduto: number;
    public prcVenda: number;
    public quantidade: number;
    public ultimaAlteracao: string;

    // aux
    public auxProduto: string;
    public auxCodigoProduto: string;
}
