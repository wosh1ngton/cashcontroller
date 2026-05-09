package br.com.cashcontroller.entity;

import br.com.cashcontroller.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "HISTORICO_ATIVO_CARTEIRA")
public class HistoricoAtivoCarteira {

	@Id
	@Column(name = "ID_HISTORICO_ATIVO_CARTEIRA")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "ID_USUARIO", nullable = false)
	private User user;

	@Column(name = "QT_VALOR")
	private double valor;

	@Column(name = "DT_REFERENCIA")
	private LocalDate dataReferencia;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_ATIVO")
	private  Ativo ativo;

}
