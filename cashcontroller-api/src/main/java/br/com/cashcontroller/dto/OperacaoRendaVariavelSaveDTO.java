package br.com.cashcontroller.dto;

import br.com.cashcontroller.utils.Taxa;
import lombok.*;

import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OperacaoRendaVariavelSaveDTO {
    private int id;
    private double valorUnitario;
    private Long ativoDto;
    private double valorCorretagem;
    private LocalDate dataOperacao;
    private int quantidadeNegociada;
    private Long tipoOperacaoDto;
    private double valorTotal;

}

