package br.com.cashcontroller.dto;

import lombok.*;

import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OperacaoRendaVariavelSaveDTO {
    private int id;
    private double valorUnitario;
    private int ativoDto;
    private double valorCorretagem;
    private LocalDate dataOperacao;
    private int quantidadeNegociada;
    private int tipoOperacaoDto;
    private double custoTotal;
    private double valorTotal;

}

