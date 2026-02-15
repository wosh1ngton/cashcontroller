package br.com.cashcontroller.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OperacaoRendaFixaDTO {

	private int id;

	@Positive(message = "Valor unitário deve ser positivo")
	private double valorUnitario;

	@NotNull(message = "Ativo é obrigatório")
	private AtivoDTO ativoDto;

	private double valorCorretagem;

	@NotNull(message = "Data da operação é obrigatória")
	private LocalDate dataOperacao;

	@Positive(message = "Quantidade negociada deve ser positiva")
	private double quantidadeNegociada;

	@NotNull(message = "Tipo de operação é obrigatório")
	private TipoOperacaoDTO tipoOperacaoDto;

	private double taxaContratada;
	private Double custoTotal;
	private Double valorTotal;

}
