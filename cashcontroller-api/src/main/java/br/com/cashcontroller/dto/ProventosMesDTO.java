package br.com.cashcontroller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.YearMonth;
import java.util.Objects;


@NoArgsConstructor
@Data
public class ProventosMesDTO {
    private String anoMes;
    private double valor;
    private int subclasseId;

    public ProventosMesDTO(String anoMes, double valor, int subclasseId) {
        this.anoMes = anoMes;
        this.valor = valor;
        this.subclasseId = subclasseId;
    }
}

