package br.com.cashcontroller.service;

import br.com.cashcontroller.dto.AtivoCarteiraDTO;
import br.com.cashcontroller.dto.enums.TipoEventoEnum;
import br.com.cashcontroller.entity.EventoRendaVariavel;
import br.com.cashcontroller.entity.OperacaoRendaVariavel;
import br.com.cashcontroller.repository.EventoRepository;
import br.com.cashcontroller.repository.OperacaoRendaVariavelRepository;
import br.com.cashcontroller.security.SecurityUtils;
import br.com.cashcontroller.service.util.CalcularXIRR;
import br.com.cashcontroller.service.util.CalcularXIRR.FluxoCaixa;
import br.com.cashcontroller.utils.Taxa;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TirService {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Autowired
    private OperacaoRendaVariavelRepository operacaoRepository;

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private CalcularXIRR calcularXIRR;

    public void calcularTirParaCarteira(List<AtivoCarteiraDTO> carteira) {
        if (carteira == null || carteira.isEmpty()) return;

        List<Integer> ativoIds = carteira.stream()
                .map(a -> a.getAtivo().getId())
                .collect(Collectors.toList());

        Long userId = SecurityUtils.getCurrentUserId();
        List<OperacaoRendaVariavel> todasOperacoes = operacaoRepository.findByAtivoIdIn(ativoIds, userId);
        List<EventoRendaVariavel> todosEventos = eventoRepository.findByAtivoIdIn(ativoIds, userId);

        Map<Integer, List<OperacaoRendaVariavel>> operacoesPorAtivo = todasOperacoes.stream()
                .collect(Collectors.groupingBy(op -> op.getAtivo().getId()));

        Map<Integer, List<EventoRendaVariavel>> eventosPorAtivo = todosEventos.stream()
                .collect(Collectors.groupingBy(ev -> ev.getAtivo().getId()));

        for (AtivoCarteiraDTO ativo : carteira) {
            try {
                int ativoId = ativo.getAtivo().getId();
                List<OperacaoRendaVariavel> operacoes = operacoesPorAtivo.getOrDefault(ativoId, Collections.emptyList());
                List<EventoRendaVariavel> eventos = eventosPorAtivo.getOrDefault(ativoId, Collections.emptyList());

                List<FluxoCaixa> fluxos = montarFluxosDeCaixa(operacoes, eventos, ativo.getValorMercado());

                Double tir = calcularXIRR.calcular(fluxos);
                ativo.setTir(tir);
                ativo.setTirMemoriaCalculo(montarMemoriaCalculo(operacoes, eventos, ativo.getValorMercado(), fluxos));
            } catch (Exception e) {
                log.warn("Erro ao calcular TIR para ativo {}: {}", ativo.getAtivo().getSigla(), e.getMessage());
                ativo.setTir(null);
                ativo.setTirMemoriaCalculo(null);
            }
        }
    }

    private String montarMemoriaCalculo(
            List<OperacaoRendaVariavel> operacoes,
            List<EventoRendaVariavel> eventos,
            double valorMercadoAtual,
            List<FluxoCaixa> fluxos) {

        double totalCompras = 0;
        int qtdCompras = 0;
        double totalVendas = 0;
        int qtdVendas = 0;
        double totalAmortizacoes = 0;
        int qtdAmortizacoes = 0;
        LocalDate primeiraData = null;

        for (OperacaoRendaVariavel op : operacoes) {
            int tipoId = op.getTipoOperacao().getId();
            if (primeiraData == null || op.getDataOperacao().isBefore(primeiraData)) {
                primeiraData = op.getDataOperacao();
            }
            switch (tipoId) {
                case 1:
                    totalCompras += op.getCustoTotal();
                    qtdCompras++;
                    break;
                case 2:
                    totalVendas += op.getValorTotal();
                    qtdVendas++;
                    break;
                case 6:
                    totalAmortizacoes += op.getCustoTotal();
                    qtdAmortizacoes++;
                    break;
            }
        }

        double totalProventos = 0;
        for (EventoRendaVariavel evento : eventos) {
            int custodiaNaData = calcularCustodiaInMemory(operacoes, evento.getDataCom());
            double valorBruto = custodiaNaData * evento.getValor();
            double valorLiquido = valorBruto;
            if (evento.getTipoEvento().getId() == TipoEventoEnum.JSCP.getId()) {
                valorLiquido = valorBruto - valorBruto * Taxa.getAliquotaJSCP(evento.getDataCom());
            }
            if (valorLiquido > 0) {
                totalProventos += valorLiquido;
            }
        }

        long diasInvestido = primeiraData != null ? ChronoUnit.DAYS.between(primeiraData, LocalDate.now()) : 0;

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Compras: R$ %,.2f (%d op.)", totalCompras, qtdCompras));
        if (qtdVendas > 0) {
            sb.append(String.format("\nVendas: R$ %,.2f (%d op.)", totalVendas, qtdVendas));
        }
        if (qtdAmortizacoes > 0) {
            sb.append(String.format("\nAmortizações: R$ %,.2f (%d op.)", totalAmortizacoes, qtdAmortizacoes));
        }
        sb.append(String.format("\nProventos líq.: R$ %,.2f (%d eventos)", totalProventos, eventos.size()));
        sb.append(String.format("\nValor de mercado: R$ %,.2f", valorMercadoAtual));
        sb.append(String.format("\nFluxos de caixa: %d", fluxos.size()));
        if (primeiraData != null) {
            sb.append(String.format("\nPeríodo: %s até hoje (%d dias)", primeiraData.format(FMT), diasInvestido));
        }

        return sb.toString();
    }

    private List<FluxoCaixa> montarFluxosDeCaixa(
            List<OperacaoRendaVariavel> operacoes,
            List<EventoRendaVariavel> eventos,
            double valorMercadoAtual) {

        List<FluxoCaixa> fluxos = new ArrayList<>();

        for (OperacaoRendaVariavel op : operacoes) {
            int tipoId = op.getTipoOperacao().getId();
            switch (tipoId) {
                case 1: // COMPRA
                    fluxos.add(new FluxoCaixa(op.getDataOperacao(), -op.getCustoTotal()));
                    break;
                case 2: // VENDA
                    fluxos.add(new FluxoCaixa(op.getDataOperacao(), op.getValorTotal()));
                    break;
                case 6: // AMORTIZACAO
                    fluxos.add(new FluxoCaixa(op.getDataOperacao(), op.getCustoTotal()));
                    break;
            }
        }

        for (EventoRendaVariavel evento : eventos) {
            int custodiaNaData = calcularCustodiaInMemory(operacoes, evento.getDataCom());
            double valorBruto = custodiaNaData * evento.getValor();
            double valorLiquido = valorBruto;

            if (evento.getTipoEvento().getId() == TipoEventoEnum.JSCP.getId()) {
                valorLiquido = valorBruto - valorBruto * Taxa.getAliquotaJSCP(evento.getDataCom());
            }

            if (valorLiquido > 0) {
                fluxos.add(new FluxoCaixa(evento.getDataPagamento(), valorLiquido));
            }
        }

        if (valorMercadoAtual > 0) {
            fluxos.add(new FluxoCaixa(LocalDate.now(), valorMercadoAtual));
        }

        return fluxos;
    }

    private int calcularCustodiaInMemory(List<OperacaoRendaVariavel> operacoes, LocalDate dataCom) {
        int custodia = 0;
        for (OperacaoRendaVariavel op : operacoes) {
            if (op.getDataOperacao().isAfter(dataCom)) break;

            int tipoId = op.getTipoOperacao().getId();
            switch (tipoId) {
                case 1: // COMPRA
                case 3: // DESDOBRAMENTO
                case 4: // BONIFICACAO
                    custodia += op.getQuantidadeNegociada();
                    break;
                case 2: // VENDA
                case 5: // GRUPAMENTO
                    custodia -= op.getQuantidadeNegociada();
                    break;
                case 6: // AMORTIZACAO
                    break;
            }
        }
        return Math.max(custodia, 0);
    }
}
