package br.com.cashcontroller.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "IPCA_MES")
public class IpcaMes extends IndiceMesBase {

    @Id
    @Column(name = "ID_IPCA_MES")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
}
