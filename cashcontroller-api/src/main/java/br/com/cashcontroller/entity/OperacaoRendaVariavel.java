package br.com.cashcontroller.entity;

import java.time.LocalDate;

import br.com.cashcontroller.utils.Taxa;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "OPERACAO_RENDA_VARIAVEL")
public class OperacaoRendaVariavel {
	
	@Id
	@Column(name = "ID_OPERACAO_RENDA_VARIAVEL")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "QT_VALOR_ATIVO")
	private double valorUnitario;
	
	@ManyToOne
	@JoinColumn(name = "ID_ATIVO", nullable = false)
	private Ativo ativo;
	
	@Column(name = "QT_VALOR_CORRETAGEM")
	private double valorCorretagem;
	
	@Column(name = "DT_OPERACAO")
	private LocalDate dataOperacao;	
	
	@Column(name = "QT_ATIVO")
	private int quantidadeNegociada;

	@Column(name = "QT_VALOR_TOTAL")
	private double valorTotal;
	
	@ManyToOne
	@JoinColumn(name = "ID_TIPO_OPERACAO", nullable = false)
	private TipoOperacao tipoOperacao;

	
}
