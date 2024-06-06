package br.com.cashcontroller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class Filter {
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer subclasse;
    private Integer ano;
    private Integer mes;


}
