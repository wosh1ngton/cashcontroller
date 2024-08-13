package br.com.cashcontroller.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Date;


@Data
@Entity(name = "SELIC_MES")
public class SelicMes {

    @Id
    @Column(name = "ID_SELIC_MES")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @Column(name = "DT_REFERENCIA")
    private LocalDate data;

    @Column(name = "QT_VALOR")
    private double valor;

    @Transient
    YearMonth dataYearMonth;
}
