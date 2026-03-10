package br.com.cashcontroller.service.util;

import br.com.cashcontroller.dto.AtivoCarteiraRFDTO;
import br.com.cashcontroller.service.interfaces.CalcularRentabilidadeStrategy;
import br.com.cashcontroller.utils.Taxa;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
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
        long t0 = System.currentTimeMillis();
        var taxaMensal = Taxa.convertAnnualToMonthlyInterestRate(ativoCarteiraRFDTO.getTaxaContratada());

        LocalDate dataInicio = ativoCarteiraRFDTO.getDataOperacao();
        double valorDeMercado = ativoCarteiraRFDTO.getCusto();
        int mesesCalculados = 0;
        while (dataInicio.isBefore(LocalDate.now()) || dataInicio.equals(LocalDate.now())) {
            valorDeMercado += (valorDeMercado * taxaMensal);
            dataInicio = dataInicio.plusMonths(1);
            mesesCalculados++;
        }

        if (!ativoCarteiraRFDTO.getIsIsento()) {
            if (valorDeMercado > ativoCarteiraRFDTO.getCusto()) {
                double lucroBrutoPresumido = valorDeMercado - ativoCarteiraRFDTO.getCusto();
                valorDeMercado = calculaImpostoService.calculaIRPFRendaFixa(ativoCarteiraRFDTO.getDataOperacao(), lucroBrutoPresumido);
                valorDeMercado+=ativoCarteiraRFDTO.getCusto();
            }
        }
        log.info("[PERF]       PRE calcularRentabilidade [{}]: {}ms ({} meses iterados)", ativoCarteiraRFDTO.getSiglaAtivo(), System.currentTimeMillis() - t0, mesesCalculados);
        return valorDeMercado;
    }
}
