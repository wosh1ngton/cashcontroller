import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DropdownModule } from 'primeng/dropdown';
import { InputNumberModule } from 'primeng/inputnumber';
import { CalendarModule } from 'primeng/calendar';
import { OperacaoRendaVariavel } from '../models/operacao-renda-variavel.model';
import { MessageService, SelectItem } from 'primeng/api';
import { AtivoService } from 'src/app/services/ativo.service';
import { HttpClient } from '@angular/common/http';
import { Ativo } from '../models/ativo.model';
import { Observable, tap } from 'rxjs';
import { OperacaoRendaVariavelService } from 'src/app/services/operacao-renda-variavel.service';
import { TipoOperacao } from '../models/tipo-operacao.model';
import { DialogService, DynamicDialogRef } from 'primeng/dynamicdialog';

@Component({
  selector: 'app-renda-variavel-form',
  standalone: true,  
  imports: [
    DropdownModule,
    FormsModule,
    InputNumberModule,
    CalendarModule
  ],
  templateUrl: './renda-variavel-form.component.html',
  styleUrl: './renda-variavel-form.component.css'
})
export class RendaVariavelFormComponent {

  operacaoRendaVariavel: OperacaoRendaVariavel = new OperacaoRendaVariavel();
  value: string | undefined;
  ativos: Ativo[] = [];
  ativoSelecionado: any;
  tiposOperacao: TipoOperacao[] = [];
  tipoOperacaoSelecionado: any;
  quantidade: any;
  valorAtivo: any;
  valorCorretagem: any;
  dataOperacao: any;
  


  constructor(public ativoService: AtivoService, 
              public operacaoRendaVariavelService: OperacaoRendaVariavelService,
              public messageService: MessageService,
              public dialogRef: DynamicDialogRef) 
  {}

  ngOnInit(): void {
    this.buscarAtivos();
    this.buscarTiposOperacao();
  }  

  private buscarAtivos() {
    this.ativoService.getAtivos().subscribe(
        ativos => this.ativos = ativos
    );
  }

  private buscarTiposOperacao() {
    this.operacaoRendaVariavelService.getTipoOperacoes().subscribe(     
        tipos => this.tiposOperacao = tipos      
    );
  }

  save(operacao: any) {    
    this.operacaoRendaVariavelService.save(operacao)
    .pipe(tap(() => this.dialogRef.close(operacao)))
    .subscribe();
  }
}
