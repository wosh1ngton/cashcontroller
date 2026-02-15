import {
  Component,
  Input,
  OnChanges,
  OnInit,
  SimpleChanges,
} from '@angular/core';
import { PrimengModule } from 'src/app/primeng/primeng.module';
import { CardComponent } from '../shared/card/card.component';
import { OperacaoRendaVariavel } from 'src/app/models/operacao-renda-variavel.model';
import { EventoRendaVariavel } from 'src/app/models/evento-renda-variavel.model';
import { FilterOperacao } from 'src/app/models/filter-operacao.model';

@Component({
  selector: 'app-numeros-do-mes',
  standalone: true,
  imports: [PrimengModule, CardComponent],
  templateUrl: './numeros-do-mes.component.html',
  styleUrl: './numeros-do-mes.component.css',
})
export class NumerosDoMesComponent implements OnInit, OnChanges {

  @Input() eventos: EventoRendaVariavel[] = [];
  @Input() operacoes: OperacaoRendaVariavel[] = [];
  @Input() filtro: FilterOperacao | undefined;
  totalEmProventosMes: number = 0;
  totalEmRendimentosMes: number = 0;
  investimentoEmAcoesMes: number = 0;
  investimentoEmFiisMes: number = 0;
  rendimentosMaisProventos: number = 0;
  saldoInvestido: number = 0;

  ngOnChanges(changes: SimpleChanges): void {
    this.calculaTotalProventos();
    this.calculaTotalRendimentos();
    this.calculaTotalInvestidoAcoes();
    this.calculaTotalInvestidoFiis();
    this.calculaTotalInvestido();
    this.calculaTotalGeralRendimentos();
  }

  private calculaTotalInvestido() {
    this.saldoInvestido = this.operacoes
      .reduce(
        (total: number, operacao: any) => this.getTotalInvestidoMes(operacao, total),
        0
      );
  }

  private calculaTotalInvestidoFiis() {
    this.investimentoEmFiisMes = this.operacoes
      .filter((operacao: any) => !this.isInvestimentoEmAcoes(operacao))
      .reduce(
        (total: number, operacao: any) => this.getTotalInvestidoMes(operacao, total),
        0
      );
  }

  private calculaTotalInvestidoAcoes() {
    this.investimentoEmAcoesMes = this.operacoes
      .filter((operacao: any) => this.isInvestimentoEmAcoes(operacao))
      .reduce(
        (total: number, operacao: any) => this.getTotalInvestidoMes(operacao, total),
        0
      );
  }

  private calculaTotalRendimentos() {
    this.totalEmRendimentosMes = this.eventos
      .filter((operacao: any) => !this.isProvento(operacao))
      .reduce(
        (total: number, evento: any) => total + evento.valorTotal,
        0
      );
  }

  private calculaTotalProventos() {
    this.totalEmProventosMes = this.eventos
      .filter((operacao: any) => this.isProvento(operacao))
      .reduce(
        (total: number, evento: any) => total + evento.valorTotal,
        0
      );
  }

  private isInvestimentoEmAcoes(operacao: any): boolean {
    return operacao.ativoDto.subclasseAtivoDto.id == 2;
  }

  private isProvento(operacao: any): boolean {
    return operacao.ativo.subclasseAtivoDto.id == 2;
  }

  private getTotalInvestidoMes(operacao: any, total: number): number {
      return operacao.tipoOperacaoDto.id === 1
        ? total + operacao.valorTotal
        : operacao.tipoOperacaoDto.id === 2
        ? total - operacao.valorTotal
        : total;
  }

  private calculaTotalGeralRendimentos(): void {
    this.rendimentosMaisProventos = this.totalEmProventosMes + this.totalEmRendimentosMes;
  }

  ngOnInit(): void {}
}
