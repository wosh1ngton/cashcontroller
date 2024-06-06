import { Ativo } from "./ativo.model";

export class AtivoCarteira {
    ativo: Ativo = new Ativo();
    custodia: number = 0;    
    precoMedio: number = 0;
    cotacao: number = 0;    
    oscilacaoDia: string = "";
    custo: number = 0;
    valorMercado: number = 0;
    percentual: number = 0;    

}