package br.com.cashcontroller.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "PARAMETRO_RENDA_FIXA")
public class ParametroRendaFixa {
	
	@Id
	@Column(name = "ID_PARAMETRO_RENDA_FIXA")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "DT_VENCIMENTO")
	private LocalDate dataVencimento;

	@ManyToOne
	@JoinColumn(name = "ID_INDEXADOR", nullable = false)
	private Indexador indexador;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_ATIVO", nullable = false)
	private Ativo ativo;

	@Column(name = "FG_ISENTO_IMPOSTO")
	private Boolean isIsento;
}
