import {
  Component,
  EventEmitter,
  Input,
  NgModule,
  OnChanges,
  Output,
  SimpleChanges,
} from '@angular/core';
import { PrimengModule } from 'src/app/primeng/primeng.module';
import { ButtonModule } from 'primeng/button';
import { ConfirmationService, MenuItem, MessageService } from 'primeng/api';
import {
  DialogService,
  DynamicDialogModule,
  DynamicDialogRef,
} from 'primeng/dynamicdialog';
import { RendaVariavelFormComponent } from '../renda-variavel-form/renda-variavel-form.component';
import { OperacaoRendaVariavelService } from 'src/app/services/operacao-renda-variavel.service';
import { OperacaoRendaVariavel } from 'src/app/models/operacao-renda-variavel.model';
import { Column } from 'src/app/interfaces/column';
import { ToastModule } from 'primeng/toast';
import { AtivoService } from 'src/app/services/ativo.service';
import { Ativo } from 'src/app/models/ativo.model';
import { DropdownModule } from 'primeng/dropdown';
import { FormsModule } from '@angular/forms';
import { CalendarModule } from 'primeng/calendar';
import { TipoOperacao } from 'src/app/models/tipo-operacao.model';
import { Observable, filter, map, of, tap } from 'rxjs';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { InputTextModule } from 'primeng/inputtext';
import { InputNumberModule } from 'primeng/inputnumber';
import { SubclasseAtivo } from 'src/app/models/subclasse-ativo.model';
import { EnumClasseAtivo } from 'src/app/enums/classe-ativo.enum';
import { FilterOperacao } from 'src/app/models/filter-operacao.model';
import { IrpfMesComponent } from '../irpf-mes/irpf-mes.component';
import { Mes } from 'src/app/models/dto/mes';
import { FiltroSuperiorComponent } from '../../filtro-superior/filtro-superior.component';
import { DateUtil } from 'src/app/shared/util/date-util';
import { EventoRendaVariavel } from 'src/app/models/evento-renda-variavel.model';
import { EventoRvFormComponent } from 'src/app/src/app/components/renda-variavel/evento-rv-form/evento-rv-form.component';
import { EventoRendaVariavelService } from 'src/app/services/evento-renda-variavel.service';

@Component({
  selector: 'app-listar-renda-variavel',
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
    IrpfMesComponent,
    FiltroSuperiorComponent,
  ],

  templateUrl: './listar-operacoes.component.html',
  styleUrl: './listar-operacoes.component.css',
  providers: [DialogService, ConfirmationService],
})
export class ListarOperacoesComponent {
  item: MenuItem[] | undefined;
  ativos: Ativo[] = [];
  subclasses: SubclasseAtivo[] = [];
  visible = false;
  ref: DynamicDialogRef | undefined;
  refEvento: DynamicDialogRef | undefined;
  cols!: Column[];

  operacoes: any[] = [];

  eventos: any[] = [];
  subclasse: number = 0;
  tiposOperacao: TipoOperacao[] = [];
  clonedOperacoes: { [s: string]: OperacaoRendaVariavel } = {};
  filter: FilterOperacao = new FilterOperacao();
  filtroChange: boolean = false;

  meses: Mes[] = [];
  mesSelecionado: number = 0;
  mesAtual: number = 0;
  activeIndex: number = 0;

  constructor(
    public dialogService: DialogService,
    public ativoService: AtivoService,
    public operacaoRendaVariavelService: OperacaoRendaVariavelService,
    private eventoRendaVariavelService: EventoRendaVariavelService,
    public messageService: MessageService,
    private confirmationService: ConfirmationService
  ) {}

  colsEventos = [
    { field: 'ativo', header: 'Ativo', type: 'objeto' },
    { field: 'dataCom', header: 'Data Com', type: 'date' },
    { field: 'dataPagamento', header: 'Data do Pagamento', type: 'date' },
    { field: 'tipoEvento', header: 'Evento', type: 'objeto' },
    { field: 'valor', header: 'Valor Unitário' },
    { field: 'valorTotal', header: 'Valor a ser Pago' },
  ];

  ngOnInit() {
    this.buscarAtivos(EnumClasseAtivo.RENDA_VARIAVEL);
    this.buscarTiposOperacao();
    this.buscarSubclassesAtivos(EnumClasseAtivo.RENDA_VARIAVEL);
    this.listarEventos();
    this.filterData();
    this.filterEventos();

    this.cols = [
      { field: 'ativoDto', header: 'Ativo' },
      { field: 'quantidadeNegociada', header: 'Quantidade' },
      { field: 'dataOperacao', header: 'Data' },
      { field: 'valorUnitario', header: 'Valor Unitário' },
      { field: 'tipoOperacaoDto', header: 'Operação' },
      { field: 'valorCorretagem', header: 'Corretagem' },
      { field: 'valorTotal', header: 'Total' },
      { field: 'custoTotal', header: 'Custo' },
    ];
  }

