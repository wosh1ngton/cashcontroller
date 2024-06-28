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

    private Ativo ativo;
    private double custodia;
    private double cotacao;
    private double oscilacaoDia;
    private double custo;
    private double valorMercado;
    private double percentual;
    private double precoMedio;

    public AtivoCarteiraDTO(Ativo ativo, double custodia, double precoMedio) {
        this.ativo = ativo;
        this.custodia = custodia;
        this.precoMedio = precoMedio;
    }


}
