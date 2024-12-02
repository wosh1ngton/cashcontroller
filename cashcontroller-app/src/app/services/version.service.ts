import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { environment } from "src/environments/environment";

@Injectable({
    providedIn: 'root'
})
export class VersionService {

    private readonly baseUrl = environment.mainUrlAPI;    
    constructor(private httpClient: HttpClient) {}

    public getVersion() {
        return this.httpClient.get(`${this.baseUrl}/version`, { responseType: 'text'})
    }
}