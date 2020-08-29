import { City } from './city.model';

export class Address {
    public id: number;
    public cep: string;
    public cityJson: City;
    public complement: string;
    public location: string;
    public neighborhood: string;
    public number: string;
    public street: string;
}
