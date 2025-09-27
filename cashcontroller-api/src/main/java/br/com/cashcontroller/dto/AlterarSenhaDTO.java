package br.com.cashcontroller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
public class AlterarSenhaDTO {

	private String senhaAtual;
	private String novaSenha;

}
