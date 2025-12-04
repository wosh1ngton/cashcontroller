package br.com.cashcontroller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Filter {
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer subclasse;
    private Integer ano;
    private Integer mes;
    private Integer ativo;

    public Filter(Integer subclasse, Integer ano, Integer mes) {
        this.subclasse = subclasse;
        this.ano = ano;
        this.mes = mes;
    }

}
