import { Ativo } from "../ativo.model";
import { TipoOperacao } from "../tipo-operacao.model";

export class OperacaoRendaVariavelDto {

    id: string = "";
    valorUnitario: number = 0;
    ativoDto: Ativo = new Ativo();
    valorCorretagem: number = 0;
    quantidadeNegociada: number = 0;
    tipoOperacaoDto: TipoOperacao = new TipoOperacao();
    dataOperacao: Date = new Date();
    valorTotalOperacao: number = 0;
    
}