import { OperacaoRendaVariavel } from "./operacao-renda-variavel.model";

export class IrpfMes {
    
        mes: string = "";
        valorAPagar: number = 0;                
        resultadoMes: number = 0 ;
        imposto: boolean = false;
        totalVendido: number = 0;
        ativosVendidos: OperacaoRendaVariavel[] = [];
}