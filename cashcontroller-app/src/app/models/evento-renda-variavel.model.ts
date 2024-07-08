import { Ativo } from "./ativo.model";
import { TipoEvento } from "./tipo-evento.model";

export class EventoRendaVariavel {
    id: number = 0;    
    dataCom: Date = new Date();
    dataPagamento: Date = new Date();
    valor: number = 0;
    valorTotal: number = 0;
    ativo: Ativo = new Ativo();
    tipoEvento: TipoEvento = new TipoEvento();
}