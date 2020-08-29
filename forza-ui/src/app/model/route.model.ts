import { Company } from './company.model';
import { RouteCustomer } from './route-customer.model';

export class Route {
    public Clientes: RouteCustomer[];
    public ativo: boolean;
    public codigo: string;
    public empresa: Company;
    public nome: string;
    public routeId: number;
    public ultimaAlteracao: string;

    // aux
    public auxAtivo: string;
}
