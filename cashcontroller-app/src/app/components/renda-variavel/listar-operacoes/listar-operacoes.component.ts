import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { PrimengModule } from 'src/app/primeng/primeng.module';
import { ButtonModule } from 'primeng/button';
import { ConfirmationService, MenuItem, MessageService } from 'primeng/api';
import { DialogService, DynamicDialogModule, DynamicDialogRef } from 'primeng/dynamicdialog';
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
    IrpfMesComponent

  ],
  templateUrl: './listar-operacoes.component.html',
  styleUrl: './listar-operacoes.component.css',
  providers: [DialogService, ConfirmationService]
})
export class ListarOperacoesComponent implements OnChanges {

  item: MenuItem[] | undefined;
  ativos: Ativo[] = [];
  subclasses: SubclasseAtivo[] = [];
  anos: [] = [];
  visible = false;
  ref: DynamicDialogRef | undefined;
  cols!: Column[]
  operacoes: any[] = [];
  subclasse: number = 0;
  tiposOperacao: TipoOperacao[] = [];
  clonedOperacoes: { [s: string]: OperacaoRendaVariavel } = {};
  filter: FilterOperacao = new FilterOperacao();
  filtroChange: boolean = false;
  filtroMes$: Observable<any[]> | undefined;
  meses: Mes[] = [];
  mesSelecionado: number = 0;
  mesAtual: number = 0;
  selectedMonth: any;

  constructor(public dialogService: DialogService,
    public ativoService: AtivoService,
    public operacaoRendaVariavelService: OperacaoRendaVariavelService,
    public messageService: MessageService,
    private confirmationService: ConfirmationService
  ) { }

  ngOnInit() {


    this.buscarAtivos(EnumClasseAtivo.RENDA_VARIAVEL);
    this.buscarTiposOperacao();
    this.buscarSubclassesAtivos(EnumClasseAtivo.RENDA_VARIAVEL);
    this.buscarAnosComDespesas();

    this.getMeses(2024)
    this.filterData();
    this.cols = [
      { field: 'ativoDto', header: 'Ativo' },
      { field: 'quantidadeNegociada', header: 'Quantidade' },
      { field: 'dataOperacao', header: 'Data' },
      { field: 'valorUnitario', header: 'Valor Unitário' },
      { field: 'tipoOperacaoDto', header: 'Operação' },
      { field: 'valorCorretagem', header: 'Corretagem' },
      { field: 'valorTotal', header: 'Total' }

    ];
  }

  ngOnChanges(changes: SimpleChanges): void {
    console.log('mudei', changes)
  }
  showDialog() {
    this.ref = this.dialogService.open(RendaVariavelFormComponent, {
      header: 'Cadastro de Operação - Renda Variável',
      width: '50vw',
      modal: true,
      breakpoints: {
        '960px': '75vw',
        '640px': '90vw'
      },
    });

    this.ref.onClose.pipe(
      filter(val => !!val),
      tap(() => {
        this.filterData();
        this.messageService.add({ severity: 'success', summary: 'Success', detail: 'Operação Cadastrada' })

      })
    ).subscribe();
  }

  private buscarAtivos(id: number) {
    this.ativoService.getAtivosPorClasse(id).subscribe(
      ativos => { this.ativos = ativos }
    );
  }

  buscarAnosComDespesas() {
    this.operacaoRendaVariavelService.getAnosComOperacoes().subscribe((res) => {
      this.anos = res;
    });
  }


  // getMeses(ano?: number) {
  //   console.log('oioi')
  //   this.filtroMes$ = this.operacaoRendaVariavelService.getMesesComOperacoesPorAno(ano)
  //     .pipe(
  //       map((meses) => {          
  //         if (meses) {
  //           meses.forEach((element:any) => {
  //             element.mesString = element.mesString.slice(0, 3)
  //           });
  //           if (this.mesSelecionado === undefined) {
  //             this.mesSelecionado = this.mesAtual;
  //           }
  //           //compara o mês selecionado com os meses existentes para o ano e destaca com o selected
  //           meses.find((m:any) => m.mesInteiro == this.mesSelecionado).selected = true;

