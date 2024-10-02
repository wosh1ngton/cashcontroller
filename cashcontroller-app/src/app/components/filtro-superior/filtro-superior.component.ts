import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Observable, map } from 'rxjs';
import { FilterOperacao } from 'src/app/models/filter-operacao.model';
import { PrimengModule } from 'src/app/primeng/primeng.module';
import { FiltroOperacaoService } from 'src/app/services/filtro-operacao.service';
import { FiltrarOperacao } from 'src/app/services/interfaces/filtrar-operacao';
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
    public operacaoRendaVariavelService: OperacaoRendaVariavelService,
    private filterService: FiltroOperacaoService
  ) { }

  meses$: Observable<any[]> | undefined;  
  filter: FilterOperacao = new FilterOperacao();
  anos: number[] = [];  
  @Output() selecaoMes = new EventEmitter<string>();
  @Input() selectedMonth: number = 0;
  @Input() selectedAno: number = 2024;
  @Input('tipoDeOperacao') tipoDeOperacao!: FiltrarOperacao;

  ngOnInit(): void {
    const hoje = new Date();
    const anoAtual = hoje.getFullYear();
    this.filter.ano = anoAtual;
    this.buscarAnosComDespesas();    
    this.getMeses(anoAtual);
  }

  ngOnChanges(changes: SimpleChanges): void {    
    this.getMeses(this.selectedAno);
    this.filter.ano = this.selectedAno;
  }

 
  
  getMeses(ano?: number) {    
    this.selectedAno = ano!;
    this.meses$ = this.filterService.getMeses(this.tipoDeOperacao, ano);
  }

  selecionarMes($event: any) {
    
    console.log('tt',$event)
    this.selecaoMes.emit($event);
    this.selectedMonth = $event.mesString;    
  }


  buscarAnosComDespesas() {
    this.filterService.getAnos(this.tipoDeOperacao)
      .subscribe(res => this.anos = res);    
  }
}
