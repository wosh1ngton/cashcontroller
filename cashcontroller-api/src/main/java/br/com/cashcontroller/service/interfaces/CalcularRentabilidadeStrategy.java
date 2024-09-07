package br.com.cashcontroller.service.interfaces;

import br.com.cashcontroller.dto.AtivoCarteiraDTO;
import br.com.cashcontroller.dto.AtivoCarteiraRFDTO;

public interface CalcularRentabilidadeStrategy {
    Integer getIdStrategy();
    double calcularRentabilidade(AtivoCarteiraRFDTO ativoCarteiraRFDTO);

}
