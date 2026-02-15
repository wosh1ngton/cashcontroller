import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { Ativo } from "src/app/models/ativo.model";
import { SubclasseAtivo } from "../models/subclasse-ativo.model";
import { AbstractService } from "./abstract.service";
import { AtivoCarteira } from "../models/ativo-carteira.model";

@Injectable({
    providedIn: 'root'
})
export class AtivoService extends AbstractService<Ativo> {

    constructor(private http: HttpClient) {
        super(http, 'ativos');
    }

    getAtivosPorClasse(id: number): Observable<Ativo[]> {
        return this.http.get<Ativo[]>(this.baseUrl + '/ativos/por-classe/' + id);
    }

    getAtivosPorSubClasse(id: number): Observable<Ativo[]> {
        return this.http.get<Ativo[]>(this.baseUrl + '/ativos/por-subclasse/' + id);
    }

    getMinhasAcoes(): Observable<AtivoCarteira[]> {
        return this.http.get<AtivoCarteira[]>(this.baseUrl + '/cotacao/minhas-acoes');
    }

    getMeusFiis(): Observable<AtivoCarteira[]> {
        return this.http.get<AtivoCarteira[]>(this.baseUrl + '/cotacao/meus-fiis');
    }

    getSubclasseAtivos(): Observable<SubclasseAtivo[]> {
        return this.http.get<SubclasseAtivo[]>(this.baseUrl + '/ativos/subclasses');
    }

}
