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
	private Long quantidadeNegociada;
	private TipoOperacaoDTO tipoOperacaoDto;
	private double custoTotal;
	private double valorTotal;

	private double resultadoOperacao;
//	public double getValorTotalOperacao() {
//		double valorBase = this.valorUnitario * this.quantidadeNegociada;
//		double valorISS = valorCorretagem * Taxa.TAXA_ISS;
//		double taxas = Taxa.TAXA_LIQUIDACAO + Taxa.TAXA_EMOLUMENTOS;
//		double valorTotalOperacao = valorBase + (valorBase * (taxas/100) + valorISS);
//		return valorTotalOperacao;
//	}

}
