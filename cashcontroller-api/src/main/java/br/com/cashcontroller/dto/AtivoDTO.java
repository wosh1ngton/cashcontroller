package br.com.cashcontroller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AtivoDTO {
	
	private int id;
	private String nome;
	private String sigla;
	private String logo;
	private SubclasseAtivoDTO subclasseAtivoDto;
	private ParametroRendaFixaDTO parametroRendaFixaDto;
	private double precoMedio;

	public AtivoDTO(int id, String sigla) {
		this.id = id;
		this.sigla = sigla;
	}

	public AtivoDTO(int id, String nome, double precoMedio) {
		this.id = id;
		this.nome = nome;
		this.precoMedio = precoMedio;
	}

	public AtivoDTO(int id, String logo, String nome, String sigla) {
		this.id = id;
		this.logo = logo;
		this.nome = nome;
		this.sigla = sigla;
	}

	public AtivoDTO(int id, String logo, String nome, String sigla, SubclasseAtivoDTO subclasseAtivoDto) {
		this.id = id;
		this.logo = logo;
		this.nome = nome;
		this.sigla = sigla;
		this.subclasseAtivoDto = subclasseAtivoDto;
	}
}
