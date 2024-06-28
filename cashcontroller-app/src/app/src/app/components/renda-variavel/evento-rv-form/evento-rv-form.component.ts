import { CommonModule } from '@angular/common';
import { AfterViewInit, ChangeDetectorRef, Component, OnInit } from '@angular/core';
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

@Component({
  selector: 'app-evento-rv-form',
  standalone: true,
  imports: [
    FormsModule, 
    PrimengModule, 
    DropdownModule,    
    CommonModule,
    InputNumberModule,
    CalendarModule],
  templateUrl: './evento-rv-form.component.html',
  styleUrl: './evento-rv-form.component.css'
})
export class EventoRvFormComponent implements OnInit {

  ativos: Ativo[] | undefined;
  tiposEvento: TipoEvento[] | undefined;
  eventoRendaVariavel: EventoRendaVariavel = new EventoRendaVariavel();
 


  constructor(
    public ativoService: AtivoService,               
              public messageService: MessageService,
              public dialogRef: DynamicDialogRef,
              private dialogConfig: DynamicDialogConfig,
              private cdr: ChangeDetectorRef , 

              private eventoRendaVariavelService: EventoRendaVariavelService
  ) {}

  ngOnInit(): void {
    const ativosObservable =  this.buscarAtivos(EnumClasseAtivo.RENDA_VARIAVEL);   
    const tipoEventosObservable = this.buscarTiposEventos();
    
    forkJoin([ativosObservable, tipoEventosObservable]).subscribe(() => {
      if(this.dialogConfig.data.isEdit) {
        this.eventoRendaVariavel = this.dialogConfig.data.rowData;  
        this.setActiveValue();
        this.cdr.detectChanges();
        console.log(this.ativos)
      }  
    })
   
    
  }  

  private isEdit() {
    return this.dialogConfig.data.isEdit;
  }

  private buscarAtivos(id: number) {
    return this.ativoService.getAtivosPorClasse(id)    
      .pipe(
        tap(val => this.ativos = val)
      );
      
  }

  private buscarTiposEventos() {
    return this.eventoRendaVariavelService.getTipoEventos()
      .pipe(
        tap(val => this.tiposEvento = val)
      );      
      
  }

  private setActiveValue(): void {
    if(this.eventoRendaVariavel) {
      const selectedAtivo = this.ativos?.find(a => a.id === this.eventoRendaVariavel.ativo.id)
      console.log('teste',selectedAtivo)
      if(selectedAtivo) {
        this.eventoRendaVariavel.ativo.id = selectedAtivo.id;
        console.log(this.eventoRendaVariavel.ativo);
      }
    }
  }

  save(operacao: any) {    
    console.log(operacao)
    this.eventoRendaVariavelService
      .save(operacao)
        .pipe(
          tap(() => {this.dialogRef.close(operacao)})
          )
        .subscribe();
  }
}
