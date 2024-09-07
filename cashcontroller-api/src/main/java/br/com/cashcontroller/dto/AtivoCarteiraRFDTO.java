package br.com.cashcontroller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AtivoCarteiraRFDTO {

    private int idAtivo;
    private String nomeAtivo;
    private String siglaAtivo;
    private int idSubclasseAtivo;
    private int idIndice;
    private String indice;
    private Double taxaContratada;
    private double custodia;
    private double cotacao;
    private double oscilacaoDia;
    private double custo;
    private double valorContratado;
    private double valorMercado;
    private double valorizacao;
    private double percentual;
    private double precoMedio;
    private double totalEmProventos;
    private double ganhoDeCapital;
    private LocalDate dataOperacao;
    private int idOperacao;
    private Boolean isIsento;

    public double getValorizacao() {
        return valorContratado - custo;
    }


    public AtivoCarteiraRFDTO(LocalDate dataOperacao, boolean isIsento) {
        this.dataOperacao = dataOperacao;
        this.isIsento = isIsento;
    }

    public AtivoCarteiraRFDTO(

            int id,
            String nome,
            String sigla,
            int idsubclasseAtivo,
            Double taxaContratada,
            double custodia,
            double precoMedio,
            int idIndice,
            LocalDate dataOperacao,
            String indice,
            int idOperacao,
            Boolean isIsento

    )
    {

        this.idAtivo = id;
        this.nomeAtivo = nome;
        this.siglaAtivo = sigla;
        this.idSubclasseAtivo = idsubclasseAtivo;
        this.taxaContratada = taxaContratada != null ? taxaContratada : 0.0;
        this.custodia = custodia;
        this.precoMedio = precoMedio;
        this.idIndice = idIndice;
        this.dataOperacao = dataOperacao;
        this.indice = indice;
        this.idOperacao = idOperacao;
        this.isIsento = isIsento;
    }

}
