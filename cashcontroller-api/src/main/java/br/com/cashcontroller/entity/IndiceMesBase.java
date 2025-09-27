package br.com.cashcontroller.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.YearMonth;
@Getter
@Setter
@MappedSuperclass
public abstract class IndiceMesBase {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @Column(name = "DT_REFERENCIA")
    private LocalDate data;

    @Column(name = "QT_VALOR")
    private double valor;

    @Transient
    protected YearMonth dataYearMonth;
}
