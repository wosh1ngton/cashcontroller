import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { Ativo } from "src/app/models/ativo.model";
import { AtivoBrapi } from "src/app/models/ativo-brapi.model";
import { SubclasseAtivo } from "../models/subclasse-ativo.model";
import { AtivoCarteira } from "../models/ativo-carteira.model";

@Injectable({
    providedIn: 'root'
})
export class AtivoCarteiraService {

    private readonly baseUrl = environment.mainUrlAPI;

    constructor(private http: HttpClient) {}

    getAtivosCarteira() : Observable<AtivoCarteira[]> {
        return this.http.get<AtivoCarteira[]>(this.baseUrl + '/ativo-carteira');
    }

    getAtivosCarteiraRendaFixa() : Observable<AtivoCarteira[]> {
        return this.http.get<AtivoCarteira[]>(this.baseUrl + '/ativo-carteira/renda-fixa');
    }

    getAtivosCarteiraFiis() : Observable<AtivoCarteira[]> {
        return this.http.get<AtivoCarteira[]>(this.baseUrl + '/ativo-carteira/fiis');
    }

    filter(filtro: any) {
        return this.http.post(this.baseUrl + '/ativo-carteira/filter', filtro);
    } 

   

    update(ativoCarteira: any, id: number) {
        return this.http.put(this.baseUrl + '/ativo-carteira/'+id, ativoCarteira);
    }
    
    

    save(ativoCarteira: any) {
        return this.http.post(this.baseUrl + '/ativo-carteira', ativoCarteira);
    }

    excluir(id: string) {
        return this.http.delete(this.baseUrl + '/ativo-carteira/' + id);
    }

    findById(id: number) {
        return this.http.get(this.baseUrl + '/ativo-carteira/' + id)
    }

    getPatrimonio() {
        return this.http.get(this.baseUrl + '/ativo-carteira/patrimonio')
    }

    getTopPagadoras() {
        return this.http.get(this.baseUrl + '/ativo-carteira/top-pagadoras')
    }

    getProventos(): Observable<any[]> {
        return this.http.get<any[]>(this.baseUrl + '/ativo-carteira/proventos')
    }

    getIbov() {
        return this.http.get(this.baseUrl + '/ativo-carteira/ibov')
    }

    updateCarteiraBySubclasse(id: number) {
        return this.http.get(this.baseUrl + '/ativo-carteira/update-carteira/' + id)
    }
}