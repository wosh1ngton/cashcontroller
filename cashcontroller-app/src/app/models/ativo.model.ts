import { ParametroRendaFixa } from "./parametro-renda-fixa.model";
import { SubclasseAtivo } from "./subclasse-ativo.model";

export class Ativo {    
    id: number = 0;
    nome: string = "";
    precoMedio: number = 0;
    sigla: string = "";
    logo: string = "";    
    parametroRendaFixaDto = new ParametroRendaFixa();
    subclasseAtivoDto: SubclasseAtivo = new SubclasseAtivo(); 
}