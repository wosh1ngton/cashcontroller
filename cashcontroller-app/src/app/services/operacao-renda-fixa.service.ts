import { HttpClient } from "@angular/common/http";
import { environment } from "src/environments/environment";
import { TipoOperacao } from "src/app/models/tipo-operacao.model";
import { Observable } from "rxjs";
import { Injectable } from "@angular/core";

@Injectable({
    providedIn: 'root'
})
export class OperacaoRendaFixaService {
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
}