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
public class EventoAddRendaVariavelDTO {
    private int id;
    private int ativo;
    private LocalDate dataCom;
    private LocalDate dataPagamento;
    private double valor;
    private int tipoEvento;

    public EventoAddRendaVariavelDTO(int id, LocalDate dataCom, LocalDate dataPagamento) {
        this.id = id;
        this.dataCom = dataCom;
        this.dataPagamento = dataPagamento;
    }

}
