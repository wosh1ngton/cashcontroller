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
public class EventoListRendaVariavelDTO {
    private int id;
    private AtivoDTO ativo;
    private LocalDate dataCom;
    private LocalDate dataPagamento;
    private double valor;
    private double valorTotal;
    private TipoEventoDTO tipoEvento;

}
