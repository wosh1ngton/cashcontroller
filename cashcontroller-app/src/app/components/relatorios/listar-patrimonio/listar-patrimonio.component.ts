import { Component, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { AtivoCarteiraService } from 'src/app/services/ativo-carteira.service';

@Component({
  selector: 'app-listar-patrimonio',
  templateUrl: './listar-patrimonio.component.html',
  styleUrl: './listar-patrimonio.component.css',
})
export class ListarPatrimonioComponent implements OnInit {
  patrimonio: any;
  valorTotal: number = 0;
  totalProventosRendimentos: number = 0;
  totalRendimentos: number = 0;
  totalProventos: number = 0;

  dataTopPagadoras: any[] = [];
  labelTopPagadoras: any[] = [];
  optionsTopPagadoras: any;
  data: any;
  options: any;
  allDataTopPagadoras: any[] = [];
  constructor(private ativoCarteiraService: AtivoCarteiraService) {}
  colsPatrimonio = [
    { field: 'categoria', header: 'Categoria', type: 'string' },
    { field: 'valor', header: 'Valor de Mercado', type: 'number' },
    { field: 'percentual', header: 'Percentual', type: 'percentual' },
    {
      field: 'percentualAtual',
      header: 'Percentual Atual',
      type: 'percentual',
    },
    { field: 'investimento', header: 'Valor a Investir (reduzir)' },
  ];

  alterarVisualizacao(categoria?: string) {
    if (categoria === 'acao') {
      this.dataTopPagadoras = this.allDataTopPagadoras.filter(
        (item) => item.subclasseAtivoId == 2
      );
    } else if (categoria == 'fii') {
      this.dataTopPagadoras = this.allDataTopPagadoras.filter(
        (item) => item.subclasseAtivoId == 1
      );
    } else {
      this.dataTopPagadoras = this.allDataTopPagadoras;
    }
    console.log(this.allDataTopPagadoras);
  }

  ngOnInit(): void {
    this.getTopProventos();
    const documentStyle = getComputedStyle(document.documentElement);
    const textColor = documentStyle.getPropertyValue('--text-color');
    const textColorSecondary = documentStyle.getPropertyValue(
      '--text-color-secondary'
    );
    const surfaceBorder = documentStyle.getPropertyValue('--surface-border');
    this.buscarPatrimonio();
    this.ativoCarteiraService.getProventos().subscribe((res: any) => {
      const groupedData = res.reduce((acc: any, current: any) => {
        const { anoMes, valor, subclasseId } = current;

        if (!acc[anoMes]) {
          acc[anoMes] = { anoMes, values: {} };
        }

        if (!acc[anoMes].values[subclasseId]) {
          acc[anoMes].values[subclasseId] = 0;
        }

        acc[anoMes].values[subclasseId] += valor;
        return acc;
      }, {});

      const labels = Object.keys(groupedData); 
      const subclasseIds = Array.from(
        new Set(res.map((item: any) => item.subclasseId))
      );
      this.totalProventosRendimentos = res.reduce(
        (acc: any, value: any) => acc + value.valor,
        0
      );

      const datasets = subclasseIds.map((subclasseId: any) => {
        return {
          label: subclasseId === 1 ? 'Fiis' : 'Ações',
          backgroundColor:
            subclasseId === 1
              ? documentStyle.getPropertyValue('--blue-500')
              : documentStyle.getPropertyValue('--green-500'),
          borderColor:
            subclasseId === 1
              ? documentStyle.getPropertyValue('--blue-500')
              : documentStyle.getPropertyValue('--green-500'),
          data: labels.map(
            (anoMes: any) => groupedData[anoMes].values[subclasseId] || 0
          ),
        };
      });
      this.data = {
        labels, // Unique anoMes values
        datasets,
      };
    });

    this.options = {
      maintainAspectRatio: false,
      aspectRatio: 0.8,
      plugins: {
        legend: {
          labels: {
            color: textColor,
          },
        },
      },
      scales: {
        x: {
          stacked: true,
          ticks: {
            color: textColorSecondary,
            font: {
              weight: 500,
            },
          },
          grid: {
            color: surfaceBorder,
            drawBorder: false,
          },
        },
        y: {
          stacked: true,
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

  private getTopProventos() {
    this.ativoCarteiraService
      .getTopPagadoras()
      .subscribe(
        (res: any) => (
          (this.dataTopPagadoras = res),
          (this.allDataTopPagadoras = this.dataTopPagadoras),
          (this.totalProventos = this.dataTopPagadoras
            .filter((item) => item.subclasseAtivoId === 2)
            .reduce((acc, value) => acc + value.valor, 0)),
          (this.totalRendimentos = this.allDataTopPagadoras
            .filter((item) => item.subclasseAtivoId == 1)
            .reduce((acc, value) => acc + value.valor, 0))
        )
      );
  }

  private buscarPatrimonio() {
    this.ativoCarteiraService.getPatrimonio().subscribe((res: any) => {
      this.patrimonio = res;
      this.valorTotal = res.reduce(
        (acc: any, value: any) => acc + value.valor,
        0
      );
    });
  }
}
