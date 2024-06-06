
import { Component, Input, OnChanges, OnInit, ViewEncapsulation } from '@angular/core';
import { CardModule } from 'primeng/card';
import { IrpfMes } from '../../../models/irpf-mes.model';
import { OperacaoRendaVariavelService } from 'src/app/services/operacao-renda-variavel.service';
import { CommonModule } from '@angular/common';
import { TableModule } from 'primeng/table';

@Component({
    selector: 'app-irpf-mes',
    templateUrl: 'irpf-mes.component.html',
    styleUrl: 'irpf-mes.component.css',
    encapsulation : ViewEncapsulation.None,
    standalone: true,
    imports: [CardModule, CommonModule, TableModule]
})
export class IrpfMesComponent implements OnInit, OnChanges {

    irpfMes: IrpfMes = new IrpfMes;
    @Input() filtro : any;
    @Input() filtroChanged: boolean = false;
    
    
    ngOnChanges() {
        this.buscarImpostoMes();
    }

    ngOnInit(): void {
        
    }

    constructor(       
        public operacaoRendaVariavelService: OperacaoRendaVariavelService,
      
      ) { }
 

    buscarImpostoMes() {
        return this.operacaoRendaVariavelService.getImpostoMes(this.filtro)
            .subscribe((x: any) => { this.irpfMes = x });
    }
}