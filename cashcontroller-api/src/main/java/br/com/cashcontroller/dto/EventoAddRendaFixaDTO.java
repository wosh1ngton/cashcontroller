package br.com.cashcontroller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EventoAddRendaFixaDTO {
    private int id;
    private int ativo;
    private LocalDate dataPagamento;
    private double valor;
    private int tipoEvento;

}
