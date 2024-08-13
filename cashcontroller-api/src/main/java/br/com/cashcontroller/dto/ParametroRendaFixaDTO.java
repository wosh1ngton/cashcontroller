package br.com.cashcontroller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParametroRendaFixaDTO {
    private int id;
    private LocalDate dataVencimento;
    private IndexadorDTO indexador;
    private Boolean isIsento;

}
