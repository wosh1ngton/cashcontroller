package br.com.cashcontroller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IrpfMesDTO {

	private String mes;
	private double valorAPagar;
	private double resultadoMes;
	private boolean isImposto;
	private double totalVendido;
	private List<OperacaoRendaVariavelDTO> ativosVendidos;
	private double prejuizoCompensar;
	private double prejuizoPosResultado;
}
