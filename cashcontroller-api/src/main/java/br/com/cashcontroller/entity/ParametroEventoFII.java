package br.com.cashcontroller.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "PARAMETRO_EVENTO_FII")
public class ParametroEventoFII {

    @Id
    @Column(name = "ID_PARAMETRO_EVENTO_FII")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "NR_DIA_UTIL_DT_COM")
    private int diaUtilDtCom;

    @Column(name = "NR_DIA_UTIL_DT_PAGAMENTO")
    private int diaUtilDtPagamento;

    @ManyToOne
    @JoinColumn(name = "ID_ATIVO")
    private Ativo ativo;
}
