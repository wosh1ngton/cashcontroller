package br.com.cashcontroller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class PrejuizoAcumuladoDTO {

    private Long id;
    private String anoMes;
    private Integer subclasseAtivoId;
    private Double prejuizoAcumulado;
    private Double lucroMes;
    private Double lucroTributavel;
    private Double vendasMes;
}
