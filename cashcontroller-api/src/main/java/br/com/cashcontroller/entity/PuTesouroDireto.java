package br.com.cashcontroller.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "pu_tesouro_direto")
public class PuTesouroDireto {
    @Id
    private Long id;

    @Column(name = "TIPO_TITULO")
    private String tipoTitulo;

    @Column(name="DATA_VENCIMENTO")
    private LocalDate dataVencimento;

    @Column(name="DATA_BASE")
    private LocalDate dataBase;

    @Column(name="TAXA_COMPRA_MANHA")
    private double taxaCompraManha;

    @Column(name="TAXA_VENDA_MANHA")
    private double taxaVendaManha;

    @Column(name="PU_COMPRA_MANHA")
    private double puCompraManha;

    @Column(name = "PU_BASE_MANHA")
    private double puBaseManha;

    @Column(name="DATA_IMPORTACAO")
    private LocalDate dataImportacao;
}
