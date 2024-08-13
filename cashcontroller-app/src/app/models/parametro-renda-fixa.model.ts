import { Indexador } from "./indexador.model";

export class ParametroRendaFixa {
    id: number = 0;
    indexador: Indexador = new Indexador();
    dataVencimento: Date = new Date();   
    isIsento: boolean = false; 
}