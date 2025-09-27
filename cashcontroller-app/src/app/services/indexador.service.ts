import { HttpClient, HttpParams } from "@angular/common/http";
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

    save(indexador: any, tipo: string) {
        return this.http.post(this.baseUrl + `/indexadores/${tipo}`, indexador, {});    
    }

    editar(indexador: any, tipo: string, id: number) {        
        return this.http.put(`${this.baseUrl}/indexadores/${tipo}/${id}`, indexador, {});    
    }

    listarHistorico(tipo: string): Observable<Indice[]>  {
        return this.http.get<Indice[]>(this.baseUrl + `/indexadores/listar-valores-indice/${tipo}`);
    }
    
}