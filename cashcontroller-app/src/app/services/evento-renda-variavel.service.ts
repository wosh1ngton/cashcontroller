import { HttpClient, HttpParams } from "@angular/common/http";
import { environment } from "src/environments/environment";
import { TipoOperacao } from "src/app/models/tipo-operacao.model";
import { Observable } from "rxjs";
import { Injectable } from "@angular/core";
import { FilterOperacao } from "../models/filter-operacao.model";
import { TipoEvento } from "../models/tipo-evento.model";

@Injectable({
    providedIn: 'root'
})
export class EventoRendaVariavelService {
    private readonly baseUrl = environment.mainUrlAPI;

    constructor(private http: HttpClient) {}

    findById(id: string)  {
        return this.http.get(this.baseUrl + '/eventos/' + id);
    }

    getTipoEventos() : Observable<TipoEvento[]> {
        return this.http.get<TipoEvento[]>(this.baseUrl + '/eventos/tipo-eventos');
    }

    save(evento: any, periodosDeRecorrencia?: number): Observable<any> {
        
        let params = new HttpParams();
        if (periodosDeRecorrencia !== undefined && periodosDeRecorrencia !== null) {
            params = params.set('periodosDeRecorrencia', periodosDeRecorrencia.toString());
        }
        return this.http.post(this.baseUrl + '/eventos', evento, { params });
    }

    editar(operacao: any) {
        return this.http.put(this.baseUrl + '/eventos', operacao);
    }

    getEventosRendaVariavel(): Observable<any[]> {
        return this.http.get<any[]>(this.baseUrl + '/eventos');
    }

    filter(filter: FilterOperacao) {
        return this.http.post(this.baseUrl + '/eventos/filter', filter);
    }

    excluir(id: string) {
        return this.http.delete(this.baseUrl + '/eventos/' + id);
    }
  
	
}