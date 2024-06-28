import { Ativo } from "./ativo.model";
import { TipoEvento } from "./tipo-evento.model";

export class EventoRendaVariavel {
    id: number = 0;    
    dataCom: Date | undefined;
    dataPagamento: Date | undefined;
    valor: number = 0;
    ativo: Ativo = new Ativo();
    tipoEvento: TipoEvento = new TipoEvento();
}