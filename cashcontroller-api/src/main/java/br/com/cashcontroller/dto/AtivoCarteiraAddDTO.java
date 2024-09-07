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
public class AtivoCarteiraAddDTO {
    private int id;
    private int ativo;
    private double custo;
    private double custodia;
    private double percentual;
    private double proventos;
    private double valorMercado;


}
