import { Observable } from "rxjs";

export interface FiltrarOperacao {
    
    getAnosComOperacoes(): Observable<any>;     
    getMesesComOperacoesPorAno(ano?: number): Observable<any>;

}