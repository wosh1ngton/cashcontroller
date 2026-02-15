package br.com.cashcontroller.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OperacaoRendaVariavelSaveDTO {
    private int id;

    @Positive(message = "Valor unitário deve ser positivo")
    private double valorUnitario;

    @Positive(message = "Ativo deve ser informado")
    private int ativoDto;

    private double valorCorretagem;

    @NotNull(message = "Data da operação é obrigatória")
    private LocalDate dataOperacao;

    @Positive(message = "Quantidade negociada deve ser positiva")
    private int quantidadeNegociada;

    @Positive(message = "Tipo de operação deve ser informado")
    private int tipoOperacaoDto;

    private double custoTotal;
    private double valorTotal;
}
