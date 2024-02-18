import { Component, OnDestroy } from '@angular/core';
import { PrimengModule } from 'src/app/primeng/primeng.module';
import { ButtonModule } from 'primeng/button';
import { MenuItem } from 'primeng/api';
import { DialogService, DynamicDialogModule, DynamicDialogRef } from 'primeng/dynamicdialog';
import { RendaVariavelFormComponent } from '../renda-variavel-form/renda-variavel-form.component';
import { OperacaoRendaVariavelService } from 'src/app/services/operacao-renda-variavel.service';
import { OperacaoRendaVariavel } from '../models/operacao-renda-variavel.model';
import { Column } from 'src/app/interfaces/column';

@Component({
  selector: 'app-listar-renda-variavel',
  standalone: true,
  imports: [
    PrimengModule,
    ButtonModule,
    DynamicDialogModule    
  ],
  templateUrl: './listar-renda-variavel.component.html',
  styleUrl: './listar-renda-variavel.component.css',
  providers: [DialogService]
})
export class ListarRendaVariavelComponent  {

  item: MenuItem[] | undefined;
  visible = false;
  ref: DynamicDialogRef | undefined;
  cols!: Column[]
  operacoes: OperacaoRendaVariavel[] = [];

  constructor(public dialogService: DialogService,
              public operacaoRendaVariavelService: OperacaoRendaVariavelService) {}

  ngOnInit() {
    this.listarOperacoes();

    this.cols = [
      { field: 'ativoDto', header: 'Ativo' },
      { field: 'quantidadeNegociada', header: 'Quantidade' },
      { field: 'dataOperacao', header: 'Data da Operação' },
      { field: 'valorUnitario', header: 'Valor Unitário' },
      { field: 'tipoOperacaoDto', header: 'Tipo de Operação' },
      { field: 'valorCorretagem', header: 'Valor da Corretagem' }
  ];
  }

  private listarOperacoes() {
    this.operacaoRendaVariavelService.getOperacoesRendaVariavel()
      .subscribe(
        ops => this.operacoes = ops
      );
  }

  showDialog() {
    this.ref = this.dialogService.open(RendaVariavelFormComponent, {
      header: 'Cadastro de Operação - Renda Variável',
      width: '50vw',
      height: '50vw',
      modal:true,
      breakpoints: {
          '960px': '75vw',
          '640px': '90vw'
      },
  });
  }
}
