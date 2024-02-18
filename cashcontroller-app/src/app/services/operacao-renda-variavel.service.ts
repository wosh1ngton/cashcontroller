import { HttpClient } from "@angular/common/http";
import { environment } from "src/environments/environment";
import { TipoOperacao } from "../components/renda-variavel/models/tipo-operacao.model";
import { Observable } from "rxjs";
import { Injectable } from "@angular/core";
import { OperacaoRendaVariavel } from "../components/renda-variavel/models/operacao-renda-variavel.model";

@Injectable({
    providedIn: 'root'
})
export class OperacaoRendaVariavelService {
    private readonly baseUrl = environment.mainUrlAPI;

    constructor(private http: HttpClient) {}

    getTipoOperacoes() : Observable<TipoOperacao[]> {
        return this.http.get<TipoOperacao[]>(this.baseUrl + '/operacoes/tipo-operacoes');
    }

    save(operacao: any) {
        return this.http.post(this.baseUrl + '/operacoes', operacao);
    }

    getOperacoesRendaVariavel(): Observable<OperacaoRendaVariavel[]> {
        return this.http.get<OperacaoRendaVariavel[]>(this.baseUrl + '/operacoes/renda-variavel');
    }
}