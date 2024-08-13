package br.com.cashcontroller.entity;

import java.time.LocalDate;

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
@Table(name = "OPERACAO_RENDA_FIXA")
public class OperacaoRendaFixa {
	
	@Id
	@Column(name = "ID_OPERACAO_RENDA_FIXA")
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
	private double quantidadeNegociada;
	
	@ManyToOne
	@JoinColumn(name = "ID_TIPO_OPERACAO", nullable = false)
	private TipoOperacao tipoOperacao;
	
	@Column(name = "QT_TAXA_CONTRATADA")
	private double taxaContratada;

	@Column(name = "QT_CUSTO_TOTAL", nullable = true)
	private double custoTotal;

	@Column(name = "QT_VALOR_TOTAL", nullable = true)
	private double valorTotal;
	
}
