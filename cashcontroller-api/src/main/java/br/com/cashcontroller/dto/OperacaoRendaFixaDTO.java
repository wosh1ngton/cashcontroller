package br.com.cashcontroller.dto;

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
	private double valorUnitario;
	private AtivoDTO ativoDto;
	private double valorCorretagem;
	private LocalDate dataOperacao;
	private int quantidadeNegociada;
	private IndexadorDTO indexadorDto;
	private TipoOperacaoDTO tipoOperacaoDto;
	private double taxaContratada;
	private LocalDate dataVencimento;

}
