package br.com.cashcontroller.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "ATIVO_CARTEIRA")
public class AtivoCarteira {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ATIVO_CARTEIRA")
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_ATIVO")
    private Ativo ativo;

    @Column(name = "QT_PERCENTUAL_DESEJADO")
    private double percentual;

    @Column(name = "DT_INICIO")
    private LocalDate dataInicio;

    @Column(name = "DT_FIM")
    private LocalDate dataFim;

    @Column(name = "QT_CUSTODIA")
    private double custodia;

    @Column(name = "QT_CUSTO")
    private double custo;

    @Column(name = "QT_VALOR_MERCADO")
    private double valorMercado;

    @Column(name = "QT_PROVENTOS")
    private double proventos;

}
