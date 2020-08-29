import { Company } from './company.model';
import { Profile } from './profile.model';

export class User {
    public firstName: string;
    public lastName: string;
    public password: string;
    public email: string;
    public profiles: Profile[];
    public state: string;
    public ssoId: string;
    public companyJson: Company;
}
