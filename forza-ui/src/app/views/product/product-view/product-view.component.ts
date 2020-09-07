import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PoBreadcrumbItem, PoDialogService } from '@po-ui/ng-components';
import { ToastrService } from 'ngx-toastr';
import { Product } from 'src/app/model/product.model';
import { Utils } from 'src/app/model/utils/Utils';
import { ProductService } from 'src/app/services/product.service';

@Component({
  selector: 'app-product-view',
  templateUrl: './product-view.component.html',
  styleUrls: ['./product-view.component.css'],
})
export class ProductViewComponent implements OnInit {

  public title: string;
  public obj: Product;
  public currentId: string;
  public utils: Utils = new Utils();
  public showLoading = false;

  public breadcrumbItems: Array<PoBreadcrumbItem> = [
    { label: 'Painel', link: '/dashboard' },
    { label: 'Produto', link: '/product' },
    { label: 'View', link: '' },
  ];

  constructor(
    private activatedRoute: ActivatedRoute,
    private productService: ProductService,
    private router: Router,
    private toast: ToastrService,
    private poAlert: PoDialogService,
  ) { }

  public ngOnInit() {
    this.obj = new Product();

    this.activatedRoute.queryParams.subscribe((params) => {
      if (params) {
        this.title = 'Produto #'.concat(params.code);
        this.currentId = params.idProduc;
        this.view(this.currentId);
      }
    });
  }

  public view(idProduct: string) {
    this.showLoading = true;
    this.productService.findOne(idProduct).subscribe((response) => {
      this.setModel(response);
      this.showLoading = false;
    }, () => {
      this.showLoading = false;
    });
  }

  public setModel(product: Product) {
    this.obj = product;
    this.obj.auxAtivo = this.utils.translateStatus(product.ativo);
    this.obj.ultimaAlteracao = this.utils.formatterDate(product.ultimaAlteracao);
  }

  public delete() {
    this.poAlert.confirm({
      title: 'Excluir Registro',
      message: 'Ao clicar em confirmar, o sistema irá excluir definitivamente o registro',
      confirm: () => {
        this.productService.delete(this.obj.idProduto).subscribe((response) => {
          this.toast.success('Removido com sucesso!', 'Registro: ' + this.obj.idProduto);
          this.returnListProduct();
        }, () => {
          this.toast.error('Ocorreu um erro ao tentar remover o registro!', 'Registro: ' + this.obj.idProduto);
        });
      },
      cancel: () => {

      },
    });
  }

  public cancel() {
    this.returnListProduct();
  }

  public returnListProduct() {
    this.router.navigate(['app/product']);
  }

}
