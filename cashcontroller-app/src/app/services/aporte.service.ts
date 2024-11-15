import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { DataService } from "../components/shared/service/abstract.service";

@Injectable({
    providedIn: 'root'
})
export class AporteService extends DataService {        

   constructor(httpClient: HttpClient) {
        super(httpClient, 'aportes');
   }
   

}