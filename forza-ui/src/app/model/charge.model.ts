import { ChargeBlock } from './charge-block.model';
import { ChargeVehicle } from './charge-vehicle.model';

export class Charge {
    public carga: ChargeBlock;
    public codigo: string;
    public emissao: string;
    public emitente: string;
    public id: number;
    // rotas: [...]
    public status: number;
    public veiculo: ChargeVehicle;
    public vendedor: string;
    // aux
    public auxVehicle: string;
    public auxStatus: string;
    public auxVendedor: string;
}
