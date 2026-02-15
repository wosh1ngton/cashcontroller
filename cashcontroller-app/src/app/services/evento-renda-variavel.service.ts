import { HttpClient, HttpParams } from "@angular/common/http";
import { environment } from "src/environments/environment";
import { Observable } from "rxjs";
import { Injectable } from "@angular/core";
import { FilterOperacao } from "../models/filter-operacao.model";
import { TipoEvento } from "../models/tipo-evento.model";
import { EventoRendaVariavel } from "../models/evento-renda-variavel.model";

@Injectable({
    providedIn: 'root'
})
export class EventoRendaVariavelService {
    private readonly baseUrl = environment.mainUrlAPI;

    constructor(private http: HttpClient) {}

    findById(id: string): Observable<EventoRendaVariavel> {
        return this.http.get<EventoRendaVariavel>(this.baseUrl + '/eventos/' + id);
    }

    getTipoEventos(): Observable<TipoEvento[]> {
        return this.http.get<TipoEvento[]>(this.baseUrl + '/eventos/tipo-eventos');
    }

    save(evento: EventoRendaVariavel, periodosDeRecorrencia?: number): Observable<void> {
        let params = new HttpParams();
        if (periodosDeRecorrencia !== undefined && periodosDeRecorrencia !== null) {
            params = params.set('periodosDeRecorrencia', periodosDeRecorrencia.toString());
        }
        return this.http.post<void>(this.baseUrl + '/eventos', evento, { params });
    }

    editar(operacao: EventoRendaVariavel): Observable<EventoRendaVariavel> {
        return this.http.put<EventoRendaVariavel>(this.baseUrl + '/eventos', operacao);
    }

    getEventosRendaVariavel(): Observable<EventoRendaVariavel[]> {
        return this.http.get<EventoRendaVariavel[]>(this.baseUrl + '/eventos');
    }

    filter(filter: FilterOperacao): Observable<EventoRendaVariavel[]> {
        return this.http.post<EventoRendaVariavel[]>(this.baseUrl + '/eventos/filter', filter);
    }

    excluir(id: string): Observable<void> {
        return this.http.delete<void>(this.baseUrl + '/eventos/' + id);
    }
}
