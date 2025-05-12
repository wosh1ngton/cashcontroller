import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { Indexador } from "../models/indexador.model";
import { Indice } from "../models/indice.model";


@Injectable({
    providedIn: 'root'
})
export class IndexadorService {

    private readonly baseUrl = environment.mainUrlAPI;    

    constructor(private http: HttpClient) {}

    getIndexadores() : Observable<Indexador[]> {
        return this.http.get<Indexador[]>(this.baseUrl + '/indexadores');
    }

    save(indexador: any) {
        return this.http.post(this.baseUrl + '/indexadores/ipca-mes-unitario', indexador, {});    
    }

    editar(indexador: any) {
        return this.http.put(this.baseUrl + '/indexadores/ipca-mes-unitario', indexador, {});    
    }

    listarHistorico(tipo: string): Observable<Indice[]>  {
        return this.http.get<Indice[]>(this.baseUrl + `/indexadores/listar-valores-indice/${tipo}`);
    }
    
}