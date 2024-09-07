import { CommonModule } from '@angular/common';
import {
  AfterViewInit,
  ChangeDetectorRef,
  Component,
  OnInit,
} from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MessageService } from 'primeng/api';
import { CalendarModule } from 'primeng/calendar';
import { DropdownModule } from 'primeng/dropdown';
import { DynamicDialogConfig, DynamicDialogRef } from 'primeng/dynamicdialog';
import { InputNumberModule } from 'primeng/inputnumber';
import { Observable, forkJoin, tap } from 'rxjs';
import { EnumClasseAtivo } from 'src/app/enums/classe-ativo.enum';
import { Ativo } from 'src/app/models/ativo.model';
import { EventoRendaVariavel } from 'src/app/models/evento-renda-variavel.model';
import { TipoEvento } from 'src/app/models/tipo-evento.model';
import { PrimengModule } from 'src/app/primeng/primeng.module';
import { AtivoService } from 'src/app/services/ativo.service';
import { EventoRendaVariavelService } from 'src/app/services/evento-renda-variavel.service';
import { LoadingService } from 'src/app/services/loading.service';
import { DateUtil } from 'src/app/shared/util/date-util';
import { SharedModule } from 'src/app/shared-modules/shared.module';
import { EventoRendaFixaService } from 'src/app/services/evento-renda-fixa.service';
import { EventoRendaFixa } from 'src/app/models/evento-renda-fixa.model';

@Component({
  selector: 'app-evento-rv-form',
  standalone: true,
  imports: [
    FormsModule,
    PrimengModule,
    DropdownModule,
    CommonModule,
    InputNumberModule,
    CalendarModule, 
    SharedModule   
      
  ],
  templateUrl: './evento-renda-fixa-form.component.html',  
  providers: []
})
export class EventoRendaFixaFormComponent implements OnInit {
  ativos: Ativo[] | undefined;
  tiposEvento: TipoEvento[] | undefined;
  eventoRendaFixa: EventoRendaFixa = new EventoRendaFixa();
  checked: boolean = false;
  periodosDeRecorrencia: number = 0;
  showPeriodosRecorrencia: boolean = false;

  constructor(
    public ativoService: AtivoService,
    public messageService: MessageService,
    public dialogRef: DynamicDialogRef,
    private dialogConfig: DynamicDialogConfig,
    private cdr: ChangeDetectorRef,
    private loading: LoadingService,
    private eventoRendaFixaService: EventoRendaFixaService
  ) {}

  ngOnInit(): void {
    this.buscarAtivos(EnumClasseAtivo.RENDA_FIXA);
    this.buscarTiposEventos();
    if (this.dialogConfig.data.rowData != undefined) {
      this.setEvento();
    }
    this.setValoresSelecionados();
  }

  private setEvento() {   
    let dataPagamento = DateUtil.dateConstructor(
      this.dialogConfig.data.rowData.dataPagamento
    );
    this.eventoRendaFixa.valor = this.dialogConfig.data.rowData.valor;    
    this.eventoRendaFixa.dataPagamento = dataPagamento;
    this.eventoRendaFixa.id = this.dialogConfig.data.rowData.id;
    this.eventoRendaFixa.ativo = this.dialogConfig.data.rowData.ativo;
    this.eventoRendaFixa.tipoEvento = this.dialogConfig.data.rowData.tipoEvento;
  }

  private isEdit() {
    return this.dialogConfig.data.isEdit;
  }

  setPeriodos(event: any) {
    this.checked = event.checked;
  }

  private buscarAtivos(id: number) {
    return this.ativoService
      .getAtivosPorClasse(id)
      .pipe(tap((val) => (this.ativos = val)))
      .subscribe();
  }

  private buscarTiposEventos() {
    return this.eventoRendaFixaService
      .getTipoEventos()
      .pipe(tap((val) => (this.tiposEvento = val)))
      .subscribe();
  }

  private setValoresSelecionados(): void {
    if (this.eventoRendaFixa) {
      const selectedAtivo = this.ativos?.find(
        (a) => a.id === this.eventoRendaFixa.ativo.id
      );
      if (selectedAtivo) {
        this.eventoRendaFixa.ativo.id = selectedAtivo.id;
      }
    }
  }

  save(evento: any) {
   
    this.loading
      .showLoaderUntilCompleted(
        this.eventoRendaFixaService.save(evento).pipe(
          tap(() => {
            this.dialogRef.close(evento);
          })
        )
      ).subscribe({
        next: (val) => console.log(val),
        error: () => {
          this.messageService.add({
            severity: 'error',
            summary: 'Erro',
            detail: 'Tente novamente',
          });
        },
      });
  }

  editar(evento: any) {
    this.eventoRendaFixaService
      .editar(evento)
      .pipe(
        tap(() => {
          this.dialogRef.close(evento);
        })
      )
      .subscribe();
  }
}
