package br.com.cashcontroller.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AporteDTO {
    private int id;

    @NotNull(message = "Data do aporte é obrigatória")
    private LocalDate dataAporte;

    @Positive(message = "Valor do aporte deve ser positivo")
    private double valorAporte;

    private double resultadoMes;
}
