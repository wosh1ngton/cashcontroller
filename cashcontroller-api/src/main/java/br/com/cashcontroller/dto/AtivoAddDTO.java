package br.com.cashcontroller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
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

	@NotBlank(message = "Nome do ativo é obrigatório")
	private String nome;

	@NotBlank(message = "Sigla do ativo é obrigatória")
	private String sigla;

	private String logo;
	private ParametroRendaFixaDTO parametroRendaFixaDto;

	@Positive(message = "Subclasse do ativo deve ser informada")
	private int subclasseAtivo;

	private boolean internacional;
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
