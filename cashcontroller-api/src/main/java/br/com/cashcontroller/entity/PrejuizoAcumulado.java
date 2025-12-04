package br.com.cashcontroller.entity;

import jakarta.persistence.*;
import lombok.Data;


import java.time.LocalDate;

@Table(name = "prejuizo_acumulado")
@Entity
@Data
public class PrejuizoAcumulado {

    @Id
    @Column(name = "ID_PREJUIZO")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ANO_MES")
    private String anoMes;

    @Column(name = "ID_SUBCLASSE_ATIVO")
    private Integer subclasseAtivoId;

    @Column(name = "PREJUIZO_ACUMULADO")
    private Double prejuizoAcumulado;

    @Column(name = "LUCRO_MES")
    private Double lucroMes;

    @Column(name = "LUCRO_TRIBUTAVEL")
    private Double lucroTributavel;

    @Column(name = "VENDAS_MES")
    private Double vendasMes;
}
