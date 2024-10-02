package br.com.cashcontroller.service.util;

import br.com.cashcontroller.dto.AtivoCarteiraRFDTO;
import br.com.cashcontroller.entity.SelicMes;
import br.com.cashcontroller.repository.SelicMesRepository;
import br.com.cashcontroller.service.interfaces.CalcularRentabilidadeStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Component
public class CalcularRentabilidadeCDI implements CalcularRentabilidadeStrategy {

    @Autowired
    SelicMesRepository selicMesRepository;
    private final CalculaImpostoService calculaImpostoService;
    @Autowired
    public CalcularRentabilidadeCDI(CalculaImpostoService calculaImpostoService) {
        this.calculaImpostoService = calculaImpostoService;
    }
    @Override
    public Integer getIdStrategy() {
        return 1;
    }

    @Override
    public double calcularRentabilidade(AtivoCarteiraRFDTO ativoCarteiraRFDTO) {

        List<SelicMes> selicMes =  selicMesRepository.findAll();
        selicMes.forEach(indice -> indice.setDataYearMonth(YearMonth.of(indice.getData().getYear(),indice.getData().getMonthValue())));
        LocalDate dataInicio = ativoCarteiraRFDTO.getDataOperacao();
        double valorDeMercado = ativoCarteiraRFDTO.getCusto();
        while (dataInicio.isBefore(LocalDate.now()) || dataInicio.equals(LocalDate.now())) {
            var dataRentabilizada = YearMonth.of(dataInicio.getYear(), dataInicio.getMonthValue());
            Optional<Double> taxaSelicMes = selicMes.stream()
                    .filter(mesAno -> mesAno.getDataYearMonth().equals(dataRentabilizada))
                    .map(SelicMes::getValor)
                    .findFirst();
            valorDeMercado += (valorDeMercado * (setTaxaContratada(ativoCarteiraRFDTO.getIdSubclasseAtivo(),ativoCarteiraRFDTO.getTaxaContratada(),taxaSelicMes.get())));
            dataInicio = dataInicio.plusMonths(1);
        }

        valorDeMercado = calculaImpostoService.getValorLiquidoDeImposto(ativoCarteiraRFDTO, valorDeMercado);
        return valorDeMercado;
    }

    private double setTaxaContratada(int subclasseRendaFixa, double taxa, double selic) {
        if(subclasseRendaFixa == 3) {
            return (selic + taxa)/100;
        } else {
            return selic * (taxa / 100)/100;
        }
    }
}
