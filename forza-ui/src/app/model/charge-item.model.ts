import { ChargeProduct } from './charge-product.model';

export class ChargeItem {
    public id: number;
    public precoVenda: number;
    public produto: ChargeProduct;
    public quantidade: number;
}
