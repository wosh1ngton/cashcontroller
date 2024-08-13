import { HttpClient } from "@angular/common/http";
import { environment } from "src/environments/environment";
import { TipoOperacao } from "src/app/models/tipo-operacao.model";
import { Observable } from "rxjs";
import { Injectable } from "@angular/core";
import { FilterOperacao } from "../models/filter-operacao.model";
import { FiltrarOperacao } from "./interfaces/filtrar-operacao";

@Injectable({
    providedIn: 'root'
})
export class OperacaoRendaFixaService implements FiltrarOperacao {

    private readonly baseUrl = environment.mainUrlAPI;

    constructor(private http: HttpClient) {}

    findById(id: string)  {
        return this.http.get(this.baseUrl + '/operacoes/' + id);
    }

    getTipoOperacoes() : Observable<TipoOperacao[]> {
        return this.http.get<TipoOperacao[]>(this.baseUrl + '/operacoes/tipo-operacoes');
    }

    save(operacao: any) {
        return this.http.post(this.baseUrl + '/operacoes/renda-fixa', operacao);
    }

    editar(operacao: any) {
        return this.http.put(this.baseUrl + '/operacoes/renda-fixa', operacao);
    }

    getOperacoesRendaFixa(): Observable<any[]> {
        return this.http.get<any[]>(this.baseUrl + '/operacoes/renda-fixa');
    }

    excluir(id: string) {
        return this.http.delete(this.baseUrl + '/operacoes/renda-fixa/' + id);
    }

    filter(filter: FilterOperacao) {
        return this.http.post(this.baseUrl + '/operacoes/renda-fixa/filter', filter);
    }

    getAnosComOperacoes(): Observable<any> {    
        return this.http.get(this.baseUrl + '/operacoes/anos-com-operacoes-rf');
    }

    getMesesComOperacoesPorAno(ano?: number): Observable<any> {
        return this.http.get(this.baseUrl + '/operacoes/meses-com-operacoes-rf/' + ano)
    } 
    
    listarCarteiraRendaFixa(): Observable<any[]> {
        return this.http.get<any[]>(this.baseUrl + '/operacoes/carteira-renda-fixa');
    }

 
}