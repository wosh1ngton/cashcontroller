
import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges, ViewEncapsulation } from '@angular/core';
import { CardModule } from 'primeng/card';
import { IrpfMes } from '../../../models/irpf-mes.model';
import { OperacaoRendaVariavelService } from 'src/app/services/operacao-renda-variavel.service';
import { CommonModule } from '@angular/common';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { MessageService } from 'primeng/api';
import { EnumSubclasseAtivo } from 'src/app/enums/subclasse-ativo.enum';

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
    @Input() tituloSubclasseAtivo: string = "";
    @Input() subclasseAtivo: EnumSubclasseAtivo = 1;
    
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
        public messageService: MessageService,
      ) { }
 

    buscarImpostoMes() {
        let filtro = {...this.filtro}
        filtro.subclasse = this.subclasseAtivo;
       // this.filtro.subclasse = this.subclasseAtivo;
        return this.operacaoRendaVariavelService.getImpostoMes(filtro)
            .subscribe((x: any) => { 
                this.irpfMes = x;
                
            });
    }

    atualizarPrejuizoAcumulado(subclasseAtivo: EnumSubclasseAtivo) {
        let mes = 0;
        let ano = 0;
        let anoMes = "";
        if(this.filtro.ano === 0) {
            mes = this.filtro.startDate.getMonth()+1;
            ano = this.filtro.startDate.getFullYear();            
        } else {
            mes = this.filtro.mes;
            ano = this.filtro.ano;
        }
        const mesStr = String(mes).padStart(2, '0');
        anoMes = `${mesStr}-${ano}`;
        this.operacaoRendaVariavelService.atualizarPrejuizoAcumulado(anoMes, subclasseAtivo)
            .subscribe({
                next: (_) => {
                 this.messageService.add({
                    severity: 'success',
                    summary: 'Sucesso',
                    detail: 'Atualizado com sucesso',
                });
                },
                error: (err: any) => {
                    this.messageService.add({
                    severity: 'error',
                    summary: 'Erro',
                    detail: 'Tente novamente',
                });
                    console.log(err)
                }
            });
        
    }
}