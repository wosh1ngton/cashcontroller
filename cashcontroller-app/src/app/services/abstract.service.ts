import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";

@Injectable({
    providedIn: 'root'
})
export class AbstractService<T> {
    
    protected baseUrl = environment.mainUrlAPI;

    constructor(
        protected httpClient: HttpClient,
        protected entity: String
    ) {}

    save(data: T): Observable<T> {
        return this.httpClient.post<T>(`${this.baseUrl}/${this.entity}`, data);
    }

    findById(id: number): Observable<T> {
        return this.httpClient.get<T>(`${this.baseUrl}/${this.entity}/${id}`);
    }

    excluir(id: number): Observable<void> {
        return this.httpClient.delete<void>(`${this.baseUrl}/${this.entity}/${id}`);
    }

    update(data: T): Observable<T> {
        return this.httpClient.put<T>(`${this.baseUrl}/${this.entity}`, data);
    }

    getAll() : Observable<T[]> {
        return this.httpClient.get<T[]>(`${this.baseUrl}/${this.entity}`);
    }
}