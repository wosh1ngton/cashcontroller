package br.com.cashcontroller.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "TIPO_EVENTO")
public class TipoEvento {

    @Id
    @Column(name = "ID_TIPO_EVENTO")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "NM_TIPO_EVENTO")
    private String nome;
}
