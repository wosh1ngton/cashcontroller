package br.com.cashcontroller.service;

import br.com.cashcontroller.dto.AtivoCarteiraRFDTO;
import br.com.cashcontroller.entity.IpcaMes;
import br.com.cashcontroller.external.dto.selic.SelicMesDTO;
import br.com.cashcontroller.external.service.IndicesService;
import br.com.cashcontroller.repository.IpcaMesRepository;
import br.com.cashcontroller.service.interfaces.CalcularRentabilidadeStrategy;
import br.com.cashcontroller.utils.DataUtil;
import br.com.cashcontroller.utils.Taxa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

        var taxaMensal = Taxa.convertAnnualToMonthlyInterestRate(ativoCarteiraRFDTO.getTaxaContratada());
        List<IpcaMes> ipcas = ipcaMesRepository.findAll();
        ipcas.stream().peek(indice -> indice.setDataYearMonth(YearMonth.of(indice.getData().getYear(), indice.getData().getMonthValue()))).collect(Collectors.toList());

        LocalDate inicioInvestimento = ativoCarteiraRFDTO.getDataOperacao();
        LocalDate finalInicioInvestimento = inicioInvestimento;
        List<IpcaMes> filteredList = ipcas.stream().filter(indice -> indice.getData().isAfter(finalInicioInvestimento) || indice.getData().isEqual(finalInicioInvestimento)).collect(Collectors.toList());
        double valorContratado = ativoCarteiraRFDTO.getCusto();
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
                            .orElse(0.0) // Provide a default value if previous month's value is also not present
            );

            valorContratado += (valorContratado * (((taxaIpca / 100) + (taxaMensal))));
            System.out.println("ativo: " + ativoCarteiraRFDTO.getNomeAtivo() + " Mes: " + inicioInvestimento.toString() + " valor:" + valorContratado + "taxa: " + taxaMensal);
            inicioInvestimento = inicioInvestimento.plusMonths(1);

        }

        if (!ativoCarteiraRFDTO.getIsIsento()) {
            if (valorContratado > ativoCarteiraRFDTO.getCusto()) {
                double lucroBrutoPresumido = valorContratado - ativoCarteiraRFDTO.getCusto();
                valorContratado = calculaImpostoService.calculaIRPFRendaFixa(ativoCarteiraRFDTO.getDataOperacao(), lucroBrutoPresumido);
                valorContratado+=ativoCarteiraRFDTO.getCusto();
            }
        }


        return valorContratado;
    }
}
