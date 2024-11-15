package br.com.cashcontroller.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table
public class Aporte {

    @Id
    @Column(name = "ID_APORTE")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "DT_APORTE")
    private LocalDate dataAporte;

    @Column(name = "QT_VALOR_APORTE")
    private double valorAporte;

}
