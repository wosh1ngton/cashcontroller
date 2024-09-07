package br.com.cashcontroller.dto;

import br.com.cashcontroller.entity.Ativo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AtivoCarteiraDTO {
    private int id;
    private Ativo ativo;
    private double custodia;
    private double cotacao;
    private double oscilacaoDia;
    private double custo;
    private double valorMercado;
    private double valorizacao;
    private double percentual;
    private double precoMedio;
    private double totalEmProventos;
    private double ganhoDeCapital;

    public double getValorizacao() {
        return valorMercado - custo;
    }

    public AtivoCarteiraDTO(Ativo ativo, double custodia, double precoMedio) {
        this.ativo = ativo;
        this.custodia = custodia;
        this.precoMedio = precoMedio;
    }

    public AtivoCarteiraDTO(Integer id, String nome, String sigla, double custodia, double precoMedio) {

        this.getAtivo().setId(id);
        this.getAtivo().setNome(nome);
        this.getAtivo().setSigla(sigla);
        this.custodia = custodia;
        this.precoMedio = precoMedio;
    }


}
