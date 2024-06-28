package br.com.cashcontroller.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "EVENTO_RENDA_VARIAVEL")
public class EventoRendaVariavel {

    @Id
    @Column(name = "ID_EVENTO_RENDA_VARIAVEL")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "DT_COM")
    private LocalDate dataCom;

    @Column(name = "DT_PAGAMENTO")
    private LocalDate dataPagamento;

    @Column(name = "QT_VALOR")
    private double valor;

    @ManyToOne
    @JoinColumn(name = "ID_TIPO_EVENTO", nullable = false)
    private TipoEvento tipoEvento;

    @ManyToOne
    @JoinColumn(name = "ID_ATIVO", nullable = false)
    private Ativo ativo;
}
