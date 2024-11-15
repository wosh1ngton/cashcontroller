import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Table } from "primeng/table";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";

@Injectable({
    providedIn: 'root'
})
export abstract class DataService {

    baseUrl = environment.mainUrlAPI;    
    entity = null;

    constructor(private httpClient: HttpClient, entity: any) {        
        this.entity = entity;        
    }

    getAll(): any {
        return this.httpClient.get(`${this.baseUrl}/${this.entity}`);
    }

    findById(id: number): any {
        return this.httpClient.get(`${this.baseUrl}/${this.entity}/${id}`);
    }

    save(data: any) : Observable<any> {
        return this.httpClient.post(`${this.baseUrl}/${this.entity}`, data);
    }

    excluir(id: number) {
        return this.httpClient.delete(`${this.baseUrl}/${this.entity}/${id}`);
    }

    // consultar(data = {}, tabela: Table): Observable<any> {
    //     return this.httpClient.post(`${ this.baseUrl }/${ this.entity }/consulta`, data, { params: RequestUtil.getRequestParams(tabela) });
    // }

    // filter(data = {}, tabela: Table, pageable?: { sort: string }): Observable<any> {
    //     const params = RequestUtil.preparePageableParams(tabela, pageable);
    //     return this.httpClient.post(`${ this.baseUrl }/${ this.entity }/filter`, data, { params: params });
    // }
    

}