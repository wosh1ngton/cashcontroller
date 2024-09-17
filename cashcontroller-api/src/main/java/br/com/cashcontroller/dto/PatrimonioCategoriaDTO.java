package br.com.cashcontroller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PatrimonioCategoriaDTO {
    private int subClasseId;
    private String categoria;
    private Double percentual;
    private Double valor;

    public PatrimonioCategoriaDTO(int subClasseId, Double valor) {
        this.subClasseId = subClasseId;
        this.valor = valor;
    }

    public PatrimonioCategoriaDTO(int subClasseId, Double percentual, Double valor) {
        this.subClasseId = subClasseId;
        this.percentual = percentual;
        this.valor = valor;
    }

}
