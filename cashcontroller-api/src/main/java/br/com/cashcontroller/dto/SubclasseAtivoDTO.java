package br.com.cashcontroller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubclasseAtivoDTO {	
	
	private int id;	
	private String nome;	
	private ClasseAtivoDTO classeAtivoDto;

}
