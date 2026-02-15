import { HttpClient } from "@angular/common/http";
import { environment } from "src/environments/environment";
import { TipoOperacao } from "src/app/models/tipo-operacao.model";
import { Observable } from "rxjs";
import { Injectable } from "@angular/core";
import { FilterOperacao } from "../models/filter-operacao.model";
import { FiltrarOperacao } from "./interfaces/filtrar-operacao";
import { ItemLabel } from "../models/interfaces/item-label";
import { EnumSubclasseAtivo } from "../enums/subclasse-ativo.enum";
import { OperacaoRendaVariavel } from "../models/operacao-renda-variavel.model";
import { AtivoCarteira } from "../models/ativo-carteira.model";
import { Mes } from "../models/dto/mes";

@Injectable({
    providedIn: 'root'
})
export class OperacaoRendaVariavelService implements FiltrarOperacao {

    private readonly baseUrl = environment.mainUrlAPI;

    constructor(private http: HttpClient) {}

    findById(id: string): Observable<OperacaoRendaVariavel> {
        return this.http.get<OperacaoRendaVariavel>(this.baseUrl + '/operacoes/' + id);
    }

    getTipoOperacoes(): Observable<TipoOperacao[]> {
        return this.http.get<TipoOperacao[]>(this.baseUrl + '/operacoes/tipo-operacoes');
    }

    save(operacao: OperacaoRendaVariavel): Observable<OperacaoRendaVariavel> {
        return this.http.post<OperacaoRendaVariavel>(this.baseUrl + '/operacoes', operacao);
    }

    editar(operacao: OperacaoRendaVariavel): Observable<OperacaoRendaVariavel> {
        return this.http.put<OperacaoRendaVariavel>(this.baseUrl + '/operacoes', operacao);
    }

    getOperacoesRendaVariavel(): Observable<OperacaoRendaVariavel[]> {
        return this.http.get<OperacaoRendaVariavel[]>(this.baseUrl + '/operacoes/renda-variavel');
    }

    filter(filter: FilterOperacao): Observable<OperacaoRendaVariavel[]> {
        return this.http.post<OperacaoRendaVariavel[]>(this.baseUrl + '/operacoes/renda-variavel/filter', filter);
    }

    excluir(id: string): Observable<void> {
        return this.http.delete<void>(this.baseUrl + '/operacoes/' + id);
    }

    getImpostoMes(filter: FilterOperacao) {
        return this.http.post(this.baseUrl + '/operacoes/renda-variavel/irpf', filter);
    }

    carteiraAcoes(): Observable<AtivoCarteira[]> {
        return this.http.get<AtivoCarteira[]>(this.baseUrl + '/operacoes/carteira-acoes');
    }

    carteiraFiis(): Observable<AtivoCarteira[]> {
        return this.http.get<AtivoCarteira[]>(this.baseUrl + '/operacoes/carteira-fiis');
    }

    getAnosComOperacoes(): Observable<number[]> {
        return this.http.get<number[]>(this.baseUrl + '/operacoes/anos-com-operacoes');
    }

    getMesesComOperacoesPorAno(ano?: number): Observable<Mes[]> {
        return this.http.get<Mes[]>(this.baseUrl + '/operacoes/MesesComOperacoesPorAno/' + ano);
    }

    posicoesEncerradas() {
        return this.http.get<any[]>(this.baseUrl + '/operacoes/posicoes-encerradas');
    }

    getOperacoesPorAtivo(idAtivo: number): Observable<OperacaoRendaVariavel[]> {
        return this.http.get<OperacaoRendaVariavel[]>(this.baseUrl + '/operacoes/por-ativo/' + idAtivo);
    }

    public buscarAtivosOperados(): Observable<ItemLabel[]> {
        return this.http.get<ItemLabel[]>(`${this.baseUrl}/operacoes/ativos-operados`);
    }

    public atualizarPrejuizoAcumulado(anoMes: string, subclasseAtivoId: EnumSubclasseAtivo) {
        return this.http.get(`${this.baseUrl}/operacoes/prejuizo/${anoMes}/${subclasseAtivoId}`);
    }
}
