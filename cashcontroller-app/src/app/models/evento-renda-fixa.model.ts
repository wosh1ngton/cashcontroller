import { Ativo } from "./ativo.model";
import { TipoEvento } from "./tipo-evento.model";

export class EventoRendaFixa {
    id: number = 0;        
    dataPagamento: Date = new Date();
    valor: number = 0;
    valorTotal: number = 0;
    ativo: Ativo = new Ativo();
    tipoEvento: TipoEvento = new TipoEvento();
    
}