  showDialog() {
    this.ref = this.dialogService.open(RendaVariavelFormComponent, {
      header: 'Cadastro de Operação - Renda Variável',
      width: '50vw',
      modal: true,
      breakpoints: {
        '960px': '75vw',
        '640px': '90vw',
      },
    });

    this.ref.onClose
      .pipe(
        filter((val) => !!val),
        tap((val) => {
          this.filter.startDate = null;
          this.filter.ano = val.dataOperacao.getFullYear();
          this.filter.mes = DateUtil.getMonthNumber(val.dataOperacao);
          this.messageService.add({
            severity: 'success',
            summary: 'Success',
            detail: 'Operação Cadastrada',
          });
        })
      )
      .subscribe((val) => this.filterData());
  }

  showEventoDialog(dados?: any) {
    console.log('dados', dados);
    this.ref = this.dialogService.open(EventoRvFormComponent, {
      header: 'Cadastro de Evento - Renda Variável',
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

    this.ref.onClose.subscribe((val) => this.filterEventos());
  }

  filtrarPorDataEspecifica(): void {
    if (this.filter.startDate != null && this.filter.endDate != null) {
      this.filter.ano = 0;
      this.filter.mes = 0;
    }
  }

  private buscarAtivos(id: number) {
    this.ativoService.getAtivosPorClasse(id).subscribe((ativos) => {
      this.ativos = ativos;
    });
  }

  filtrarPorMes($event: any) {
    this.filter.startDate = null;
    this.filter.ano = $event.ano;
    this.filter.mes = $event.mesInteiro;
    this.filter.endDate = DateUtil.getLastDayOfMonthByYear(
      this.filter.ano,
      this.filter.mes
    );
    this.filterData();
    this.filterEventos();
  }

  buscarSubclassesAtivos(id: number) {
    this.ativoService.getSubclasseAtivos().subscribe((sc) => {
      this.subclasses = sc.filter((c) => c.classeAtivo.id === id);
    });
  }

  private buscarTiposOperacao() {
    this.operacaoRendaVariavelService
      .getTipoOperacoes()
      .subscribe((tipos) => (this.tiposOperacao = tipos));
  }

  private listarOperacoes() {
    this.operacaoRendaVariavelService
      .getOperacoesRendaVariavel()
      .subscribe((ops: any[]) => {
        this.operacoes = ops;
      });
  }

  private listarEventos() {
    this.eventoRendaVariavelService
      .getEventosRendaVariavel()
      .subscribe((eventos: any[]) => {
        this.eventos = eventos;
      });
  }

  private listarOperacoesPorSubclasse(subclasse: number) {
    this.operacaoRendaVariavelService
      .getOperacoesRendaVariavel()
      .subscribe((ops: any[]) => {
        this.operacoes = ops.filter(
          (op) => op.ativoDto.subclasseAtivoDto.id == subclasse
        );
      });
  }

  onRowEditInit(operacao: OperacaoRendaVariavel) {
    this.clonedOperacoes[operacao.id as string] = { ...operacao };
    const selectedAtivo = this.ativos.find(
      (ativo) => ativo.id === operacao.ativoDto.id
    )!;
    operacao.dataOperacao = new Date(operacao.dataOperacao);
    operacao.ativoDto = selectedAtivo;
  }

  onRowEditCancel(operacao: OperacaoRendaVariavel, index: number) {
    this.operacoes[index] = this.clonedOperacoes[operacao.id as string];
    delete this.clonedOperacoes[operacao.id as string];
  }

  onRowEditSave(operacao: any) {
    if (operacao.valorUnitario > 0) {
      delete this.clonedOperacoes[operacao.id as string];

      this.operacaoRendaVariavelService.editar(operacao).subscribe({
        next: () => {
          this.filterData();
          this.messageService.add({
            severity: 'success',
            summary: 'Success',
            detail: 'Operação Atualizada',
          });
        },
        error: (err) =>
          this.messageService.add({
            severity: 'error',
            summary: 'Error',
            detail: 'Valor inválido' + err,
          }),
      });
    }
  }

  validarExclusao(operacao: any, event: Event, key: string) {
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
      reject: (close: any) => {
        this.filterData();
        this.messageService.add({
          severity: 'error',
          summary: 'Rejeitado',
          detail: 'Cancelada a exclusão',
          life: 3000,
        });
        console.log(close);
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
      reject: (close:any) => {
        this.filterEventos();
        this.messageService.add({
          severity: 'error',
          summary: 'Rejeitado',
          detail: 'Cancelada a exclusão',
          life: 3000,
        });
        
        console.log(close);
      },
    });
  }

  excluirOperacao(id: string) {
    return this.operacaoRendaVariavelService.excluir(id);
  }

  excluirEvento(id: string) {
    return this.eventoRendaVariavelService.excluir(id);
  }

  filterData() {
    this.filtrarPorDataEspecifica();
    return this.operacaoRendaVariavelService
      .filter(this.filter)
      .subscribe((res: any) => {
        (this.operacoes = res), console.log('ano ', this.filter.ano);
        console.log('meses: ', this.filter.mes);
        this.filtroChange = !this.filtroChange;
      });
  }

  filterEventos() {
    this.filtrarPorDataEspecifica();
    return this.eventoRendaVariavelService
      .filter(this.filter)
      .subscribe((res: any) => {
        (this.eventos = res), console.log('ano ', this.filter.ano);
        console.log('meses: ', this.filter.mes);
        this.filtroChange = !this.filtroChange;
      });
  }
}
