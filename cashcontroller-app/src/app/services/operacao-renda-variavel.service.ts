import { HttpClient } from "@angular/common/http";
import { environment } from "src/environments/environment";
import { TipoOperacao } from "src/app/models/tipo-operacao.model";
import { Observable } from "rxjs";
import { Injectable } from "@angular/core";
import { FilterOperacao } from "../models/filter-operacao.model";
import { FiltrarOperacao } from "./interfaces/filtrar-operacao";
import { ItemLabel } from "../models/interfaces/item-label";

@Injectable({
    providedIn: 'root'
})
export class OperacaoRendaVariavelService implements FiltrarOperacao {
    
    private readonly baseUrl = environment.mainUrlAPI;

    constructor(private http: HttpClient) {}

    findById(id: string)  {
        return this.http.get(this.baseUrl + '/operacoes/' + id);
    }

    getTipoOperacoes() : Observable<TipoOperacao[]> {
        return this.http.get<TipoOperacao[]>(this.baseUrl + '/operacoes/tipo-operacoes');
    }

    save(operacao: any) {
        return this.http.post(this.baseUrl + '/operacoes', operacao);
    }

    editar(operacao: any) {
        return this.http.put(this.baseUrl + '/operacoes', operacao);
    }

    getOperacoesRendaVariavel(): Observable<any[]> {
        return this.http.get<any[]>(this.baseUrl + '/operacoes/renda-variavel');
    }

    filter(filter: FilterOperacao) {
        return this.http.post(this.baseUrl + '/operacoes/renda-variavel/filter', filter);
    }    

    excluir(id: string) {
        return this.http.delete(this.baseUrl + '/operacoes/' + id);
    }

    getImpostoMes(filter: FilterOperacao) {
        return this.http.post(this.baseUrl + '/operacoes/renda-variavel/irpf', filter);
    }

    carteiraAcoes() {
        return this.http.get<any[]>(this.baseUrl + '/operacoes/carteira-acoes');
    }

    carteiraFiis() {
        return this.http.get<any[]>(this.baseUrl + '/operacoes/carteira-fiis');
    }

    getAnosComOperacoes(): Observable<any> {    
        return this.http.get(this.baseUrl + '/operacoes/anos-com-operacoes');
    }

    getMesesComOperacoesPorAno(ano?: number): Observable<any> {
        return this.http.get(this.baseUrl + '/operacoes/MesesComOperacoesPorAno/' + ano)
    }

    posicoesEncerradas() {        
        return this.http.get<any[]>(this.baseUrl + '/operacoes/posicoes-encerradas');
    }

    getOperacoesPorAtivo(idAtivo: number): Observable<any> {
        return this.http.get<any[]>(this.baseUrl + '/operacoes/por-ativo/' + idAtivo);
    }

    public buscarAtivosOperados(): Observable<ItemLabel[]> {
        return this.http.get<ItemLabel[]>(`${this.baseUrl}/operacoes/ativos-operados`);
    }
	
}