import { SubclasseAtivo } from "./subclasse-ativo.model";

export class Ativo {
    
    id: number = 0;
    nome: string = "";
    sigla: string = "";
    subclasseAtivo: SubclasseAtivo = new SubclasseAtivo();

}