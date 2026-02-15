package br.com.cashcontroller.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OperacaoRendaVariavelDTO {

	private int id;

	@Positive(message = "Valor unitário deve ser positivo")
	private double valorUnitario;

	@NotNull(message = "Ativo é obrigatório")
	private AtivoDTO ativoDto;

	private double valorCorretagem;

	@NotNull(message = "Data da operação é obrigatória")
	private LocalDate dataOperacao;

	@Positive(message = "Quantidade negociada deve ser positiva")
	private int quantidadeNegociada;

	@NotNull(message = "Tipo de operação é obrigatório")
	private TipoOperacaoDTO tipoOperacaoDto;

	private double custoTotal;
	private double valorTotal;

	private double resultadoOperacao;

}
