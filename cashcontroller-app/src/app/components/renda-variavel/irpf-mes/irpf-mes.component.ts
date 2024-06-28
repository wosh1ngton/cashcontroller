
import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges, ViewEncapsulation } from '@angular/core';
import { CardModule } from 'primeng/card';
import { IrpfMes } from '../../../models/irpf-mes.model';
import { OperacaoRendaVariavelService } from 'src/app/services/operacao-renda-variavel.service';
import { CommonModule } from '@angular/common';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';

@Component({
    selector: 'app-irpf-mes',
    templateUrl: 'irpf-mes.component.html',
    styleUrl: 'irpf-mes.component.css',
    encapsulation : ViewEncapsulation.None,
    standalone: true,
    imports: [CardModule, CommonModule, TableModule, ButtonModule]
})
export class IrpfMesComponent implements OnInit, OnChanges {

    irpfMes: IrpfMes = new IrpfMes;
    @Input() filtro : any;
    @Input() filtroChanged: boolean = false;
    @Output() notify = new EventEmitter<string>();

   

    notifyParent() {
        this.notify.emit("mensagem emitida");
    }
    
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