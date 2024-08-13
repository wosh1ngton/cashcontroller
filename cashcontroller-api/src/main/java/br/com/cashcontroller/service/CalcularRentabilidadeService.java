package br.com.cashcontroller.service;

import br.com.cashcontroller.dto.AtivoCarteiraRFDTO;
import br.com.cashcontroller.external.dto.selic.SelicMesDTO;
import br.com.cashcontroller.external.service.IndicesService;
import br.com.cashcontroller.service.interfaces.CalcularRentabilidadeStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CalcularRentabilidadeService {

    @Autowired List<CalcularRentabilidadeStrategy> strategies;

    public double calcularRentabilidade(AtivoCarteiraRFDTO ativoCarteiraRFDTO) {
        CalcularRentabilidadeStrategy strategy = strategies.stream()
                .filter(a -> a.getIdStrategy().equals(ativoCarteiraRFDTO.getIdIndice()))
                .findAny()
                .get();

        return strategy.calcularRentabilidade(ativoCarteiraRFDTO);
    }


}
