package br.com.cashcontroller.entity;

import jakarta.persistence.*;
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

	@Column(name = "FL_LOGO")
	private String logo;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_SUBCLASSE_ATIVO", nullable = false)
	private SubclasseAtivo subclasseAtivo;

	@Transient
	private double precoMedio;
	
}
