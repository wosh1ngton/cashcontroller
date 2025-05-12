import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { Ativo } from "src/app/models/ativo.model";
import { AtivoBrapi } from "src/app/models/ativo-brapi.model";
import { SubclasseAtivo } from "../models/subclasse-ativo.model";
import { AbstractService } from "./abstract.service";

@Injectable({
    providedIn: 'root'
})
export class AtivoService extends AbstractService<Ativo> {
    
    private readonly baseBrapiUrl = environment.brapiURL;    

    constructor(private http: HttpClient) {
        super(http, 'ativos');
    } 

    getAtivosPorClasse(id: number) : Observable<Ativo[]> {
        return this.http.get<Ativo[]>(this.baseUrl + '/ativos/por-classe/' + id);
    }

    getAtivosPorSubClasse(id: number) : Observable<Ativo[]> {
        return this.http.get<Ativo[]>(this.baseUrl + '/ativos/por-subclasse/' + id);
    }

    getAcoesBrapi(): Observable<any> {
        return this.http.get(this.baseBrapiUrl + '/quote/list?type=stock&token=hQTbQHiTVUDV9ohDHfCErU');
    } 
    
    getMinhasAcoes(): Observable<any> {
        return this.http.get(this.baseUrl + '/cotacao/minhas-acoes');
    } 

    getMeusFiis(): Observable<any> {
        return this.http.get(this.baseUrl + '/cotacao/meus-fiis');
    }   

    getSubclasseAtivos() : Observable<SubclasseAtivo[]> {
        return this.http.get<SubclasseAtivo[]>(this.baseUrl + '/ativos/subclasses');
    }    
    
}