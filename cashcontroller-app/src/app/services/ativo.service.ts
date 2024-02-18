import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { Ativo } from "../components/renda-variavel/models/ativo.model";

@Injectable({
    providedIn: 'root'
})
export class AtivoService {

    private readonly baseUrl = environment.mainUrlAPI;

    constructor(private http: HttpClient) {}

    getAtivos() : Observable<Ativo[]> {
        return this.http.get<Ativo[]>(this.baseUrl + '/ativos');
    }
}