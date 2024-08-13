package br.com.cashcontroller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AtivoAddDTO {

	private int id;
	private String nome;
	private String sigla;
	private String logo;
	private ParametroRendaFixaDTO parametroRendaFixaDto;
	private int subclasseAtivo;
	private double precoMedio;

	public AtivoAddDTO(int id, String nome, double precoMedio) {
		this.id = id;
		this.nome = nome;
		this.precoMedio = precoMedio;
	}

	public AtivoAddDTO(int id, String logo, String nome, String sigla, int subclasseAtivo) {
		this.id = id;
		this.logo = logo;
		this.nome = nome;
		this.sigla = sigla;
		this.subclasseAtivo = subclasseAtivo;
	}

}
