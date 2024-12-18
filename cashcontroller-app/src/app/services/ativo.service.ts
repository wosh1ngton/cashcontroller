import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { Ativo } from "src/app/models/ativo.model";
import { AtivoBrapi } from "src/app/models/ativo-brapi.model";
import { SubclasseAtivo } from "../models/subclasse-ativo.model";

@Injectable({
    providedIn: 'root'
})
export class AtivoService {

    private readonly baseUrl = environment.mainUrlAPI;
    private readonly baseBrapiUrl = environment.brapiURL;
    private readonly tokenBrapi = environment.tokenBrapi;

    constructor(private http: HttpClient) {}

    getAtivos() : Observable<Ativo[]> {
        return this.http.get<Ativo[]>(this.baseUrl + '/ativos');
    }

    getAll() : Observable<Ativo[]> {
        return this.http.get<Ativo[]>(this.baseUrl + '/ativos');
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

    getFiisBrapi(): Observable<any> {
        return this.http.get(this.baseBrapiUrl + '/quote/list?type=fund&token=hQTbQHiTVUDV9ohDHfCErU');
    }

    getSubclasseAtivos() : Observable<SubclasseAtivo[]> {
        return this.http.get<SubclasseAtivo[]>(this.baseUrl + '/ativos/subclasses');
    }

    save(ativo: any) {
        return this.http.post(this.baseUrl + '/ativos', ativo);
    }

    update(ativo: any) {
        return this.http.put(this.baseUrl + '/ativos', ativo);
    }

    excluir(id: string) {
        return this.http.delete(this.baseUrl + '/ativos/' + id);
    }

    findById(id: number) {
        return this.http.get(this.baseUrl + '/ativos/' + id)
    }
}