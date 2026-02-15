package br.com.cashcontroller.service.util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class CalcularXIRR {

    private static final double TOLERANCIA = 1e-7;
    private static final int MAX_ITERACOES = 1000;

    public static class FluxoCaixa {
        private final LocalDate data;
        private final double valor;

        public FluxoCaixa(LocalDate data, double valor) {
            this.data = data;
            this.valor = valor;
        }

        public LocalDate getData() { return data; }
        public double getValor() { return valor; }
    }

    public Double calcular(List<FluxoCaixa> fluxos) {
        if (fluxos == null || fluxos.size() < 2) return null;

        boolean temPositivo = false;
        boolean temNegativo = false;
        for (FluxoCaixa f : fluxos) {
            if (f.getValor() > 0) temPositivo = true;
            if (f.getValor() < 0) temNegativo = true;
        }
        if (!temPositivo || !temNegativo) return null;

        LocalDate dataBase = fluxos.get(0).getData();
        for (FluxoCaixa f : fluxos) {
            if (f.getData().isBefore(dataBase)) {
                dataBase = f.getData();
            }
        }

        double taxa = 0.1;

        for (int i = 0; i < MAX_ITERACOES; i++) {
            double vpl = calcularVPL(fluxos, taxa, dataBase);
            double derivada = calcularDerivadaVPL(fluxos, taxa, dataBase);

            if (Math.abs(derivada) < 1e-14) {
                taxa = taxa + 0.1;
                continue;
            }

            double novaTaxa = taxa - vpl / derivada;

            if (novaTaxa <= -1.0) {
                novaTaxa = (taxa + (-1.0)) / 2.0;
            }

            if (Math.abs(novaTaxa - taxa) < TOLERANCIA) {
                return novaTaxa;
            }

            taxa = novaTaxa;
        }

        return null;
    }

    private double calcularVPL(List<FluxoCaixa> fluxos, double taxa, LocalDate dataBase) {
        double vpl = 0;
        for (FluxoCaixa f : fluxos) {
            double anos = ChronoUnit.DAYS.between(dataBase, f.getData()) / 365.25;
            vpl += f.getValor() / Math.pow(1.0 + taxa, anos);
        }
        return vpl;
    }

    private double calcularDerivadaVPL(List<FluxoCaixa> fluxos, double taxa, LocalDate dataBase) {
        double derivada = 0;
        for (FluxoCaixa f : fluxos) {
            double anos = ChronoUnit.DAYS.between(dataBase, f.getData()) / 365.25;
            derivada -= anos * f.getValor() / Math.pow(1.0 + taxa, anos + 1);
        }
        return derivada;
    }
}
