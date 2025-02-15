package br.com.cashcontroller.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AporteDTO {
    private int id;
    private LocalDate dataAporte;
    private double valorAporte;
}
