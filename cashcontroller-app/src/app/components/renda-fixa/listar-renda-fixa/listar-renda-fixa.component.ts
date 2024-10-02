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
import { FiltroSuperiorComponent } from '../../filtro-superior/filtro-superior.component';
import { FilterOperacao } from 'src/app/models/filter-operacao.model';
import { FiltroOperacaoService } from 'src/app/services/filtro-operacao.service';
import { EventoRendaFixaFormComponent } from '../evento-renda-fixa-form/evento-renda-fixa-form.component';
import { DateUtil } from 'src/app/shared/util/date-util';
import { EventoRendaFixaService } from 'src/app/services/evento-renda-fixa.service';

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
    InputNumberModule,
    FiltroSuperiorComponent
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
  filter: FilterOperacao = new FilterOperacao();
  filtroChange: boolean = false;
  activeIndex: number = 0;
  eventos: any[] = [];

  constructor(public dialogService: DialogService,
    public ativoService: AtivoService,
    public indexadorService: IndexadorService,
    public operacaoRendaFixaService: OperacaoRendaFixaService,
    public eventoRendaFixaService: EventoRendaFixaService,
    public messageService: MessageService,
    private confirmationService: ConfirmationService,
    private filterService: FiltroOperacaoService
  ) { }

  ngOnInit() {   
    
    //this.listarOperacoes();   
    this.buscarAtivos(EnumClasseAtivo.RENDA_FIXA);
    this.buscarTiposOperacao();
    this.buscarIndexadores();
    this.filterData();
    this.filterEventos();

    this.cols = [
      { field: 'ativoDto', header: 'Ativo' },
      { field: 'quantidadeNegociada', header: 'Quantidade' },
      { field: 'dataOperacao', header: 'Data' },
      { field: 'valorUnitario', header: 'Valor Unitário' },
      { field: 'tipoOperacaoDto', header: 'Operação' },
      { field: 'valorCorretagem', header: 'Corretagem' },      
      { field: 'taxaContratada', header: 'Taxa' },
      { field: 'custoTotal', header: 'Custo' },
      { field: 'valorTotal', header: 'Valor' },
      
    ];
  }

  colsEventos = [
    { field: 'ativo', header: 'Ativo', type: 'ativo' },  
    { field: 'dataPagamento', header: 'Data do Pagamento', type: 'date' },
    { field: 'tipoEvento', header: 'Evento', type: 'objeto' },
    { field: 'valor', header: 'Valor Unitário', type: 'number' },
    { field: 'valorTotal', header: 'Valor a ser Pago', type: 'number' },
  ];

  showEventoDialog(dados?: any) {
    console.log('tt ',dados)
    this.ref = this.dialogService.open(EventoRendaFixaFormComponent, {
      header: 'Cadastro de Evento - Renda Fixa',
      width: '50vw',
      modal: true,
      breakpoints: {
        '960px': '75vw',
        '640px': '90vw',
      },
      data: {
        isEdit: dados === undefined ? false : true,
        rowData: dados,
      },
    });

    this.ref.onClose
    .pipe(
      filter((val) => !!val),
      tap((val) => {
        this.filter.startDate = null;
        this.filter.ano = val.dataPagamento.getFullYear();
        this.filter.mes = DateUtil.getMonthNumber(val.dataPagamento);
        this.messageService.add({
          severity: 'success',
          summary: 'Successo',
          detail: 'Evento Cadastrado',
        });
      })
    ).subscribe(
      (val) => {
        this.filterEventos();
        this.filterData();
      }
      
    );
  }

  filterEventos() {
    this.filterService.filtrarPorDataEspecifica(this.filter);
    return this.eventoRendaFixaService
      .filter(this.filter)
      .subscribe((res: any) => {
        (this.eventos = res);        
        this.filtroChange = !this.filtroChange;
      });
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
          this.operacoes = ops;
          console.log(ops)

        }
      );
  }

  onRowEditInit(operacao: OperacaoRendaFixa) {
   
     this.clonedOperacoes[operacao.id as string] = { ...operacao };
     const selectedAtivo = this.ativos.find(ativo => ativo.id === operacao.ativoDto.id)!;
     operacao.dataOperacao = new Date(operacao.dataOperacao);     
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
                this.listarOperacoes();          
                this.messageService.add({ severity: 'success', summary: 'Success', detail: 'Operação Atualizada' })
              },
              error: err => this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Valor inválido' + err })
            }
          );
    };
  }

  validarExclusaoOperacao(operacao: any, event: Event, key: string) {
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
        this.excluirOperacao(operacao.id).subscribe(() => this.filterData());
        this.messageService.add({
          severity: 'info',
          summary: 'Confirmado',
          detail: 'Confirmada a exclusão',
        });
      },
      reject: () => {
        this.filterData();
        this.messageService.add({
          severity: 'error',
          summary: 'Rejeitado',
          detail: 'Cancelada a exclusão',
          life: 3000,
        });
        
      },
    });
  }

  validarExclusaoEvento(evento: any, event: Event, key: string) {
    this.confirmationService.confirm({
      key: key,
      target: event.target as EventTarget,
      message: 'Tem certeza que deseja excluir este evento?',
      header: 'Confirmação',
      icon: 'pi pi-exclamation-triangle',
      acceptIcon: 'none',
      rejectIcon: 'none',
      acceptLabel: 'Sim',
      rejectLabel: 'Não',      
      acceptButtonStyleClass: 'p-button-danger',
      accept: () => {
        this.excluirEvento(evento.id).subscribe(() => this.filterEventos());
        this.messageService.add({
          severity: 'info',
          summary: 'Confirmado',
          detail: 'Confirmada a exclusão',
        });
      },
      reject: () => {
        this.filterEventos();
        this.messageService.add({
          severity: 'error',
          summary: 'Rejeitado',
          detail: 'Cancelada a exclusão',
          life: 3000,
        });       
        
      },
    });
  }

  excluirEvento(id: string) {
    return this.eventoRendaFixaService.excluir(id);
  }
  excluirOperacao(id: string) {
    return this.operacaoRendaFixaService.excluir(id);
  }

  filtrarPorMes($event: any) {
    this.filterService.filtrarPorMes($event, this.filter);    
    this.filterData();    
    this.filterEventos();
  }

  filterData() {
    this.filterService.filtrarPorDataEspecifica(this.filter);
    return this.operacaoRendaFixaService
      .filter(this.filter)
      .subscribe((res: any) => {
        (this.operacoes = res), console.log('ano ', this.filter.ano);       
        this.filtroChange = !this.filtroChange;
      });
  }


}