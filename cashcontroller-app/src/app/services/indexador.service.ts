import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { Indexador } from "../models/indexador.model";


@Injectable({
    providedIn: 'root'
})
export class IndexadorService {

    private readonly baseUrl = environment.mainUrlAPI;    

    constructor(private http: HttpClient) {}

    getIndexadores() : Observable<Indexador[]> {
        return this.http.get<Indexador[]>(this.baseUrl + '/indexadores');
    }
    
}