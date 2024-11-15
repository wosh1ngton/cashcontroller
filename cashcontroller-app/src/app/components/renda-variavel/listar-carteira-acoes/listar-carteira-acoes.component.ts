import { Component, OnInit } from '@angular/core';
import { filter, forkJoin, map } from 'rxjs';
import { AtivoBrapi } from 'src/app/models/ativo-brapi.model';
import { AtivoCarteira } from 'src/app/models/ativo-carteira.model';
import { AtivoService } from 'src/app/services/ativo.service';
import { OperacaoRendaVariavelService } from 'src/app/services/operacao-renda-variavel.service';
import { DetalharAtivoComponent } from '../detalhar-ativo/detalhar-ativo.component';
import { LoadingService } from 'src/app/services/loading.service';
import { AtivoCarteiraService } from 'src/app/services/ativo-carteira.service';

@Component({
  selector: 'app-carteira-acoes',
  templateUrl: './listar-carteira-acoes.component.html',
  styleUrl: './listar-carteira-acoes.component.css',  
})
export class ListarCarteiraAcoesComponent implements OnInit {
  constructor(
    private operacaoRendaVariavelService: OperacaoRendaVariavelService,
    private ativoService: AtivoService,
    private loading: LoadingService,
    private ativoCarteiraService: AtivoCarteiraService
  ) {}

  carteira: AtivoCarteira[] = [];
  totalValorMercado: number = 0;
  totalCusto: number = 0;
  totalValorizacao: number = 0;
  ativoSelecionado: number = 0;
  totalProventos: number = 0;
  ibov: string= "";
  ngOnInit(): void {
    this.listarCarteira();   
  }
  
  selecionarAtivo(id: any) {    
    this.ativoSelecionado = id.data;
  }
  listarCarteira() {
    this.ativoCarteiraService.getIbov().subscribe((res:any) => {
      this.ibov = res;
    });
    const ativosBrapi$ = this.ativoService.getAcoesBrapi();
    const carteira$ = this.loading.showLoaderUntilCompleted(this.operacaoRendaVariavelService.carteiraAcoes());

    forkJoin([ativosBrapi$, carteira$])
      .pipe(
        map(([ativosBrapi, carteira]) => {
          return carteira.map((c) => {
            let ativo = ativosBrapi.stocks.find(
              (a: any) => a.stock === c.ativo.sigla
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
