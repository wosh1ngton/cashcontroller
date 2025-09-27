import { HttpClient, HttpHeaders, HttpResponse } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { AbstractService } from "./abstract.service";
import { Usuario } from "../models/usuario.model";
import { AlterarSenha } from "../models/novo-usuario.model";

@Injectable({
    providedIn: 'root'
})
export class UserService extends AbstractService<Usuario> {

    constructor(httpClient: HttpClient) {
        super(httpClient, 'usuarios');
    }

    alterarSenha(usuario: AlterarSenha) {        
        return this.httpClient.post(this.baseUrl + '/usuarios/alterar-senha', usuario,
            {responseType: 'text' as 'json'}
        );
    }
  
}