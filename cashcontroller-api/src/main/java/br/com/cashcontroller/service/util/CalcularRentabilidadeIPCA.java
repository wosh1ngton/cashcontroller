package br.com.cashcontroller.service.util;

import br.com.cashcontroller.dto.AtivoCarteiraRFDTO;
import br.com.cashcontroller.entity.IpcaMes;
import br.com.cashcontroller.repository.IpcaMesRepository;
import br.com.cashcontroller.service.interfaces.CalcularRentabilidadeStrategy;
import br.com.cashcontroller.utils.Taxa;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class CalcularRentabilidadeIPCA implements CalcularRentabilidadeStrategy {

    private CalculaImpostoService calculaImpostoService;
    @Autowired
    private IpcaMesRepository ipcaMesRepository;


    @Autowired
    public CalcularRentabilidadeIPCA(CalculaImpostoService calculaImpostoService) {
        this.calculaImpostoService = calculaImpostoService;
    }

    @Override
    public Integer getIdStrategy() {
        return 2;
    }

    @Override
    public double calcularRentabilidade(AtivoCarteiraRFDTO ativoCarteiraRFDTO) {
        long t0 = System.currentTimeMillis();

        var taxaMensal = Taxa.convertAnnualToMonthlyInterestRate(ativoCarteiraRFDTO.getTaxaContratada());

        long tQuery = System.currentTimeMillis();
        List<IpcaMes> ipcas = ipcaMesRepository.findAll();
        log.info("[PERF]       IPCA ipcaMesRepository.findAll(): {}ms ({} registros)", System.currentTimeMillis() - tQuery, ipcas.size());

        ipcas.stream().peek(indice -> indice.setDataYearMonth(YearMonth.of(indice.getData().getYear(), indice.getData().getMonthValue()))).toList();

        LocalDate inicioInvestimento = ativoCarteiraRFDTO.getDataOperacao();
        LocalDate finalInicioInvestimento = inicioInvestimento;
        List<IpcaMes> filteredList = ipcas.stream().filter(indice -> indice.getData().isAfter(finalInicioInvestimento) || indice.getData().isEqual(finalInicioInvestimento)).toList();
        double valorContratado = ativoCarteiraRFDTO.getCusto();
        int mesesCalculados = 0;
        while (inicioInvestimento.isBefore(LocalDate.now())) {
            var dataRentabilizada = YearMonth.of(inicioInvestimento.getYear(), inicioInvestimento.getMonthValue());
            Optional<Double> taxaIpcaMes = filteredList.stream()
                    .filter(mesAno -> mesAno.getDataYearMonth().equals(dataRentabilizada))
                    .map(IpcaMes::getValor)
                    .findFirst();

            double taxaIpca = taxaIpcaMes.orElseGet(() ->
                    ipcas.stream()
                            .filter(mesAno -> mesAno.getDataYearMonth().equals(dataRentabilizada.minusMonths(1)))
                            .map(IpcaMes::getValor)
                            .findFirst()
                            .orElse(0.0)
            );

            valorContratado += (valorContratado * (((taxaIpca / 100) + (taxaMensal))));
            inicioInvestimento = inicioInvestimento.plusMonths(1);
            mesesCalculados++;

        }

        if (!ativoCarteiraRFDTO.getIsIsento()) {
            if (valorContratado > ativoCarteiraRFDTO.getCusto()) {
                double lucroBrutoPresumido = valorContratado - ativoCarteiraRFDTO.getCusto();
                valorContratado = calculaImpostoService.calculaIRPFRendaFixa(ativoCarteiraRFDTO.getDataOperacao(), lucroBrutoPresumido);
                valorContratado+=ativoCarteiraRFDTO.getCusto();
            }
        }

        log.info("[PERF]       IPCA calcularRentabilidade [{}]: {}ms ({} meses iterados)", ativoCarteiraRFDTO.getSiglaAtivo(), System.currentTimeMillis() - t0, mesesCalculados);
        return valorContratado;
    }
}
