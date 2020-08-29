import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { Product } from 'src/app/model/product.model';
import { Utils } from 'src/app/model/utils/Utils';
import { ProductService } from 'src/app/services/product.service';

@Component({
  selector: 'app-pricetable-form',
  templateUrl: './pricetable-form.component.html',
  styleUrls: ['./pricetable-form.component.css'],
})
export class PriceTableFormComponent implements OnInit {

  public title: string;
  public products: Product[];
  public form: FormGroup;
  public currentId: string;
  public utils: Utils = new Utils();

  constructor(
    private activatedRoute: ActivatedRoute,
    private productService: ProductService,
    private loader: NgxUiLoaderService,
    private formBuilder: FormBuilder,
    private router: Router,
  ) { }

  public ngOnInit() {
    this.title = 'Produto';

    this.form = this.formBuilder.group({
      ean: ['', []],
      description: ['', []],
      unit: ['', []],
      group: ['', []],
      note: ['', []],
      status: ['', []],
      lastChange: ['', []],
    });

    this.activatedRoute.queryParams.subscribe((params) => {
      if (params) {
        this.title = 'Produto #'.concat(params.code);
        this.currentId = params.idProduc;
        this.view(this.currentId);
      }
    });

  }

  public view(idProduct: string) {
    this.loader.startBackground();
    this.productService.findOne(idProduct).subscribe((response) => {
      this.setModel(response);
      this.loader.stopBackground();
    }, () => {
      this.loader.stopBackground();
    });
  }

  public setModel(product: Product) {
    this.form.get('ean').setValue(product.codigoBarras);
    this.form.get('description').setValue(product.descricao);
    this.form.get('unit').setValue(product.un);
    this.form.get('group').setValue(product.grupo);
    this.form.get('note').setValue(product.obs);
    this.form.get('status').setValue(this.utils.translateStatus(product.ativo));
    this.form.get('lastChange').setValue(this.utils.getDBDate(product.ultimaAlteracao));
  }

  public cancel() {
    this.returnListProduct();
  }

  public returnListProduct() {
    this.router.navigate(['app/product']);
  }

}
