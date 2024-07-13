import {
  Component,
  Input,
  OnChanges,
  OnInit,
  SimpleChanges,
} from '@angular/core';
import { PrimengModule } from 'src/app/primeng/primeng.module';

@Component({
  selector: 'app-numeros-do-mes',
  standalone: true,
  imports: [PrimengModule],
  templateUrl: './numeros-do-mes.component.html',
  styleUrl: './numeros-do-mes.component.css',
})
export class NumerosDoMesComponent implements OnInit, OnChanges {
  @Input() eventos: any[] = [];
  @Input() operacoes: any[] = [];
  @Input() filtro: any;
  totalEmProventosMes: number = 0;
  totalEmRendimentosMes: number = 0;
  investimentoEmAcoesMes: number = 0;
  investimentoEmFiisMes: number = 0;
  rendimentosMaisProventos: number = 0;

  ngOnChanges(changes: SimpleChanges): void {
    this.calculaTotalProventos();
    this.calculaTotalRendimentos();
    this.calculaTotalInvestidoAcoes();
    this.calculaTotalInvestidoFiis();
    this.calculaTotalGeralRendimentos();
  }
  

  private calculaTotalInvestidoFiis() {
    this.investimentoEmFiisMes = this.operacoes
      .filter(operacao => !this.isInvestimentoEmAcoes(operacao))
      .reduce(
        (total, operacao) => this.getTotalInvestidoMes(operacao, total),
        0
      );
  }

  private calculaTotalInvestidoAcoes() {
    this.investimentoEmAcoesMes = this.operacoes
      .filter(operacao => this.isInvestimentoEmAcoes(operacao))
      .reduce(
        (total, operacao) => this.getTotalInvestidoMes(operacao, total),
        0
      );
  }

  private calculaTotalRendimentos() {
    this.totalEmRendimentosMes = this.eventos
      .filter(operacao => !this.isProvento(operacao))
      .reduce(
        (total, evento) => total + evento.valorTotal,
        0
      );
  }

  private calculaTotalProventos() {
    this.totalEmProventosMes = this.eventos
      .filter(operacao => this.isProvento(operacao))
      .reduce(
        (total, evento) => total + evento.valorTotal,
        0
      );
  }

  private isInvestimentoEmAcoes(operacao: any): boolean {
    return operacao.ativoDto.subclasseAtivoDto.id == 2;
  }

  private isProvento(operacao: any): boolean {
    return operacao.ativo.subclasseAtivoDto.id == 2;
  }

  private getTotalInvestidoMes(operacao: any, total: any): any {       
      return operacao.tipoOperacaoDto.id === 1
        ? total + operacao.valorTotal
        : operacao.tipoOperacaoDto.id === 2
        ? total - operacao.valorTotal
        : total;
  }

  private calculaTotalGeralRendimentos(): void {    
    this.rendimentosMaisProventos = this.totalEmProventosMes + this.totalEmRendimentosMes
  }

  ngOnInit(): void {}
}
