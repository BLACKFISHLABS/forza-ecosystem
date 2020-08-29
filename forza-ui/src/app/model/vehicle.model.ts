import { SalesMan } from './salesman.model';

export class Vehicle {
    public Vendedor: SalesMan[];
    public description: string;
    public id: number;
    public plate: string;

    // aux
    public auxVendedor: string;
}
