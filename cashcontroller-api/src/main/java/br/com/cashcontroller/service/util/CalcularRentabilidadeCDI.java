package br.com.cashcontroller.service.util;

import br.com.cashcontroller.dto.AtivoCarteiraRFDTO;
import br.com.cashcontroller.entity.SelicMes;
import br.com.cashcontroller.external.service.IndicesService;
import br.com.cashcontroller.repository.SelicMesRepository;
import br.com.cashcontroller.service.interfaces.CalcularRentabilidadeStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class CalcularRentabilidadeCDI implements CalcularRentabilidadeStrategy {

    @Autowired
    SelicMesRepository selicMesRepository;

    @Autowired
    IndicesService indicesService;
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
        long t0 = System.currentTimeMillis();

        long tQuery = System.currentTimeMillis();
        List<SelicMes> selicMes =  selicMesRepository.findAll();
        log.info("[PERF]       CDI selicMesRepository.findAll(): {}ms ({} registros)", System.currentTimeMillis() - tQuery, selicMes.size());

        selicMes.forEach(indice -> indice.setDataYearMonth(YearMonth.of(indice.getData().getYear(), indice.getData().getMonthValue())));
        LocalDate dataInicio = ativoCarteiraRFDTO.getDataOperacao();
        int mesesCalculados = 0;
        double valorDeMercado = ativoCarteiraRFDTO.getCusto();
        while (dataInicio.isBefore(LocalDate.now()) || dataInicio.equals(LocalDate.now())) {
            var dataRentabilizada = YearMonth.of(dataInicio.getYear(), dataInicio.getMonthValue());
            Optional<Double> taxaSelicMes = selicMes.stream()
                    .filter(mesAno -> mesAno.getDataYearMonth().equals(dataRentabilizada))
                    .map(SelicMes::getValor)
                    .findFirst();
            valorDeMercado += (valorDeMercado * (setTaxaContratada(ativoCarteiraRFDTO.getIdSubclasseAtivo(),ativoCarteiraRFDTO.getTaxaContratada(),taxaSelicMes.get())));
            dataInicio = dataInicio.plusMonths(1);
            mesesCalculados++;
        }

        valorDeMercado = calculaImpostoService.getValorLiquidoDeImposto(ativoCarteiraRFDTO, valorDeMercado);
        log.info("[PERF]       CDI calcularRentabilidade [{}]: {}ms ({} meses iterados)", ativoCarteiraRFDTO.getSiglaAtivo(), System.currentTimeMillis() - t0, mesesCalculados);
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
