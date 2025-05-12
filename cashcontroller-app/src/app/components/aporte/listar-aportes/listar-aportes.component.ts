import { Component, OnInit } from '@angular/core';
import { ConfirmationService, MessageService } from 'primeng/api';
import { DialogService, DynamicDialogRef } from 'primeng/dynamicdialog';
import { CadastrarAporteComponent } from '../cadastrar-aporte/cadastrar-aporte.component';
import { Aporte } from 'src/app/models/aporte.model';
import { AporteService } from 'src/app/services/aporte.service';
import { Observer } from 'rxjs';
import { DateUtil } from 'src/app/shared/util/date-util';

@Component({
  selector: 'app-listar-aportes',
  templateUrl: './listar-aportes.component.html',
  styleUrl: './listar-aportes.component.css',
  providers: [DialogService, ConfirmationService],
})
export class ListarAportesComponent implements OnInit {
  dialogRef: DynamicDialogRef | undefined;
  aportes: Aporte[] = [];
  cadastrarAporte: CadastrarAporteComponent | undefined;
  CadastrarAporteComponent = CadastrarAporteComponent;
  dataGrafico: any = {};
  optionsGrafico: any;

  ngOnInit(): void {
    this.listarAportes();
  }

  constructor(
    public dialogService: DialogService,
    public confirmationService: ConfirmationService,
    public aporteService: AporteService,
    private messageService: MessageService
  ) {}

  showDialogCadastrarAporte() {
    this.dialogRef = this.dialogService.open(CadastrarAporteComponent, {
      header: 'Cadastro de Aporte',
      width: '50vw',
      modal: true,
    });

    this.dialogRef.onClose.subscribe((val) => {
      this.listarAportes();
    });
  }

  listarAportes() {
    this.aporteService.getAll().subscribe((res: any) => {
      this.aportes = res;
      this.initChart();
    });
  }

  editar(id: number) {}

  excluirAtivo(id: number) {
    return this.aporteService.excluir(id);
  }
  validarExclusaoAtivo(ativo: any, event: Event, key: string) {
    console.log(ativo);
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
        //  this.excluirAtivo(ativo.id).subscribe(() => this.listarAtivosRendaFixa());
        //   this.messageService.add({
        //     severity: 'info',
        //     summary: 'Confirmado',
        //     detail: 'Confirmada a exclusão',
        //   });
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

  initChart() {
    const documentStyle = getComputedStyle(document.documentElement);
    const textColor = documentStyle.getPropertyValue('--p-text-color');
    const textColorSecondary = documentStyle.getPropertyValue(
      '--p-text-muted-color'
    );
    const surfaceBorder = documentStyle.getPropertyValue(
      '--p-content-border-color'
    );
    const sortedAportes = this.aportes.sort(
      (a, b) =>
        new Date(a.dataAporte).getTime() - new Date(b.dataAporte).getTime()
    );

   

    let accumulator = 0;
    const accumulatedValues = sortedAportes.map((aporte) => {
      accumulator += aporte.valorAporte;
      return accumulator;
    });

    let accumulatorResultado = 0;
    const accumulatedResultado = sortedAportes.map((aporte) => {
      accumulatorResultado += aporte.resultadoMes;
      return accumulatorResultado;
    });

    const patrimonio = accumulatedResultado.map((value,index) => value + accumulatedValues[index])

    this.dataGrafico.labels = this.aportes.map((label) => label.dataAporte);
    this.dataGrafico.datasets = [
      {
        type: 'bar',
        label: 'Valor Aporte',
        data: sortedAportes.map((aporte) => aporte.valorAporte),
        backgroundColor: documentStyle.getPropertyValue('--blue-500'),
        borderColor: documentStyle.getPropertyValue('--blue-500'),
        borderWidth: 1,
      },
      {
        type: 'line',
        label: 'Valor Acumulado',
        data: accumulatedValues,
        backgroundColor: documentStyle.getPropertyValue('--red-500'),
        borderColor: documentStyle.getPropertyValue('--red-500'),
        borderWidth: 1,
        fill: false,
      },
      {
        type: 'line',
        label: 'Resultado Acumulado',
        data: accumulatedResultado,
        backgroundColor: documentStyle.getPropertyValue('--green-500'),
        borderColor: documentStyle.getPropertyValue('--green-500'),
        borderWidth: 1,
        fill: false,
      },
      {
        type: 'line',
        label: 'Patrimônio',
        data: patrimonio,
        backgroundColor: documentStyle.getPropertyValue('--blue-500'),
        borderColor: documentStyle.getPropertyValue('--blue-500'),
        borderWidth: 1,
        fill: false,
      },
    ];

    this.optionsGrafico = {
      scales: {
        x: {
          ticks: {
            color: textColorSecondary,
          },
          grid: {
            color: surfaceBorder,
            drawBorder: false,
          },
        },
        y: {
          ticks: {
            color: textColorSecondary,
          },
          grid: {
            color: surfaceBorder,
            drawBorder: false,
          },
        },
      },
    };
  }
}
