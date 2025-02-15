import { Component, OnInit } from '@angular/core';
import { forkJoin, map } from 'rxjs';
import { AtivoCarteira } from 'src/app/models/ativo-carteira.model';
import { AtivoService } from 'src/app/services/ativo.service';
import { LoadingService } from 'src/app/services/loading.service';
import { OperacaoRendaVariavelService } from 'src/app/services/operacao-renda-variavel.service';

@Component({
  selector: 'app-listar-fiis',  
  templateUrl: './listar-fiis.component.html',
  styleUrl: './listar-fiis.component.css'
})
export class ListarFiisComponent implements OnInit {
  constructor(
    private operacaoRendaVariavelService: OperacaoRendaVariavelService,
    private ativoService: AtivoService,
    private loading: LoadingService
  ) {}

  carteira: AtivoCarteira[] = [];
  totalValorMercado: number = 0;
  totalCusto: number = 0;
  totalValorizacao: number = 0;
  ativoSelecionado: number = 0;
  totalProventos: number = 0;
  
  ngOnInit(): void {
    this.listarCarteira();   
  }

  selecionarAtivo(id: any) {
    console.log('id', id)
    this.ativoSelecionado = id.data;
  }
  listarCarteira() {
    const ativosBrapi$ = this.ativoService.getFiisBrapi();
    const carteira$ = this.loading.showLoaderUntilCompleted(this.operacaoRendaVariavelService.carteiraFiis());

    forkJoin([ativosBrapi$, carteira$])
      .pipe(
        map(([ativosBrapi, carteira]) => {
          return carteira.map((c) => {
            console.log('o que? ', c)
            let ativo = ativosBrapi.stocks.find(
             
              (a: any) => {
                console.log('o que2? ', a),
                a.stock === c.ativo.sigla
              }
            );
            return {
              ...c,
              cotacao: ativo.close,
              oscilacaoDia: ativo.change,              
              valorMercado: ativo.close * c.custodia,
              custo: c.custodia * c.precoMedio,
              valorizacao: ativo.close * c.custodia - c.custodia * c.precoMedio,
            };
          });
        })
      )
      .subscribe({
        next: (data) => {
            (this.carteira = data),
            this.calculateTotals();
        },
        complete: () => console.log('complete'),
        error: (err: Error) => console.log('Erro: ', err.message),
      });
  }

  
  getTotalValorMercado(): number {
    return this.carteira.reduce((total, item) => total + item.valorMercado, 0);
  }

  getCustoTotal(): number {
    return this.carteira.reduce((total, item) => total + item.custo, 0);
  }

  getValorizacao(): number {
    return this.carteira.reduce((total, item) => total + (item.valorMercado - item.custo), 0);
  }

  getTotalProventos(): number {
    return this.carteira.reduce((total, item) => total + item.totalEmProventos, 0);
  }

  calculateTotals() {
    this.totalValorMercado = this.getTotalValorMercado();
    this.totalCusto = this.getCustoTotal();
    this.totalValorizacao = this.totalValorMercado - this.totalCusto;
    this.totalProventos = this.getTotalProventos();

  }
}
