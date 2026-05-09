package br.com.cashcontroller.entity;

import br.com.cashcontroller.model.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;

@Data
@Entity
@Table
public class Aporte {

    @Id
    @Column(name = "ID_APORTE")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_USUARIO", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;

    @Column(name = "DT_APORTE")
    private LocalDate dataAporte;

    @Column(name = "QT_VALOR_APORTE")
    private double valorAporte;

    @Column(name = "QT_RESULTADO_MES")
    private Double resultadoMes;

}
