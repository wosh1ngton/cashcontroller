package br.com.cashcontroller.service;

import br.com.cashcontroller.dto.AtivoCarteiraRFDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Service
public class CalculaImpostoService {

    public double getValorLiquidoDeImposto(AtivoCarteiraRFDTO ativoCarteiraRFDTO, double valorBruto) {
        if (!ativoCarteiraRFDTO.getIsIsento()) {
            if (valorBruto > ativoCarteiraRFDTO.getCusto()) {
                double lucroBrutoPresumido = valorBruto - ativoCarteiraRFDTO.getCusto();
                valorBruto = calculaIRPFRendaFixa(ativoCarteiraRFDTO.getDataOperacao(), lucroBrutoPresumido);
                valorBruto+=ativoCarteiraRFDTO.getCusto();
            }
        }
        return valorBruto;
    }

    public double calculaIRPFRendaFixa(LocalDate dataInvestimento, double valor) {

        LocalDate hoje = LocalDate.now();
        Period period = Period.between(dataInvestimento, hoje);
        double totalMesesDecorrido = period.getYears() * 12 + period.getMonths();
        if (totalMesesDecorrido <= 6) {
            valor -= valor * 0.2250;
        } else if (totalMesesDecorrido > 6 && totalMesesDecorrido <= 12) {
            valor -= valor * 0.2000;
        } else if (totalMesesDecorrido > 12 && totalMesesDecorrido <= 24) {
            valor -= valor * 0.1750;
        } else {
            valor -= valor * 0.1500;
        }
        return valor;
    }

    public double getValorLiquidoImpostoEvento(double valorBruto, boolean isIsento, LocalDate dataInvestimento) {
        var valorLiquido = valorBruto;
        if (!isIsento) {
            valorLiquido = calculaIRPFRendaFixa(dataInvestimento, valorBruto);
        }
        return valorLiquido;
    }
}
