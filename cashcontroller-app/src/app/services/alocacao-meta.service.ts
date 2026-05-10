import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { AbstractService } from "./abstract.service";
import { AlocacaoMeta } from "../models/alocacao-meta.model";

@Injectable({
    providedIn: 'root'
})
export class AlocacaoMetaService extends AbstractService<AlocacaoMeta> {

    constructor(httpClient: HttpClient) {
        super(httpClient, 'alocacao-meta');
    }

    listar(): Observable<AlocacaoMeta[]> {
        return this.httpClient.get<AlocacaoMeta[]>(`${this.baseUrl}/${this.entity}`);
    }

    salvar(metas: AlocacaoMeta[]): Observable<AlocacaoMeta[]> {
        return this.httpClient.put<AlocacaoMeta[]>(`${this.baseUrl}/${this.entity}`, metas);
    }
}
