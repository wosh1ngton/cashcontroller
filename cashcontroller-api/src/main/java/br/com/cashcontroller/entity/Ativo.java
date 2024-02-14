package br.com.cashcontroller.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "ATIVO")
public class Ativo {
	
	@Id
	@Column(name = "ID_ATIVO")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "NM_ATIVO")
	private String nome;
	
	@Column(name = "SG_ATIVO")
	private String sigla;	
	
	@ManyToOne
	@JoinColumn(name = "ID_SUBCLASSE_ATIVO", nullable = false)
	private SubclasseAtivo subclasseAtivo;
	
	
	
}
