import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PoBreadcrumbItem } from '@po-ui/ng-components';
import { Company } from 'src/app/model/company.model';
import { CompanyService } from 'src/app/services/company.service';

@Component({
  selector: 'app-company-view',
  templateUrl: './company-view.component.html',
  styleUrls: ['./company-view.component.css'],
})
export class CompanyViewComponent implements OnInit {

  public title: string;
  public obj: Company;
  public currentId: string;
  public showLoading = false;

  public breadcrumbItems: Array<PoBreadcrumbItem> = [
    { label: 'Painel', link: '/dashboard' },
    { label: 'Empresa', link: '/company' },
    { label: 'View', link: '' },
  ];

  constructor(
    private activatedRoute: ActivatedRoute,
    private companyService: CompanyService,
    private route: Router,
  ) { }

  public ngOnInit() {
    this.obj = new Company();
    this.activatedRoute.queryParams.subscribe((params) => {
      if (params) {
        this.title = 'Empresa #'.concat(params.idEmpresa);
        this.currentId = params.idEmpresa;
        this.view(this.currentId);
      }
    });
  }

  public view(idCompany: string) {
    this.showLoading = true;
    this.companyService.findOne(idCompany).subscribe((response) => {
      this.setModel(response);
      this.showLoading = false;
    }, () => {
      this.showLoading = false;
    });
  }

  public setModel(company: Company) {
    this.obj = company;
    this.obj.auxCity = company.addressJson.cityJson.nome;
    this.obj.auxCep = company.addressJson.cep;
    this.obj.auxEstado = company.addressJson.cityJson.Estado.uf;
    this.obj.auxNumber = company.addressJson.number;
    this.obj.auxcomplement = company.addressJson.complement || '.';
    this.obj.auxNeighborhood = company.addressJson.neighborhood;
    this.obj.auxStreet = company.addressJson.street;
    this.obj.auxPhoneNumber = company.contactJson.phoneJson.phoneNumber;
    this.obj.auxPhoneName = company.contactJson.name;
  }

  public cancel() {
    this.returnCompany();
  }

  public edit() {
    this.route.navigate(['app/company/edit'], { queryParams: { code: this.obj.idEmpresa, id: this.obj.idEmpresa } });
  }

  public returnCompany() {
    this.route.navigate(['app/dashboard']);
  }
}
