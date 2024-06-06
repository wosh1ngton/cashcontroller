import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DropdownModule } from 'primeng/dropdown';
import { InputNumberModule } from 'primeng/inputnumber';
import { CalendarModule } from 'primeng/calendar';
import { MessageService } from 'primeng/api';
import { AtivoService } from 'src/app/services/ativo.service';
import { Ativo } from 'src/app/models/ativo.model';
import { tap } from 'rxjs';
import { OperacaoRendaVariavelService } from 'src/app/services/operacao-renda-variavel.service';
import { TipoOperacao } from 'src/app/models/tipo-operacao.model';
import { DynamicDialogRef } from 'primeng/dynamicdialog';
import { OperacaoRendaFixa } from 'src/app/models/operacao-renda-fixa.model';
import { OperacaoRendaFixaService } from 'src/app/services/operacao-renda-fixa.service';
import { IndexadorService } from 'src/app/services/indexador.service';
import { Indexador } from 'src/app/models/indexador.model';

@Component({
  selector: 'app-renda-fixa-form',
  standalone: true,  
  imports: [
    DropdownModule,
    FormsModule,
    InputNumberModule,
    CalendarModule
  ],
  templateUrl: './renda-fixa-form.component.html',
  styleUrl: './renda-fixa-form.component.css'
})
export class RendaFixaFormComponent {

  operacaoRendaFixa: OperacaoRendaFixa = new OperacaoRendaFixa();
  value: string | undefined;
  ativos: Ativo[] = [];
  indices: Indexador[] = [];
  ativosBrapi: any;
  ativoSelecionado: any;
  tiposOperacao: TipoOperacao[] = [];
  tipoOperacaoSelecionado: any;
  quantidade: any;
  valorAtivo: any;
  valorCorretagem: any;
  dataOperacao: any;
  


  constructor(public ativoService: AtivoService, 
              public operacaoRendaFixaService: OperacaoRendaFixaService,
              public operacaoRendaVariavelService: OperacaoRendaVariavelService,
              public indexadorService: IndexadorService,
              public messageService: MessageService,
              public dialogRef: DynamicDialogRef) 
  {}

  ngOnInit(): void {
    this.buscarAtivos();    
    this.buscarTiposOperacao();
    this.buscarIndexadores();
  }  

  private buscarAtivos() {
    this.ativoService.getAtivosPorClasse(1).subscribe(
        ativos => this.ativos = ativos
    );
  } 

  private buscarTiposOperacao() {
    this.operacaoRendaVariavelService.getTipoOperacoes().subscribe(  
      
        tipos => {console.log(tipos), this.tiposOperacao = tipos      }
    );
  }

  private buscarIndexadores() {
    this.indexadorService.getIndexadores().subscribe(
      index => { this.indices = index }
    );
  }

  save(operacao: any) {    
    this.operacaoRendaFixaService
      .save(operacao)
        .pipe(
          tap(() => {this.dialogRef.close(operacao)})
          )
        .subscribe();
  }
}
