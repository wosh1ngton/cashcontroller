import { Component } from '@angular/core';
import { PrimengModule } from 'src/app/primeng/primeng.module';
import { ButtonModule } from 'primeng/button';
import { ConfirmationService, MenuItem, MessageService } from 'primeng/api';
import { DialogService, DynamicDialogModule, DynamicDialogRef } from 'primeng/dynamicdialog';
import { Column } from 'src/app/interfaces/column';
import { ToastModule } from 'primeng/toast';
import { AtivoService } from 'src/app/services/ativo.service';
import { Ativo } from 'src/app/models/ativo.model';
import { DropdownModule } from 'primeng/dropdown';
import { FormsModule } from '@angular/forms';
import { CalendarModule } from 'primeng/calendar';
import { TipoOperacao } from 'src/app/models/tipo-operacao.model';
import { filter, tap } from 'rxjs';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { InputTextModule } from 'primeng/inputtext';
import { InputNumberModule } from 'primeng/inputnumber';
import { OperacaoRendaFixa } from 'src/app/models/operacao-renda-fixa.model';
import { OperacaoRendaFixaService } from 'src/app/services/operacao-renda-fixa.service';
import { RendaFixaFormComponent } from '../renda-fixa-form/renda-fixa-form.component';
import { Indexador } from 'src/app/models/indexador.model';
import { IndexadorService } from 'src/app/services/indexador.service';
import { EnumClasseAtivo } from 'src/app/enums/classe-ativo.enum';

@Component({
  selector: 'app-listar-renda-fixa',
  standalone: true,
  imports: [
    PrimengModule,
    ButtonModule,
    DynamicDialogModule,
    ToastModule,
    DropdownModule,
    FormsModule,
    CalendarModule,
    ConfirmDialogModule,
    InputTextModule,
    InputNumberModule
  ],
  templateUrl: './listar-renda-fixa.component.html',
  styleUrl: './listar-renda-fixa.component.css',
  providers: [DialogService, ConfirmationService]
})
export class ListarRendaFixaComponent {

  item: MenuItem[] | undefined;
  ativos: Ativo[] = [];
  indices: Indexador[] = [];
  visible = false;
  ref: DynamicDialogRef | undefined;
  cols!: Column[]
  operacoes: any[] = [];
  tiposOperacao: TipoOperacao[] = [];
  clonedOperacoes: { [s: string]: OperacaoRendaFixa } = {};

  constructor(public dialogService: DialogService,
    public ativoService: AtivoService,
    public indexadorService: IndexadorService,
    public operacaoRendaFixaService: OperacaoRendaFixaService,
    public messageService: MessageService,
    private confirmationService: ConfirmationService
  ) { }

  ngOnInit() {
    
    this.listarOperacoes();
    this.buscarAtivos(EnumClasseAtivo.RENDA_FIXA);
    this.buscarTiposOperacao();
    this.buscarIndexadores();

    this.cols = [
      { field: 'ativoDto', header: 'Ativo' },
      { field: 'quantidadeNegociada', header: 'Quantidade' },
      { field: 'dataOperacao', header: 'Data' },
      { field: 'valorUnitario', header: 'Valor Unitário' },
      { field: 'tipoOperacaoDto', header: 'Operação' },
      { field: 'valorCorretagem', header: 'Corretagem' },
      { field: 'dataVencimento', header: 'Vencimento' },
      { field: 'taxaContratada', header: 'Taxa' },
      { field: 'indexadorDto', header: 'Índice' }
    ];
  }

  showDialog() {
    this.ref = this.dialogService.open(RendaFixaFormComponent, {
      header: 'Cadastro de Operação - Renda Fixa',
      width: '50vw',
      height: '60vw',
      modal: true,
      breakpoints: {
        '960px': '75vw',
        '640px': '90vw'
      },
    });

    this.ref.onClose.pipe(
      filter(val => !!val),
      tap(() => {
        this.listarOperacoes();
        this.messageService.add({ severity: 'success', summary: 'Success', detail: 'Operação Cadastrada' })

      })
    ).subscribe();
  }

  private buscarAtivos(id: number) {
    this.ativoService.getAtivosPorClasse(id).subscribe(
      ativos => { this.ativos = ativos }
    );
  }

  private buscarIndexadores() {
    this.indexadorService.getIndexadores().subscribe(
      index => { this.indices = index }
    );
  }

  private buscarTiposOperacao() {
    this.operacaoRendaFixaService.getTipoOperacoes()
      .subscribe(
        tipos => this.tiposOperacao = tipos
      );
  }

  private listarOperacoes() {
    this.operacaoRendaFixaService.getOperacoesRendaFixa()
      .subscribe(
        (ops: any[]) => {
          this.operacoes = ops
        }
      );
  }

  onRowEditInit(operacao: OperacaoRendaFixa) {
   
     this.clonedOperacoes[operacao.id as string] = { ...operacao };
     const selectedAtivo = this.ativos.find(ativo => ativo.id === operacao.ativoDto.id)!;
     operacao.dataOperacao = new Date(operacao.dataOperacao);
     operacao.dataVencimento = new Date(operacao.dataVencimento);
     operacao.ativoDto = selectedAtivo;
  

  }

  onRowEditCancel(operacao: OperacaoRendaFixa, index: number) {
      this.operacoes[index] = this.clonedOperacoes[operacao.id as string];
      delete this.clonedOperacoes[operacao.id as string];
  }

  onRowEditSave(operacao: any) {
    if (operacao.valorUnitario > 0) {     

      delete this.clonedOperacoes[operacao.id as string];

      this.operacaoRendaFixaService.editar(operacao)        
          .subscribe({
              next: () => {             
                this.messageService.add({ severity: 'success', summary: 'Success', detail: 'Operação Atualizada' })
              },
              error: err => this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Valor inválido' + err })
            }
          );
    };
  }

  validarExclusao(operacao: any, event: Event) {

    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: 'Tem certeza que deseja excluir esta operação?',
      header: 'Confirmação',
      icon: 'pi pi-exclamation-triangle',
      acceptIcon: "none",
      rejectIcon: "none",
      rejectButtonStyleClass: "p-button-text",
      accept: () => {
        this.excluirOperacao(operacao.id).subscribe(() => this.listarOperacoes());
        this.messageService.add({ severity: 'info', summary: 'Confirmado', detail: 'Confirmada a exclusão' });
      },
      reject: () => {
        this.listarOperacoes();
        this.messageService.add({ severity: 'error', summary: 'Rejeitado', detail: 'Cancelada a exclusão', life: 3000 });
      }
    });
  }

  excluirOperacao(id: string) {
    return this.operacaoRendaFixaService.excluir(id);
  }


}