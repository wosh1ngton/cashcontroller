package br.com.cashcontroller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
public class TopPagadoraProventosDTO {
    private String ativo;
    private int subclasseAtivoId;
    private double valor;
    private String logo;

    public TopPagadoraProventosDTO(String ativo, int subclasseAtivoid, double valor) {
        this.ativo = ativo;
        this.subclasseAtivoId = subclasseAtivoid;
        this.valor = valor;
    }
}

