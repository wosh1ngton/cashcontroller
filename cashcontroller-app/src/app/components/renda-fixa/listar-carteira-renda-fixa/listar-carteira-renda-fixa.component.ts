import { Component, OnInit } from '@angular/core';
import { AtivoCarteira } from 'src/app/models/ativo-carteira.model';
import { OperacaoRendaFixaService } from 'src/app/services/operacao-renda-fixa.service';

@Component({
  selector: 'app-listar-carteira-renda-fixa',
  templateUrl: './listar-carteira-renda-fixa.component.html',
  styleUrl: './listar-carteira-renda-fixa.component.css'
})
export class ListarCarteiraRendaFixaComponent implements OnInit {

  
  constructor(private operacaoRendaFixaService: OperacaoRendaFixaService) {}  
  
  ativoSelecionado: number = 0;
  carteira: AtivoCarteira[] = [];
  totalValorMercado: number = 0;
  totalCusto: number = 0;
  totalValorizacao: number = 0;  
  totalContratado: number = 0;
  totalProventos: number = 0;

  ngOnInit(): void {

    this.operacaoRendaFixaService.listarCarteiraRendaFixa()
      .subscribe(res => {
        this.carteira = res;
        this.calculateTotals();
      }) 
  }


  selecionarAtivo(id: any) {    
    this.ativoSelecionado = id.data;
  }

  getTotalValorMercado(): number {
    return this.carteira.reduce((total, item) => total + item.valorMercado, 0);
  }

  getTotalValorContratado(): number {
    return this.carteira.reduce((total, item) => total + item.valorContratado, 0);
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
    this.totalContratado = this.getTotalValorContratado();
    this.totalValorMercado = this.getTotalValorMercado();
    this.totalCusto = this.getCustoTotal();
    this.totalValorizacao = this.totalValorMercado - this.totalCusto;
    this.totalProventos = this.getTotalProventos();
  }
}


