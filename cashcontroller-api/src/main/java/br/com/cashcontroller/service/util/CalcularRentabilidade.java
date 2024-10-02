package br.com.cashcontroller.service.util;

import br.com.cashcontroller.dto.AtivoCarteiraRFDTO;
import br.com.cashcontroller.service.interfaces.CalcularRentabilidadeStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CalcularRentabilidade {

    @Autowired List<CalcularRentabilidadeStrategy> strategies;

    public double calcularRentabilidade(AtivoCarteiraRFDTO ativoCarteiraRFDTO) {
        CalcularRentabilidadeStrategy strategy = strategies.stream()
                .filter(a -> a.getIdStrategy().equals(ativoCarteiraRFDTO.getIdIndice()))
                .findAny()
                .get();

        return strategy.calcularRentabilidade(ativoCarteiraRFDTO);
    }




}
