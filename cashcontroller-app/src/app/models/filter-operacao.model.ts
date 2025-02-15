export class FilterOperacao {   

    startDate: Date | null;
    endDate: Date | null = new Date();
    subclasse: number = 0;
    ano: number;
    mes: number = 0;
    ativo: number = 0;
    constructor() {
        const today = new Date();
        this.startDate = new Date(today.getFullYear(), today.getMonth(), 1);
        this.ano = today.getFullYear();
    }    
  
}