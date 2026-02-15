package br.com.cashcontroller.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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

    @Positive(message = "Ativo deve ser informado")
    private int ativo;

    @NotNull(message = "Data com é obrigatória")
    private LocalDate dataCom;

    @NotNull(message = "Data de pagamento é obrigatória")
    private LocalDate dataPagamento;

    @Positive(message = "Valor do evento deve ser positivo")
    private double valor;

    @Positive(message = "Tipo de evento deve ser informado")
    private int tipoEvento;

    public EventoAddRendaVariavelDTO(int id, LocalDate dataCom, LocalDate dataPagamento) {
        this.id = id;
        this.dataCom = dataCom;
        this.dataPagamento = dataPagamento;
    }

}
