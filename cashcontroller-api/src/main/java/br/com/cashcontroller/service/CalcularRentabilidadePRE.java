package br.com.cashcontroller.service;

import br.com.cashcontroller.dto.AtivoCarteiraRFDTO;
import br.com.cashcontroller.external.dto.selic.SelicMesDTO;
import br.com.cashcontroller.external.service.IndicesService;
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

@Component
public class CalcularRentabilidadePRE implements CalcularRentabilidadeStrategy {
    private CalculaImpostoService calculaImpostoService;

    @Autowired
    public CalcularRentabilidadePRE(CalculaImpostoService calculaImpostoService) {

        this.calculaImpostoService = calculaImpostoService;
    }

    @Override
    public Integer getIdStrategy() {
        return 3;
    }

    @Override
    public double calcularRentabilidade(AtivoCarteiraRFDTO ativoCarteiraRFDTO) {
        var taxaMensal = Taxa.convertAnnualToMonthlyInterestRate(ativoCarteiraRFDTO.getTaxaContratada());

        LocalDate dataInicio = ativoCarteiraRFDTO.getDataOperacao();
        double valorDeMercado = ativoCarteiraRFDTO.getCusto();
        while (dataInicio.isBefore(LocalDate.now()) || dataInicio.equals(LocalDate.now())) {
            valorDeMercado += (valorDeMercado * taxaMensal);
            dataInicio = dataInicio.plusMonths(1);
            System.out.println("Mes: " + dataInicio.toString() + " valor:" + valorDeMercado + "taxa: " + taxaMensal);
        }

        if (!ativoCarteiraRFDTO.getIsIsento()) {
            if (valorDeMercado > ativoCarteiraRFDTO.getCusto()) {
                double lucroBrutoPresumido = valorDeMercado - ativoCarteiraRFDTO.getCusto();
                valorDeMercado = calculaImpostoService.calculaIRPFRendaFixa(ativoCarteiraRFDTO.getDataOperacao(), lucroBrutoPresumido);
                valorDeMercado+=ativoCarteiraRFDTO.getCusto();
            }
        }
        return valorDeMercado;
    }
}
