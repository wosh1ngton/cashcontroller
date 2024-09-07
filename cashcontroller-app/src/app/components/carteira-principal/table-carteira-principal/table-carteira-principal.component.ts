import { Component, EventEmitter, Input, Output, ViewChild } from '@angular/core';
import { ConfirmationService, MessageService } from 'primeng/api';
import { DialogService, DynamicDialogRef } from 'primeng/dynamicdialog';
import { AtivoCarteira } from 'src/app/models/ativo-carteira.model';
import { AtivoCarteiraService } from 'src/app/services/ativo-carteira.service';
import { CadastrarAtivoCarteiraComponent } from '../cadastrar-ativo-carteira/cadastrar-ativo-carteira.component';

@Component({
  selector: 'app-table-carteira-principal',
  templateUrl: './table-carteira-principal.component.html',
  styleUrl: './table-carteira-principal.component.css'
})
export class TableCarteiraPrincipalComponent {
  
  dialogRef: DynamicDialogRef | undefined;
  @Input('subclasseAtivo') subclasseAtivo = new Input();
  @Input('ativosCarteira') ativosCarteira = new Input();
  totalValorMercado: number = 0;
  @Output() updateAtivosCarteira: EventEmitter<void> = new EventEmitter<void>();


  constructor(
    private dialogService: DialogService, 
    private ativoCarteiraService: AtivoCarteiraService,
    private confirmationService: ConfirmationService,
    private messageService: MessageService) {

    }
  
  carteira: AtivoCarteira[] = [];

  private getValorizacaoCarteira() : void {
      this.totalValorMercado = this.carteira.reduce((acc, value) => acc + value.valorMercado, 0);
      console.log(this.totalValorMercado)
  }
  ngOnInit(): void {
  //  this.buscarCarteiraAcoes();
   // this.buscarCarteiraFiis();
  }

  private calcularRentabilidade() {
    this.getValorizacaoCarteira();
  }

  colsCarteiraAtivo = [
    { field: 'ativo', header: 'Ativo', type: 'object'},
    { field: 'custodia', header: 'Custódia', type: 'number'},
    { field: 'custo', header: 'Custo', type: 'number'},
    { field: 'valorMercado', header: 'Valor de Mercado', type: 'number'},
    { field: 'percentual', header: 'Percentual', type: 'number'},
    { field: 'precoMedio', header: 'Preço Médio', type: 'number'},
  ]
 

  showAtivoCarteiraDialog() {
    this.dialogRef = this.dialogService.open(CadastrarAtivoCarteiraComponent, {
      header: 'Cadastro de Ativo na Carteira',
      width: '50vw',
      modal: true,
      breakpoints: {
        '960px': '75vw',
        '640px': '90vw',
      },
      data: {
        subclasse: this.subclasseAtivo
      },
    });

    this.dialogRef.onClose.subscribe((res) => {
      this.updateAtivosCarteira.emit();      
    })
  }

  editarAtivoCarteira(id: number) {
    console.log(id)
    this.dialogRef = this.dialogService.open(CadastrarAtivoCarteiraComponent, {
      header: 'Editar Ativo da Carteira',
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

    this.dialogRef.onClose.subscribe((res) => {
      this.updateAtivosCarteira.emit();
    })
  }

  validarExclusaoAtivoCarteira(ativo: any, event: Event, key: string) {
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
       this.excluirAtivo(ativo.id).subscribe(() => {
          this.updateAtivosCarteira.emit();
      });
        this.messageService.add({
          severity: 'info',
          summary: 'Confirmado',
          detail: 'Confirmada a exclusão',
        });
      },
      reject: () => {
        this.updateAtivosCarteira.emit();
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
    return this.ativoCarteiraService.excluir(id);
  }
}
