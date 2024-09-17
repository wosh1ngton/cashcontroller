import {
  Component,
  Input,
  OnChanges,
  OnInit,
  SimpleChanges,
} from '@angular/core';
import Chart, { ChartConfiguration } from 'chart.js/auto';
import {
  TreemapController,
  TreemapControllerDatasetLabelsOptions,
  TreemapDataPoint,
  TreemapElement,
} from 'chartjs-chart-treemap';

Chart.register(TreemapController, TreemapElement);

@Component({
  selector: 'app-tree-map-chart',
  templateUrl: './tree-map-chart.component.html',
  styleUrl: './tree-map-chart.component.css',
})
export class TreeMapChartComponent implements OnInit, OnChanges {
  @Input() dados: any[] = [];
  chart: Chart | any;

  ngOnInit() {
    const ctx = document.getElementById('myTreemap') as HTMLCanvasElement;
    this.chart = this.createTreemapChart(ctx, this.dados);
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['dados'] && !changes['dados'].firstChange) {
      console.log('oi')
      this.updateTreemapChart();
    }
  }

  updateTreemapChart() {    

    if (this.chart) {
      this.chart.destroy();
    }
    const ctx = document.getElementById('myTreemap') as HTMLCanvasElement;
    this.chart = this.createTreemapChart(ctx, this.dados); // Create new chart
  }

  GROUPS = ['category'];

  createTreemapChart(ctx: HTMLCanvasElement, data: any[]) {
    
    const dataValue: Array<{ label: string; value: number; category: string }> =
      data.map((valor: any) => {
        return {
          label: valor.ativo,
          value: valor.valor,
          category: valor.subclasseAtivoId == 1 ? 'FIIS' : 'AÇÃO',
        };
      });

    const getColorByCategory = (category: string) => {
      if (category === 'FIIS') {
        return 'rgba(75, 192, 192, 0.5)';
      } else if (category === 'AÇÃO') {
        return 'rgba(255, 159, 64, 0.5)';
      }
      return 'rgba(153, 102, 255, 0.5)';
    };

    const config: ChartConfiguration<any,TreemapDataPoint,TreemapControllerDatasetLabelsOptions> = {
      type: 'treemap',
      data: {
        datasets: [
          {
            labels: {
              display: true,
            },
            tree: dataValue,
            key: 'value',
            //groups: ['category'],
            borderColor: 'green',
            borderWidth: 0.5,
            spacing: 0,
            backgroundColor: (ctx: any) => {
              const category = dataValue[ctx.dataIndex]?.category;
              return getColorByCategory(category);
            },
          },
        ],
      },
      options: {
        plugins: {
          title: {
            display: true,
            text: 'Top pagadoras de proventos',
          },
          legend: {
            display: false,
          },
        },
      },
    };
    return new Chart(ctx, config);
  }
}
