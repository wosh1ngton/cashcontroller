import { Component, OnInit } from '@angular/core';
import { DialogService, DynamicDialogRef } from 'primeng/dynamicdialog';
import { CadastroAtivoComponent } from '../cadastro-ativo/cadastro-ativo.component';
import { AtivoService } from 'src/app/services/ativo.service';
import { Ativo } from 'src/app/models/ativo.model';
import { EnumClasseAtivo } from 'src/app/enums/classe-ativo.enum';
import { Column } from 'src/app/interfaces/column';
import { ConfirmationService, MessageService } from 'primeng/api';

@Component({
  selector: 'app-listar-ativos', 
  templateUrl: './listar-ativos.component.html',
  styleUrl: './listar-ativos.component.css',
  providers: [DialogService, ConfirmationService, MessageService]
})
export class ListarAtivosComponent implements OnInit {

  constructor(
    public dialogService: DialogService,
    private ativoService: AtivoService,
    private confirmationService: ConfirmationService,
    private messageService: MessageService
  ) {

  }
  ref: DynamicDialogRef | undefined;
  ativos: Ativo[] = [];
  
  colsAtivos = [
    { field: 'nome', header: 'Nome', type: 'string'},
    { field: 'sigla', header: 'Sigla', type: 'string'},
    { field: 'dataVencimento', header: 'Data de Vencimento', type: 'data'},
    { field: 'subclasseAtivoDto', header: 'Subclasse', type: 'objeto'},
    { field: 'indexador', header: 'Indexador', type: 'indexador'},
    { field: 'isIsento', header: 'Isento', type: 'boolean'}
  ]
  

  ngOnInit(): void {
    this.listarAtivosRendaFixa();
  }

  private listarAtivosRendaFixa() {
    this.ativoService.getAtivosPorClasse(EnumClasseAtivo.RENDA_FIXA)
      .subscribe(res => this.ativos = res);
  }

  showAtivoDialog() {
    this.ref = this.dialogService.open(CadastroAtivoComponent, {
      header: 'Cadastro de Ativo',
      width: '50vw',
      modal: true,
      breakpoints: {
        '960px': '75vw',
        '640px': '90vw',
      },
      data: {
       
      },
    });

    this.ref.onClose.subscribe((res) => this.listarAtivosRendaFixa())
  }

  editarAtivo(id: number) {
    console.log(id)
    this.ref = this.dialogService.open(CadastroAtivoComponent, {
      header: 'Editar Ativo',
      width: '50vw',
      modal: true,
      breakpoints: {
        '960px': '75vw',
        '640px': '90vw',
      },
      data: {
        isEdit: true,
        rowData: id,
      },
    });

    this.ref.onClose.subscribe((res) => this.listarAtivosRendaFixa())
  }

  validarExclusaoAtivo(ativo: any, event: Event, key: string) {
    console.log(ativo)
    this.confirmationService.confirm({
      key: key,
      target: event.target as EventTarget,
      message: 'Tem certeza que deseja excluir esta operação?',
      header: 'Confirmação',
      icon: 'pi pi-exclamation-triangle',
      acceptIcon: 'none',
      rejectIcon: 'none',
      acceptLabel: 'Sim',
      rejectLabel: 'Não',      
      acceptButtonStyleClass: 'p-button-danger',
      accept: () => {
       this.excluirAtivo(ativo.id).subscribe(() => this.listarAtivosRendaFixa());
        this.messageService.add({
          severity: 'info',
          summary: 'Confirmado',
          detail: 'Confirmada a exclusão',
        });
      },
      reject: () => {
      //  this.filterData();
        this.messageService.add({
          severity: 'error',
          summary: 'Rejeitado',
          detail: 'Cancelada a exclusão',
          life: 3000,
        });
        
      },
    });
  }

  excluirAtivo(id: string) {
    return this.ativoService.excluir(id);
  }

}
