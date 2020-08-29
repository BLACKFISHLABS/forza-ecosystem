import { Customer } from './customer.model';

export class RouteCustomer {
    public Cliente: Customer;
    public idCliente: number;
    public routeCustomerId: number;
    public ultimaAlteracao: string;

    // aux
    public auxAddress: string;
    public auxCodigo: string;
    public auxNomeFantasia: string;
}
