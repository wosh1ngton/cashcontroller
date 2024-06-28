import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Observable, map } from 'rxjs';
import { FilterOperacao } from 'src/app/models/filter-operacao.model';
import { PrimengModule } from 'src/app/primeng/primeng.module';
import { OperacaoRendaVariavelService } from 'src/app/services/operacao-renda-variavel.service';
import { DateUtil } from 'src/app/shared/util/date-util';

@Component({
  selector: 'app-filtro-superior',
  templateUrl: './filtro-superior.component.html',
  styleUrl: './filtro-superior.component.css',
  standalone: true,
  imports: [PrimengModule, FormsModule]
})
export class FiltroSuperiorComponent implements OnInit, OnChanges {

  constructor(
    private operacaoRendaVariavelService: OperacaoRendaVariavelService
  ) {

  }

  ngOnInit(): void {
    const hoje = new Date();
    const anoAtual = hoje.getFullYear();
    this.filter.ano = anoAtual;
    this.buscarAnosComDespesas();    
    this.getMeses(anoAtual);
  }

  ngOnChanges(changes: SimpleChanges): void {
    console.log('oi')
    this.getMeses(this.selectedAno);
    this.filter.ano = this.selectedAno;
  }

  filtroMes$: Observable<any[]> | undefined;
  //selectedMonth: any;
  filter: FilterOperacao = new FilterOperacao();
  anos: [] = [];
  @Output() selecaoMes = new EventEmitter<string>();
  @Input() selectedMonth: number = 0;
  @Input() selectedAno: number = 2024;

  getMeses(ano?: number) {
    this.selectedAno = ano!;
    this.filtroMes$ = this.operacaoRendaVariavelService.getMesesComOperacoesPorAno(ano).pipe(
      map((meses) => {
        if (meses) {
          meses.forEach(
            (element: any) => {
              element.mesString = element.mesString.slice(0, 3);
            });
        }
        return meses;
      }
      ))
  }

  selecionarMes($event: any) {

    this.selecaoMes.emit($event);
    this.selectedMonth = $event.mesString;
    console.log(this.selectedMonth)
  }


  buscarAnosComDespesas() {
    this.operacaoRendaVariavelService.getAnosComOperacoes().subscribe((res) => {
      this.anos = res;
    });
  }
}
