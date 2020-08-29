import { Activity } from './activity.model';
import { MemberShip } from './membership.model';

export class DadosReceita {
    public abertura: string;
    public atividadePrincipal: Activity;
    public atividadesSecundarias: Activity;
    public bairro: string;
    public capitalSocial: string;
    public cep: string;
    public cnpj: string;
    public complemento: string;
    public dataSituacao: string;
    public dataSituacaoEspecial: string;
    public efr: string;
    public email: string;
    public fantasia: string;
    public logradouro: string;
    public message: string;
    public motivoSituacao: string;
    public municipio: string;
    public naturezaJuridica: string;
    public nome: string;
    public numero: string;
    public qsa: MemberShip[];
    public situacao: string;
    public situacaoEspecial: string;
    public status: string;
    public telefone: string;
    public tipo: string;
    public uf: string;
    public ultimaAtualizacao: string;
}
