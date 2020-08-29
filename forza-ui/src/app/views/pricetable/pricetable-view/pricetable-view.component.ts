import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PoBreadcrumbItem, PoTableColumn, PoDialogService } from '@po-ui/ng-components';
import 'moment/locale/pt-br';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { ItemTabela } from 'src/app/model/item-tabela.model';
import { PriceTable } from 'src/app/model/price-table.model';
import { Utils } from 'src/app/model/utils/Utils';
import { PriceTableService } from 'src/app/services/price-table.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-pricetable-view',
  templateUrl: './pricetable-view.component.html',
  styleUrls: ['./pricetable-view.component.css'],
})
export class PriceTableViewComponent implements OnInit {

  public title: string;
  public itensTabela: ItemTabela[];
  public obj: PriceTable;
  public currentId: string;
  public utils: Utils = new Utils();

  public breadcrumbItems: Array<PoBreadcrumbItem> = [
    { label: 'Painel', link: '/dashboard' },
    { label: 'Tabelas de Preço', link: '/pricetable' },
    { label: 'View', link: '' },
  ];

  public columns: PoTableColumn[] = [
    { property: 'auxCodigoProduto', label: 'Código' },
    { property: 'auxProduto', label: 'Produto' },
    { property: 'quantidade', label: 'Estoque da Tabela' },
    { property: 'prcVenda', label: 'Valor de Venda da Tabela', type: 'currency', format: 'BRL' },
  ];

  constructor(
    private activatedRoute: ActivatedRoute,
    private priceTableService: PriceTableService,
    private loader: NgxUiLoaderService,
    private router: Router,
    private toast: ToastrService,
    private poAlert: PoDialogService,
  ) { }

  public ngOnInit() {
    this.obj = new PriceTable();
    this.activatedRoute.queryParams.subscribe((params) => {
      if (params) {
        this.title = 'Tabelas de Preço #'.concat(params.code);
        this.currentId = params.idTabela;
        this.view(this.currentId);
      }
    });
  }

  public view(idPriceTable: string) {
    this.loader.startBackground();
    this.priceTableService.findOne(idPriceTable).subscribe((response) => {
      this.setModel(response);
      this.loader.stopBackground();
    }, () => {
      this.loader.stopBackground();
    });
  }

  public setModel(priceTable: PriceTable) {
    this.obj = priceTable;
    this.obj.auxAtivo = this.utils.translateStatus(priceTable.ativo);
    this.obj.ultimaAlteracao = this.utils.formatterDate(priceTable.ultimaAlteracao);
    this.itensTabela = this.feedTable(priceTable.ItensTabela);
  }

  public feedTable(itensTabela: ItemTabela[]) {
    itensTabela.forEach((element) => {
      element.auxProduto = element.Produto.descricao;
      element.auxCodigoProduto = element.Produto.codigo;
    });
    return itensTabela;
  }

  public delete() {
    this.poAlert.confirm({
      title: 'Excluir Registro',
      message: 'Ao clicar em confirmar, o sistema irá excluir definitivamente o registro',
      confirm: () => {
        this.priceTableService.delete(this.obj.idTabela).subscribe((response) => {
          this.toast.success('Removido com sucesso!', 'Registro: ' + this.obj.idTabela);
          this.returnListPriceTable();
        }, () => {
          this.toast.error('Ocorreu um erro ao tentar remover o registro!', 'Registro: ' + this.obj.idTabela);
        });
      },
      cancel: () => {

      },
    });
  }

  public cancel() {
    this.returnListPriceTable();
  }

  public returnListPriceTable() {
    this.router.navigate(['app/pricetable']);
  }

}
