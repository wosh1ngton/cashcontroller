package br.com.cashcontroller.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;


@Data
@Entity
@Table(name = "EVENTO_RENDA_FIXA")
public class EventoRendaFixa {
    @Id
    @Column(name = "ID_EVENTO_RENDA_FIXA")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "DT_PAGAMENTO")
    private LocalDate dataPagamento;

    @Column(name = "QT_VALOR")
    private double valor;

    @ManyToOne
    @JoinColumn(name = "ID_ATIVO", nullable = false)
    private Ativo ativo;

    @ManyToOne
    @JoinColumn(name = "ID_TIPO_EVENTO", nullable = false)
    private TipoEvento tipoEvento;

}
