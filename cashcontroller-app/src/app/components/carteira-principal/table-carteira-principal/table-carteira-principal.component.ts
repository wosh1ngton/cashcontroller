import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges, ViewChild } from '@angular/core';
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
export class TableCarteiraPrincipalComponent implements OnChanges {
  
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
  ngOnChanges(changes: SimpleChanges): void {
    if(changes['ativosCarteira']) {
      this.getValorizacaoCarteira();
    }
  }
  
  carteira: AtivoCarteira[] = [];

  private getValorizacaoCarteira() : void {
      this.totalValorMercado = this.ativosCarteira.reduce((acc: any, value: any) => acc + value.valorMercado, 0);      
  }

  ngOnInit(): void {
    this.calcularRentabilidade();  
  }

  private calcularRentabilidade() {
    this.getValorizacaoCarteira();
  }

  colsCarteiraAtivo = [
    { field: 'ativo', header: 'Ativo', type: 'object'},
    { field: 'custodia', header: 'Custódia'},
    { field: 'custo', header: 'Custo', type: 'number'},
    { field: 'valorMercado', header: 'Valor de Mercado', type: 'number'},
    { field: 'percentual', header: 'Percentual', type: 'percentual'},
    { field: 'percentualAtual', header: 'Percentual Atual', type: 'percentual'},
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
        subclasse: this.subclasseAtivo
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
       this.excluirAtivo(ativo.id).subscribe((res) => {
          this.updateAtivosCarteira.emit();
      });
        this.messageService.add({
          severity: 'info',
          summary: 'Confirmado',
          detail: 'Confirmada a exclusão',
        });
      },
      reject: () => {        
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

  updateCarteiraPorSubclasse() {
    console.log('teste', this.subclasseAtivo);
    this.ativoCarteiraService.updateCarteiraBySubclasse(this.subclasseAtivo)
      .subscribe((res) => {
        this.calcularRentabilidade();
      });
  }
}