  //         }
  //         return meses;
  //       })
  //     )
  // }


  getLastDayOfMonth(year: number, month: number): Date {
    const nextMonth = new Date(year, month, 1);
    nextMonth.setDate(nextMonth.getDate() - 1);
    return nextMonth;
  }

  filtrarPorMes($event: any) {
    
    this.selectedMonth = $event.mesString;
    console.log(this.selectedMonth)
    this.filter.startDate = null;
    this.filter.ano = $event.ano;
    this.filter.mes = $event.mesInteiro;
    this.filter.endDate = this.getLastDayOfMonth(this.filter.ano, this.filter.mes);
    this.filterData();

  }

  getMeses(ano?: number) {

    this.filtroMes$ = this.operacaoRendaVariavelService.getMesesComOperacoesPorAno(ano).pipe(
      map((meses) => {
        if (meses) {
          meses.forEach(
            (element: any) => {             
              element.mesString = element.mesString.slice(0, 3);
            });           
        }
        return meses;
      }
      ))


  // .pipe(
  //   map((meses) => {          
  //     if (meses) {
  //       meses.forEach((element:any) => {
  //         element.mesString = element.mesString.slice(0, 3)
  //       });
  //       if (this.mesSelecionado === undefined) {
  //         this.mesSelecionado = this.mesAtual;
  //       }
  //       //compara o mês selecionado com os meses existentes para o ano e destaca com o selected
  //       meses.find((m:any) => m.mesInteiro == this.mesSelecionado).selected = true;

  //     }
  //     return meses;
  //   })
  // )
}
buscarSubclassesAtivos(id: number) {
  this.ativoService.getSubclasseAtivos().subscribe(
    sc => {
      this.subclasses = sc.filter(c => c.classeAtivo.id === id)
    }
  );
}

  private buscarTiposOperacao() {
  this.operacaoRendaVariavelService.getTipoOperacoes()
    .subscribe(
      tipos => this.tiposOperacao = tipos
    );
}

  private listarOperacoes() {
  this.operacaoRendaVariavelService.getOperacoesRendaVariavel()
    .subscribe(
      (ops: any[]) => {
        this.operacoes = ops
      }
    );
}

  private listarOperacoesPorSubclasse(subclasse: number) {
  this.operacaoRendaVariavelService.getOperacoesRendaVariavel()
    .subscribe(
      (ops: any[]) => {
        this.operacoes = ops.filter(op => op.ativoDto.subclasseAtivoDto.id == subclasse)
      }
    );
}

onRowEditInit(operacao: OperacaoRendaVariavel) {
  console.log('open edit', operacao);
  this.clonedOperacoes[operacao.id as string] = { ...operacao };
  const selectedAtivo = this.ativos.find(ativo => ativo.id === operacao.ativoDto.id)!;
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

    this.operacaoRendaVariavelService.editar(operacao)
      .subscribe({
        next: () => {
          this.filterData();
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
      this.excluirOperacao(operacao.id).subscribe(() => this.filterData());
      this.messageService.add({ severity: 'info', summary: 'Confirmado', detail: 'Confirmada a exclusão' });
    },
    reject: () => {
      this.listarOperacoes();
      this.messageService.add({ severity: 'error', summary: 'Rejeitado', detail: 'Cancelada a exclusão', life: 3000 });
    }
  });
}

excluirOperacao(id: string) {
  return this.operacaoRendaVariavelService.excluir(id);
}


filterData() {
 if(this.filter.startDate != null) {
    this.filter.ano = 0;
    this.filter.mes = 0
 }
  return this.operacaoRendaVariavelService.filter(this.filter)
    .subscribe((x: any) => {
      this.operacoes = x,
        this.filtroChange = !this.filtroChange;
    });
}

}