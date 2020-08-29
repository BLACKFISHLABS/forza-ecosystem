import { Principal } from './principal.model';

export class AuthDetails {
    public name: string;
    public authenticated: string;
    public permissions: string;
    public principal: Principal;
}
