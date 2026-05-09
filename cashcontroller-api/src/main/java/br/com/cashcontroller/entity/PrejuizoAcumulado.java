package br.com.cashcontroller.entity;

import br.com.cashcontroller.model.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


import java.time.LocalDate;

@Table(name = "prejuizo_acumulado")
@Entity
@Data
public class PrejuizoAcumulado {

    @Id
    @Column(name = "ID_PREJUIZO")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_USUARIO", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;

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
