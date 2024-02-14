package br.com.cashcontroller.dto;

import java.time.LocalDate;

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
	private double valorUnitario;
	private AtivoDTO ativoDto;
	private double valorCorretagem;
	private LocalDate dataOperacao;
	private int quantidadeNegociada;
	private TipoOperacaoDTO tipoOperacaoDto;

}
