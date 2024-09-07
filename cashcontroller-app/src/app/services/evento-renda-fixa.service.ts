import { HttpClient, HttpParams } from "@angular/common/http";
import { environment } from "src/environments/environment";
import { Observable } from "rxjs";
import { Injectable } from "@angular/core";
import { FilterOperacao } from "../models/filter-operacao.model";
import { TipoEvento } from "../models/tipo-evento.model";

@Injectable({
    providedIn: 'root'
})
export class EventoRendaFixaService {
    private readonly baseUrl = environment.mainUrlAPI;

    constructor(private http: HttpClient) {}

    findById(id: string)  {
        return this.http.get(this.baseUrl + '/eventos/' + id);
    }

    getTipoEventos() : Observable<TipoEvento[]> {
        return this.http.get<TipoEvento[]>(this.baseUrl + '/eventos/tipo-eventos');
    }

    save(evento: any): Observable<any> {       
        return this.http.post(this.baseUrl + '/eventos/renda-fixa', evento);
    }

    editar(operacao: any) {
        return this.http.put(this.baseUrl + '/eventos/renda-fixa', operacao);
    }

    getEventosRendaFixa(): Observable<any[]> {
        return this.http.get<any[]>(this.baseUrl + '/eventos/renda-fixa');
    }

    filter(filter: FilterOperacao) {
        return this.http.post(this.baseUrl + '/eventos/filter/renda-fixa', filter);
    }

    excluir(id: string) {
        return this.http.delete(this.baseUrl + '/eventos/renda-fixa/' + parseInt(id));
    }
  
	
}