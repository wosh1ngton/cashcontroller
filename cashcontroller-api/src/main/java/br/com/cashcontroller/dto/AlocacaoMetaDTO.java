package br.com.cashcontroller.dto;

import br.com.cashcontroller.entity.enums.CategoriaAlocacao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlocacaoMetaDTO {
    private CategoriaAlocacao categoria;
    private String descricao;
    private Double percentual;
}
