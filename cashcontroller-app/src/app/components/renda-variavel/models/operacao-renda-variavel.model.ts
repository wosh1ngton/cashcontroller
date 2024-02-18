import { Ativo } from "./ativo.model";
import { TipoOperacao } from "./tipo-operacao.model";

export class OperacaoRendaVariavel {

    id: number = 0;
    valorUnitario: number = 0;
    ativo: Ativo = new Ativo();
    valorCorretagem: number = 0;
    quantidadeNegociada: number = 0;
    tipoOperacao: TipoOperacao = new TipoOperacao();
    dataOperacao: Date = new Date();

    /**
     *
     */
    // constructor(quantidadeNegociada: number) {
    //     this.quantidadeNegociada = quantidadeNegociada;
        
    // }
}