package br.com.cashcontroller.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(name = "SUBCLASSE_ATIVO")
public class SubclasseAtivo {
	
	@Id
	@Column(name = "ID_SUBCLASSE_ATIVO")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "NM_SUBCLASSE_ATIVO")
	private String nome;
	
	@ManyToOne(fetch = FetchType.EAGER) 
	@JoinColumn(name="ID_CLASSE_ATIVO", nullable = false)
	private ClasseAtivo classeAtivo;
	
	
}
