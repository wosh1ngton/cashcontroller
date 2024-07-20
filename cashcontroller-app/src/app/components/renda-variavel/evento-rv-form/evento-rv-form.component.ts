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
import { LoadingComponent } from '../../shared/loading/loading.component';
import { AppModule } from 'src/app/app.module';
import { SharedModule } from 'src/app/shared-modules/shared.module';

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
  templateUrl: './evento-rv-form.component.html',
  styleUrl: './evento-rv-form.component.css',
  providers: []
})
export class EventoRvFormComponent implements OnInit {
  ativos: Ativo[] | undefined;
  tiposEvento: TipoEvento[] | undefined;
  eventoRendaVariavel: EventoRendaVariavel = new EventoRendaVariavel();
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
    private eventoRendaVariavelService: EventoRendaVariavelService
  ) {}

  ngOnInit(): void {
    this.buscarAtivos(EnumClasseAtivo.RENDA_VARIAVEL);
    this.buscarTiposEventos();
    if (this.dialogConfig.data.rowData != undefined) {
      this.setEvento();
    }
    this.setValoresSelecionados();
  }

  private setEvento() {
    let dataCom = DateUtil.dateConstructor(
      this.dialogConfig.data.rowData.dataCom
    );
    let dataPagamento = DateUtil.dateConstructor(
      this.dialogConfig.data.rowData.dataPagamento
    );
    this.eventoRendaVariavel.valor = this.dialogConfig.data.rowData.valor;
    this.eventoRendaVariavel.dataCom = dataCom;
    this.eventoRendaVariavel.dataPagamento = dataPagamento;
    this.eventoRendaVariavel.id = this.dialogConfig.data.rowData.id;
    this.eventoRendaVariavel.ativo = this.dialogConfig.data.rowData.ativo;
    this.eventoRendaVariavel.tipoEvento = this.dialogConfig.data.rowData.tipoEvento;
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
    return this.eventoRendaVariavelService
      .getTipoEventos()
      .pipe(tap((val) => (this.tiposEvento = val)))
      .subscribe();
  }

  private setValoresSelecionados(): void {
    if (this.eventoRendaVariavel) {
      const selectedAtivo = this.ativos?.find(
        (a) => a.id === this.eventoRendaVariavel.ativo.id
      );
      if (selectedAtivo) {
        this.eventoRendaVariavel.ativo.id = selectedAtivo.id;
      }
    }
  }

  save(evento: any) {
   
    this.loading
      .showLoaderUntilCompleted(
        this.eventoRendaVariavelService.save(evento, this.periodosDeRecorrencia).pipe(
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
    this.eventoRendaVariavelService
      .editar(evento)
      .pipe(
        tap(() => {
          this.dialogRef.close(evento);
        })
      )
      .subscribe();
  }
}
