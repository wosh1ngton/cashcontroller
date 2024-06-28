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
import { OperacaoRendaVariavelDto } from 'src/app/models/dto/operacao-renda-variavel.model';
import { EnumClasseAtivo } from 'src/app/enums/classe-ativo.enum';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-renda-variavel-form',
  standalone: true,  
  imports: [
    DropdownModule,
    FormsModule,
    CommonModule,
    InputNumberModule,
    CalendarModule
  ],
  templateUrl: './renda-variavel-form.component.html',
  styleUrl: './renda-variavel-form.component.css'
})
export class RendaVariavelFormComponent {

  operacaoRendaVariavel: OperacaoRendaVariavelDto = new OperacaoRendaVariavelDto();
  value: string | undefined;
  ativos: Ativo[] = [];
  ativosBrapi: any;
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
    this.buscarAtivos(EnumClasseAtivo.RENDA_VARIAVEL);    
    this.buscarTiposOperacao();
  }  

  private buscarAtivos(id: number) {
    this.ativoService.getAtivosPorClasse(id).subscribe(
        ativos => this.ativos = ativos
    );
  }

  // private buscarAtivosBrapi() {
  //   this.ativoService.getAtivosBrapi().subscribe(
  //       ativos => {
  //         this.ativosBrapi = ativos.stocks.map((a:any) => { 
  //           return {sigla: a.stock,  nome: a.name, logo: a.logo}
  //         }) ; 
          
  //         console.log('teste', this.ativosBrapi)
  //       }
  //   );
  // }

  private buscarTiposOperacao() {
    this.operacaoRendaVariavelService.getTipoOperacoes().subscribe(  
      
        tipos => {console.log(tipos), this.tiposOperacao = tipos      }
    );
  }

  save(operacao: any) {    
    console.log(operacao)
    this.operacaoRendaVariavelService
      .save(operacao)
        .pipe(
          tap(() => {this.dialogRef.close(operacao)})
          )
        .subscribe();
  }
}
