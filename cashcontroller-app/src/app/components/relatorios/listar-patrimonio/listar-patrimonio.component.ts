import { Component, OnInit } from '@angular/core';
import { AtivoCarteiraService } from 'src/app/services/ativo-carteira.service';
import { mediaMovel } from '../../shared/util/funcoes-matematica';

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

    this.ativoCarteiraService.getProventos().subscribe((res: any[]) => {
      
      const groupedData = this.getData(res);
      const paddedMediaMovel = this.calculaMediaMovel12Meses(groupedData);
      const labels = Object.keys(groupedData);
      const subclasseIds = Array.from(new Set(res.map((item: any) => item.subclasseId)));

      this.totalProventosRendimentos = res.reduce((acc: any, value: any) => acc + value.valor, 0);
      const blueHex = documentStyle.getPropertyValue('--blue-500').trim();
      const greenHex = documentStyle.getPropertyValue('--green-500').trim();

      const datasets = subclasseIds.map((subclasseId: any) => {
        return {
          type: 'bar',
          label: subclasseId === 1 ? 'Fiis' : 'Ações',
          backgroundColor:
            subclasseId === 1
              ? this.hexToRgba(blueHex,0.3)
              : this.hexToRgba(greenHex,0.3),
          borderColor:
            subclasseId === 1
              ? documentStyle.getPropertyValue('--blue')
              : documentStyle.getPropertyValue('--green-900'),
          data: labels.map(
            (anoMes: any) => groupedData[anoMes].values[subclasseId] || 0
          ),
          stack: 'stack1',
        };
      });

      (datasets as any[]).push({
        type: 'line',
        label: 'Média Móvel 12 meses',
        borderColor: documentStyle.getPropertyValue('--gray-500'),
        backgroundColor: documentStyle.getPropertyValue('--gray-500'),
        borderWidth: 3,
        fill: false,
        tension: 0.4,
        data: paddedMediaMovel,
        order: 100, 
        pointRadius: 3,
        pointHoverRadius: 5,
        pointBackgroundColor: documentStyle.getPropertyValue('--gray-500'),
        yAxisID: 'y', 
      });

      this.data = {
        labels,
        datasets,
      };
    });

    this.options = {
      maintainAspectRatio: false,
      aspectRatio: 0.6,
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

  private hexToRgba(hex: string, alpha: number): string {
    const r = parseInt(hex.slice(1, 3), 16);
    const g = parseInt(hex.slice(3, 5), 16);
    const b = parseInt(hex.slice(5, 7), 16);
    return `rgba(${r}, ${g}, ${b}, ${alpha})`;
  }
  private calculaMediaMovel12Meses(groupedData: any) {
    const arr = Object.values(groupedData);
    const mediaMovel12Meses: any = mediaMovel(
      Object.values(arr.map((val: any) => val.total)), 12);

    const paddedMediaMovel = Array(12 - 1)
      .fill(null)
      .concat(mediaMovel12Meses);

        
    return paddedMediaMovel;
  }

  private getData(res: any[]) {
    return res.reduce((acc: any[], current: any) => {
      const { anoMes, valor, subclasseId } = current;

      if (!acc[anoMes]) {
        acc[anoMes] = { anoMes, values: {} };
      }

      if (!acc[anoMes].values[subclasseId]) {
        acc[anoMes].values[subclasseId] = 0;
      }

      acc[anoMes].values[subclasseId] += valor;
      const valoresRendimentosProventos = Object.values(acc[anoMes].values);
      const valoresRendimentosProventosSomados = valoresRendimentosProventos.reduce((sum: any, val) => sum + val, 0);
      acc[anoMes].total = valoresRendimentosProventosSomados;

      return acc;
    }, {});
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
