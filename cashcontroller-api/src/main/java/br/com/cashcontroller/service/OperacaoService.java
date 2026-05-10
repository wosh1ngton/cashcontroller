package br.com.cashcontroller.service;

import br.com.cashcontroller.dto.*;
import br.com.cashcontroller.dto.enums.SubclasseAtivoEnum;
import br.com.cashcontroller.dto.enums.TipoOperacaoEnum;
import br.com.cashcontroller.entity.*;
import br.com.cashcontroller.external.dto.FeriadoDTO;
import br.com.cashcontroller.external.service.FeriadosService;
import br.com.cashcontroller.external.service.TesouroService;
import br.com.cashcontroller.mapper.OperacaoRendaFixaMapper;
import br.com.cashcontroller.mapper.OperacaoRendaVariavelMapper;
import br.com.cashcontroller.mapper.TipoOperacaoMapper;
import br.com.cashcontroller.repository.*;
import br.com.cashcontroller.security.SecurityUtils;
import br.com.cashcontroller.service.util.CalculaImpostoService;
import br.com.cashcontroller.service.util.CalcularRentabilidade;
import br.com.cashcontroller.utils.Taxa;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class OperacaoService {


    @Autowired
    CalculaImpostoService calculaImpostoService;
    @Autowired
    OperacaoRendaVariavelRepository operacaoRendaVariavelRepository;
    @Autowired
    OperacaoRendaFixaRepository operacaoRendaFixaRepository;
    @Autowired
    TipoOperacaoRepository tipoOperacaoRepository;
    @Autowired
    EventoService eventoService;
    @Autowired
    CalcularRentabilidade calcularRentabilidade;
    @Autowired
    TesouroService tesouroService;

    @Autowired
    PrejuizoCompensatorioService prejuizoCompensatorioService;
    @Autowired
    AtivoCarteiraRepository ativoCarteiraRepository;
    @Autowired
    AtivoRepository ativoRepository;

    @Autowired
    PuTesouroDiretoRepository puTesouroDiretoRepository;

    @Autowired
    AporteRepository aporteRepository;
    private final FeriadosService feriadosService;
    private List<FeriadoDTO> listaFeriados;


    @Autowired
    public OperacaoService(FeriadosService feriadosService) {
        this.feriadosService = feriadosService;
    }

    public List<OperacaoRendaVariavelDTO> listarOperacoesRendaVariavel() {
        List<OperacaoRendaVariavel> operacoes = operacaoRendaVariavelRepository.findAllByUser(SecurityUtils.getCurrentUserId());
        return OperacaoRendaVariavelMapper.INSTANCE.toListDTO(operacoes);
    }

    public List<OperacaoRendaVariavelDTO> listarOperacoesRendaVariavelPorData(Filter filter) {
        List<OperacaoRendaVariavel> operacoes = operacaoRendaVariavelRepository.findOperacoesByData(filter.getStartDate(), filter.getEndDate(), filter.getSubclasse(), filter.getAno(), filter.getMes(), filter.getAtivo(), SecurityUtils.getCurrentUserId());
        return OperacaoRendaVariavelMapper.INSTANCE.toListDTO(operacoes);
    }

    public List<OperacaoRendaFixaDTO> listarOperacoesRendaFixaPorData(Filter filter) {

        List<OperacaoRendaFixa> operacoes = operacaoRendaFixaRepository.findOperacoesByData(filter.getStartDate(), filter.getEndDate(), filter.getSubclasse(), filter.getAno(), filter.getMes(), SecurityUtils.getCurrentUserId());
        return OperacaoRendaFixaMapper.INSTANCE.toListDTO(operacoes);
    }


    public double valorTotalOperacao(OperacaoRendaVariavelSaveDTO operacaoDto) {
        double valorBase = operacaoDto.getValorUnitario() * operacaoDto.getQuantidadeNegociada();
        return calcularTaxaseImpostos(operacaoDto, valorBase);
    }

    public double custoTotalOperacao(OperacaoRendaVariavelSaveDTO operacaoDto) {

        double valorBase = operacaoDto.getValorUnitario() * operacaoDto.getQuantidadeNegociada();
        if (operacaoDto.getTipoOperacaoDto() == TipoOperacaoEnum.VENDA.getId()) {
            LocalDate startDate = LocalDate.of(2015, 1, 1);
            double pmAtivo = this.operacaoRendaVariavelRepository.calcularPrecoMedio(operacaoDto.getAtivoDto(), operacaoDto.getDataOperacao(), startDate, SecurityUtils.getCurrentUserId()).getPrecoMedio();
            valorBase = pmAtivo * operacaoDto.getQuantidadeNegociada();
        }
        return calcularTaxaseImpostos(operacaoDto, valorBase);
    }

    private static double calcularTaxaseImpostos(OperacaoRendaVariavelSaveDTO operacaoDto, double valorBase) {
        double impostos = 0d;
        if (operacaoDto.getValorCorretagem() > 0) {
            impostos = (operacaoDto.getValorCorretagem() / Taxa.IMPOSTOS) - operacaoDto.getValorCorretagem();
        }
        double taxas = Taxa.TAXA_LIQUIDACAO_B3 + Taxa.TAXA_EMOLUMENTOS;
        double taxaCorretora = operacaoDto.getValorCorretagem() * Taxa.TAXA_OPERACIONAL_XP;
        double custos = impostos + (valorBase * (taxas / 100));
        if(operacaoDto.getTipoOperacaoDto() == TipoOperacaoEnum.AMORTIZACAO.getId()) { custos = 0d; }
        return valorBase + custos + operacaoDto.getValorCorretagem() + taxaCorretora;
    }

    public List<OperacaoRendaFixaDTO> listarOperacoesRendaFixa() {
        List<OperacaoRendaFixa> operacoes = operacaoRendaFixaRepository.findAllByUser(SecurityUtils.getCurrentUserId());
        return OperacaoRendaFixaMapper.INSTANCE.toListDTO(operacoes);
    }

    public List<TipoOperacaoDTO> listarTipoOperacao() {
        List<TipoOperacao> tiposOperacao = tipoOperacaoRepository.findAll();
        return TipoOperacaoMapper.INSTANCE.toListDTO(tiposOperacao);
    }

    public List<ItemLabelDTO> listarAtivosOperados() {
        return this.operacaoRendaVariavelRepository.findDistinctAtivo(SecurityUtils.getCurrentUserId());
    }

    public OperacaoRendaVariavelDTO cadastrarOperacaoRendaVariavel(OperacaoRendaVariavelSaveDTO operacaoRendaVariavelSaveDTO) {


        if (operacaoRendaVariavelSaveDTO.getTipoOperacaoDto() == TipoOperacaoEnum.DESDOBRAMENTO.getId()) {
            operacaoRendaVariavelSaveDTO.setQuantidadeNegociada(desdobrarAtivo(operacaoRendaVariavelSaveDTO));
        }

        if (operacaoRendaVariavelSaveDTO.getTipoOperacaoDto() == TipoOperacaoEnum.GRUPAMENTO.getId()) {
            operacaoRendaVariavelSaveDTO.setQuantidadeNegociada(agruparAtivo(operacaoRendaVariavelSaveDTO));
        }

        operacaoRendaVariavelSaveDTO.setCustoTotal(custoTotalOperacao(operacaoRendaVariavelSaveDTO));
        operacaoRendaVariavelSaveDTO.setValorTotal(valorTotalOperacao(operacaoRendaVariavelSaveDTO));

        if(operacaoRendaVariavelSaveDTO.getTipoOperacaoDto() == TipoOperacaoEnum.AMORTIZACAO.getId()) {
            operacaoRendaVariavelSaveDTO.setValorTotal(0);
        }
        updateOrCreateAtivoCarteira(operacaoRendaVariavelSaveDTO.getAtivoDto(), operacaoRendaVariavelSaveDTO.getQuantidadeNegociada(), operacaoRendaVariavelSaveDTO.getCustoTotal(), operacaoRendaVariavelSaveDTO.getTipoOperacaoDto());

        OperacaoRendaVariavel operacao = OperacaoRendaVariavelMapper.INSTANCE.toSaveEntity(operacaoRendaVariavelSaveDTO);
        operacao.setUser(SecurityUtils.getCurrentUser());
        return OperacaoRendaVariavelMapper.INSTANCE.toDTO(operacaoRendaVariavelRepository.save(operacao));
    }


    private int desdobrarAtivo(OperacaoRendaVariavelSaveDTO operacaoRendaVariavelSaveDTO) {
        int fatorDeProporcao = operacaoRendaVariavelSaveDTO.getQuantidadeNegociada();
        int totalAtualDeAcoes = this.operacaoRendaVariavelRepository.getCustodiaPorAtivo(operacaoRendaVariavelSaveDTO.getAtivoDto(), SecurityUtils.getCurrentUserId());
        int totalDoAumento = (fatorDeProporcao * totalAtualDeAcoes) - totalAtualDeAcoes;
        return totalDoAumento;
    }

    private int agruparAtivo(OperacaoRendaVariavelSaveDTO operacaoRendaVariavelSaveDTO) {
        int fatorDeProporcao = operacaoRendaVariavelSaveDTO.getQuantidadeNegociada();
        int totalAtualDeAcoes = this.operacaoRendaVariavelRepository.getCustodiaPorAtivo(operacaoRendaVariavelSaveDTO.getAtivoDto(), SecurityUtils.getCurrentUserId());
        int totalDecrescimo = totalAtualDeAcoes - (totalAtualDeAcoes / fatorDeProporcao);
        return totalDecrescimo;
    }


    public OperacaoRendaVariavelDTO atualizarOperacaoRendaVariavel(OperacaoRendaVariavelDTO operacaoRendaVariavelDTO) {

        var saveDto = OperacaoRendaVariavelMapper.INSTANCE.FromDTOtoSaveDTO(operacaoRendaVariavelDTO);
        operacaoRendaVariavelDTO.setCustoTotal(custoTotalOperacao(saveDto));
        operacaoRendaVariavelDTO.setValorTotal(valorTotalOperacao(saveDto));

        var operacaoExistente = this.operacaoRendaVariavelRepository.findByIdAndUser(operacaoRendaVariavelDTO.getId(), SecurityUtils.getCurrentUserId())
                .orElseThrow(() -> new NullPointerException("registro.nao.encontrado"));


        double quantidadeDiff = operacaoRendaVariavelDTO.getQuantidadeNegociada() - operacaoExistente.getQuantidadeNegociada();
        double custoDiff = operacaoRendaVariavelDTO.getCustoTotal() - operacaoExistente.getCustoTotal();
        var operacao = OperacaoRendaVariavelMapper.INSTANCE.toEntity(operacaoRendaVariavelDTO);
        operacao.setUser(operacaoExistente.getUser());

        if (!posicaoLiquidada(operacao.getAtivo().getId())) {
            updateOrCreateAtivoCarteira(operacao.getAtivo().getId(), quantidadeDiff, custoDiff, operacaoRendaVariavelDTO.getTipoOperacaoDto().getId());
        }
        var operacaoDto = OperacaoRendaVariavelMapper.INSTANCE.toDTO(operacaoRendaVariavelRepository.save(operacao));
        return operacaoDto;
    }

    private boolean posicaoLiquidada(int idAtivo) {
        int custodia = operacaoRendaVariavelRepository.getCustodiaPorAtivo(idAtivo, SecurityUtils.getCurrentUserId());
        return custodia == 0;
    }

    private void updateOrCreateAtivoCarteira(int ativo, double quantidadeDiff, double custoDiff, int tipoOperacao) {
        Optional<AtivoCarteira> ativoCarteira = ativoCarteiraRepository.findByIdAtivo(ativo, SecurityUtils.getCurrentUserId());

        if (!ativoCarteira.isPresent()) {
            AtivoCarteira novoAtivoCarteira = new AtivoCarteira();
            novoAtivoCarteira.setAtivo(ativoRepository.findByIdVisibleToUser(ativo, SecurityUtils.getCurrentUserId()).get());
            novoAtivoCarteira.setCustodia(quantidadeDiff);
            novoAtivoCarteira.setCusto(custoDiff);
            novoAtivoCarteira.setUser(SecurityUtils.getCurrentUser());
            ativoCarteiraRepository.save(novoAtivoCarteira);
        } else {

            if (tipoOperacao == 1) {
                ativoCarteira.get().setCustodia(ativoCarteira.get().getCustodia() + quantidadeDiff);
                ativoCarteira.get().setCusto(ativoCarteira.get().getCusto() + custoDiff);
            } else if (tipoOperacao == 2) {
                ativoCarteira.get().setCustodia(ativoCarteira.get().getCustodia() - quantidadeDiff);
                ativoCarteira.get().setCusto(ativoCarteira.get().getCusto() - custoDiff);
                if (ativoCarteira.get().getCustodia() == 0) {
                    ativoCarteiraRepository.delete(ativoCarteira.get());
                    return;
                }
            }
            ativoCarteiraRepository.save(ativoCarteira.get());
        }

    }

    public OperacaoRendaFixaDTO cadastrarOperacaoRendaFixa(OperacaoRendaFixaDTO operacaoRendaFixaDto) {

        operacaoRendaFixaDto.setCustoTotal(calcularCustoOperacaoRendaFixa(operacaoRendaFixaDto));
        operacaoRendaFixaDto.setValorTotal(cadastrarValorVendaOperacaoRendaFixa(operacaoRendaFixaDto));
        OperacaoRendaFixa operacao = OperacaoRendaFixaMapper.INSTANCE.toEntity(operacaoRendaFixaDto);
        operacao.setUser(SecurityUtils.getCurrentUser());
        return OperacaoRendaFixaMapper.INSTANCE.toDTO(operacaoRendaFixaRepository.save(operacao));
    }


    private Double cadastrarValorVendaOperacaoRendaFixa(OperacaoRendaFixaDTO operacaoRendaFixaDto) {
        var valorTotal = 0.0;
        if (operacaoRendaFixaDto.getTipoOperacaoDto().getId() == TipoOperacaoEnum.VENDA.getId()) {
            valorTotal = operacaoRendaFixaDto.getQuantidadeNegociada() * operacaoRendaFixaDto.getValorUnitario()
                    - operacaoRendaFixaDto.getValorCorretagem();
            return valorTotal;
        }
        return valorTotal;
    }

    private Double calcularCustoOperacaoRendaFixa(OperacaoRendaFixaDTO operacaoRendaFixaDto) {
        var custoTotal = 0.0;
        if (operacaoRendaFixaDto.getTipoOperacaoDto().getId() == TipoOperacaoEnum.VENDA.getId()) {
            custoTotal = operacaoRendaFixaDto.getQuantidadeNegociada() * operacaoRendaFixaDto.getValorUnitario()
                    + operacaoRendaFixaDto.getValorCorretagem();
            return custoTotal;
        } else if (operacaoRendaFixaDto.getTipoOperacaoDto().getId() == TipoOperacaoEnum.COMPRA.getId()) {
            double valorBase = 0.0;
            LocalDate startDate = LocalDate.of(2015, 1, 1);
            double pmAtivo = operacaoRendaFixaDto.getValorUnitario();
            var operacoes = this.operacaoRendaVariavelRepository.listarOperacoesPorAtivo(operacaoRendaFixaDto.getAtivoDto().getId(), SecurityUtils.getCurrentUserId());
            if(!operacoes.isEmpty()) {
                pmAtivo = this.operacaoRendaFixaRepository.calcularPrecoMedio(operacaoRendaFixaDto.getAtivoDto().getId(),
                        operacaoRendaFixaDto.getDataOperacao(),
                        startDate, SecurityUtils.getCurrentUserId()).getPrecoMedio();
            }
            return pmAtivo * operacaoRendaFixaDto.getQuantidadeNegociada();

        }
        return custoTotal;
    }

    @Transactional
    public OperacaoRendaFixaDTO atualizarOperacaoRendaFixa(OperacaoRendaFixaDTO operacaoRendaFixaDTO) {

        Optional<OperacaoRendaFixa> operacaoOptional = this.operacaoRendaFixaRepository.findByIdAndUser(operacaoRendaFixaDTO.getId(), SecurityUtils.getCurrentUserId());
        if (operacaoOptional.isPresent()) {
            OperacaoRendaFixa operacaoUpdated;
            operacaoUpdated = OperacaoRendaFixaMapper.INSTANCE.toEntity(operacaoRendaFixaDTO);
            operacaoUpdated.setUser(operacaoOptional.get().getUser());
            operacaoUpdated.setCustoTotal(calcularCustoOperacaoRendaFixa(operacaoRendaFixaDTO));
            operacaoUpdated.setValorTotal(cadastrarValorVendaOperacaoRendaFixa(operacaoRendaFixaDTO));
            operacaoUpdated = operacaoRendaFixaRepository.save(operacaoUpdated);
            operacaoRendaFixaRepository.flush();
            return OperacaoRendaFixaMapper.INSTANCE.toDTO(operacaoUpdated);
        } else {
            throw new RuntimeException("não foi possível salvar a operação");
        }

    }

    public void excluirOperacaoRendaVariavel(int id) {

        Optional<OperacaoRendaVariavel> operacao = operacaoRendaVariavelRepository.findByIdAndUser(id, SecurityUtils.getCurrentUserId());
        operacao.ifPresent(value -> {
            updateOrCreateAtivoCarteira(value.getAtivo().getId(), value.getQuantidadeNegociada(), value.getCustoTotal(), 2);
            this.operacaoRendaVariavelRepository.delete(value);
        });

    }

    public void excluirOperacaoRendaFixa(int id) {

        Optional<OperacaoRendaFixa> operacao = operacaoRendaFixaRepository.findByIdAndUser(id, SecurityUtils.getCurrentUserId());
        operacao.ifPresent(value -> this.operacaoRendaFixaRepository.delete(value));

    }


    public OperacaoRendaVariavelDTO findById(Integer id) {
        return OperacaoRendaVariavelMapper.INSTANCE.toDTO(operacaoRendaVariavelRepository.findByIdAndUser(id, SecurityUtils.getCurrentUserId()).get());
    }

    public void atualizarPrejuizoAcumulado(String anoMes, Integer subclasseAtivoId) {
        Filter filtro = new Filter();
        parserAnoMes(anoMes, filtro);
        List<OperacaoRendaVariavelDTO> vendasDoMes = listarVendasDoMes(filtro);
        oterResultadoOperacao(vendasDoMes);
        double resultadoMes = obterResultadoMesPorSubclasse(vendasDoMes,subclasseAtivoId);
        Double valorVendasMes = vendasDoMes.stream()
                .filter(op -> op.getAtivoDto().getSubclasseAtivoDto().getId() == subclasseAtivoId

        ).mapToDouble(op -> op.getValorTotal()).sum();
        prejuizoCompensatorioService.atualizaPrejuizoAcumulado(anoMesAnteriorString(anoMes), anoMes, subclasseAtivoId, resultadoMes, valorVendasMes);
    }

    private static void parserAnoMes(String anoMes, Filter filtro) {
        var arr = (anoMes.split("-"));
        filtro.setAno(Integer.parseInt(arr[1]));
        filtro.setMes(Integer.parseInt(arr[0]));
        filtro.setEndDate(LocalDate.of(filtro.getAno(), filtro.getMes(),1)
                .with(TemporalAdjusters.lastDayOfMonth()));
    }

    private String anoMesAnteriorString(String anoMesAtual) {
        var arr = (anoMesAtual.split("-"));
        var data = LocalDate.of(Integer.parseInt(arr[1]), Integer.parseInt(arr[0]), 1);
        var mesAnterior = data.minusMonths(1);
        String mesAnterioString = mesAnterior.format(DateTimeFormatter.ofPattern("MM-yyyy"));
        return mesAnterioString;
    }

    public IrpfMesDTO calcularImpostoMensal(Filter filter) {

        List<OperacaoRendaVariavelDTO> vendasDoMes = listarVendasDoMes(filter);
        oterResultadoOperacao(vendasDoMes);
        double resultadoMes = obterResultadoMesPorSubclasse(vendasDoMes, filter.getSubclasse());

        IrpfMesDTO irMes = new IrpfMesDTO();

        String monthName = filter.getEndDate().getMonth().getDisplayName(TextStyle.FULL, new Locale("pt"));
        irMes.setMes(monthName);
        var valorTotalEmVendas = vendasDoMes.stream()
                .filter(op -> op.getAtivoDto().getSubclasseAtivoDto().getId() == filter.getSubclasse())
                .mapToDouble(op -> op.getValorTotal()).sum();

        irMes.setAtivosVendidos(vendasDoMes);
        irMes.setResultadoMes(resultadoMes);
        irMes.setTotalVendido(valorTotalEmVendas);
        if (valorTotalEmVendas > Taxa.LIMITE_IR && resultadoMes > 0) {
            irMes.setImposto(true);
            irMes.setValorAPagar(resultadoMes * Taxa.ALIQUOTA_IR);
        }
        LocalDate previousMonth = filter.getEndDate().minusMonths(1);
        String formatted = previousMonth.format(DateTimeFormatter.ofPattern("MM-yyyy"));

        Double pejuizoACompensar = prejuizoCompensatorioService.getPrejuizoMesAnterior(formatted, filter.getSubclasse());
        if(pejuizoACompensar == null) {
            pejuizoACompensar = 0d;
        }
        irMes.setPrejuizoCompensar(pejuizoACompensar);
        setPrejuizoAcumuladoAtualizado(irMes, pejuizoACompensar, valorTotalEmVendas, filter.getSubclasse());

        return irMes;
    }

    private static void setPrejuizoAcumuladoAtualizado(IrpfMesDTO irMes, Double pejuizoACompensar, double valorTotalEmVendas, Integer subclasseAtivoId) {
        if(irMes.getResultadoMes() < 0) {
            irMes.setPrejuizoPosResultado(pejuizoACompensar + Math.abs(irMes.getResultadoMes()));
        } else if(irMes.getResultadoMes() > 0 && valorTotalEmVendas > 20000 && subclasseAtivoId.equals(SubclasseAtivoEnum.ACAO.getId()) || irMes.getResultadoMes() > 0 && subclasseAtivoId.equals(SubclasseAtivoEnum.FII.getId())) {
            irMes.setPrejuizoPosResultado(pejuizoACompensar - irMes.getResultadoMes());
        } else {
            irMes.setPrejuizoPosResultado(pejuizoACompensar);
        }
    }

    private Double obterResultadoMesPorSubclasse(
            List<OperacaoRendaVariavelDTO> vendasDoMes,
            int idSubclasseAtivo
    ) {
        if (vendasDoMes == null) return 0.0;

        return vendasDoMes.stream()
                .filter(op -> op.getAtivoDto().getSubclasseAtivoDto().getId() == idSubclasseAtivo)
                .mapToDouble(OperacaoRendaVariavelDTO::getResultadoOperacao)
                .sum();
        }


    private void oterResultadoOperacao(List<OperacaoRendaVariavelDTO> vendasDoMes) {
        for (OperacaoRendaVariavelDTO op : vendasDoMes) {
            double resultadoOperacao = op.getValorTotal() - op.getCustoTotal();
            op.setResultadoOperacao(resultadoOperacao);
        }
    }


    private AtivoDTO calcularPrecoMedio(int idAtivo, LocalDate dataDeCorte) {
        LocalDate startDate = LocalDate.of(2015, 1, 1);
        return this.operacaoRendaVariavelRepository.calcularPrecoMedio(idAtivo, dataDeCorte, startDate, SecurityUtils.getCurrentUserId());

    }


    private List<OperacaoRendaVariavelDTO> listarVendasDoMes(Filter filter) {
        var mes = filter.getEndDate().getMonth();
        var operacoes = listarOperacoesRendaVariavelPorData(filter);

        var operacoesDeVenda = operacoes.stream().filter(op -> op.getTipoOperacaoDto().getNome()
                .contains("Venda") && op.getDataOperacao().getMonth() == mes).collect(Collectors.toList());
        return operacoesDeVenda;
    }

    public List<AtivoCarteiraDTO> listarCarteiraDeAcoes() {
        var carteira = this.operacaoRendaVariavelRepository.listarCarteiraDeAcoes(SecurityUtils.getCurrentUserId());
        carteira
                .forEach(ativoCarteira -> {

                            ativoCarteira.setTotalEmProventos(this.eventoService.getTotalProventosPorAtivo(ativoCarteira.getAtivo().getId()));
                            ativoCarteira.setGanhoDeCapital(this.calcularGanhoDeCapital(ativoCarteira.getAtivo().getId()));
                        }
                );

        return carteira;
    }

    private double calculaIRPFRendaFixa(LocalDate dataInvestimento, double valor) {

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


    public List<AtivoCarteiraRFDTO> listarCarteiraRendaFixa() {
        long inicio = System.currentTimeMillis();

        long t0 = System.currentTimeMillis();
        List<AtivoCarteiraRFDTO> operacoesAtivosCustodiados = this.operacaoRendaFixaRepository.listarOperacoesAtivosCustodiados(SecurityUtils.getCurrentUserId());
        log.info("[PERF] listarOperacoesAtivosCustodiados: {}ms ({} registros)", System.currentTimeMillis() - t0, operacoesAtivosCustodiados.size());

        t0 = System.currentTimeMillis();
        List<String> titulosTesouro = operacoesAtivosCustodiados.stream()
                .filter(op -> op.getIdSubclasseAtivo() == SubclasseAtivoEnum.TESOURO_DIRETO.getId())
                .map(AtivoCarteiraRFDTO::getNomeAtivo)
                .distinct()
                .collect(Collectors.toList());
        List<PuTesouroDireto> precosTesouro = titulosTesouro.isEmpty()
                ? Collections.emptyList()
                : puTesouroDiretoRepository.listarValoresMaisRecentesPorTitulos(titulosTesouro);
        log.info("[PERF] listarValoresMaisRecentesPorTitulo: {}ms ({} titulos, {} registros)", System.currentTimeMillis() - t0, titulosTesouro.size(), precosTesouro.size());

        t0 = System.currentTimeMillis();
            operacoesAtivosCustodiados = operacoesAtivosCustodiados.stream().filter(operacaoAtivo -> {
                List<OperacaoRendaFixa> operacoes = verificarVendaParcial(operacaoAtivo);
                return operacoes.isEmpty() || operacoes.stream().anyMatch(operacao -> operacao.getId() == operacaoAtivo.getIdOperacao());

            }).collect(Collectors.toList());
        log.info("[PERF] verificarVendaParcial (loop): {}ms ({} ativos restantes)", System.currentTimeMillis() - t0, operacoesAtivosCustodiados.size());

        t0 = System.currentTimeMillis();
        setValoresAtivos(operacoesAtivosCustodiados, precosTesouro);
        log.info("[PERF] setValoresAtivos: {}ms", System.currentTimeMillis() - t0);

        log.info("[PERF] === listarCarteiraRendaFixa TOTAL: {}ms ===", System.currentTimeMillis() - inicio);
        return operacoesAtivosCustodiados;

    }

    private void setValoresAtivos(List<AtivoCarteiraRFDTO> carteira, List<PuTesouroDireto> precosTesouro) {

        // Batch load all data upfront to eliminate N+1 queries
        List<Integer> ativoIds = carteira.stream()
                .map(AtivoCarteiraRFDTO::getIdAtivo)
                .distinct()
                .collect(Collectors.toList());

        long t0 = System.currentTimeMillis();
        Map<Integer, Double> proventosMap = eventoService.getTotalProventosRendaFixaBatch(ativoIds);
        log.info("[PERF]   getTotalProventosRendaFixaBatch: {}ms ({} ativos)", System.currentTimeMillis() - t0, ativoIds.size());

        t0 = System.currentTimeMillis();
        Map<Integer, AtivoCarteira> ativoCarteiraMap = ativoCarteiraRepository.findByAtivoIdIn(ativoIds, SecurityUtils.getCurrentUserId())
                .stream()
                .collect(Collectors.toMap(ac -> ac.getAtivo().getId(), ac -> ac, (a, b) -> a));
        log.info("[PERF]   findByAtivoIdIn: {}ms", System.currentTimeMillis() - t0);

        Set<Integer> processedAtivoIds = new HashSet<>();

        long tRentabilidade = System.currentTimeMillis();
        carteira.forEach(
                operacaoRendaFixaDto -> {
                    operacaoRendaFixaDto.setCusto(operacaoRendaFixaDto.getCustodia() * operacaoRendaFixaDto.getPrecoMedio());
                    operacaoRendaFixaDto.setTotalEmProventos(proventosMap.getOrDefault(operacaoRendaFixaDto.getIdAtivo(), 0.0));

                    if (operacaoRendaFixaDto.getIdSubclasseAtivo() == SubclasseAtivoEnum.TESOURO_DIRETO.getId()) {
                        setValoresTesouroDireto(precosTesouro, operacaoRendaFixaDto, ativoCarteiraMap);

                    } else if (operacaoRendaFixaDto.getIdSubclasseAtivo() == SubclasseAtivoEnum.CREDITO_BANCARIO.getId()) {
                        long tCalc = System.currentTimeMillis();
                        operacaoRendaFixaDto.setValorMercado(calcularRentabilidade.calcularRentabilidade(operacaoRendaFixaDto));
                        log.info("[PERF]     calcularRentabilidade (mercado) [{}]: {}ms", operacaoRendaFixaDto.getSiglaAtivo(), System.currentTimeMillis() - tCalc);

                    } else if(operacaoRendaFixaDto.getIdSubclasseAtivo() == SubclasseAtivoEnum.CREDITO_PRIVADO.getId() ||
                             operacaoRendaFixaDto.getIdSubclasseAtivo() == SubclasseAtivoEnum.PREVIDENCIA.getId())  {

                        if(processedAtivoIds.contains(operacaoRendaFixaDto.getIdAtivo())) return;
                        processedAtivoIds.add(operacaoRendaFixaDto.getIdAtivo());

                        AtivoCarteira ac = ativoCarteiraMap.get(operacaoRendaFixaDto.getIdAtivo());
                        operacaoRendaFixaDto.setValorMercado(ac != null ? ac.getValorMercado() : 0.0);
                    }
                    long tCalc2 = System.currentTimeMillis();
                    operacaoRendaFixaDto.setValorContratado(calcularRentabilidade.calcularRentabilidade(operacaoRendaFixaDto));
                    log.info("[PERF]     calcularRentabilidade (contratado) [{}]: {}ms", operacaoRendaFixaDto.getSiglaAtivo(), System.currentTimeMillis() - tCalc2);
                }
        );
        log.info("[PERF]   loop calcularRentabilidade total: {}ms ({} ativos)", System.currentTimeMillis() - tRentabilidade, carteira.size());
    }

    private void setValoresTesouroDireto(List<PuTesouroDireto> precosTesouro, AtivoCarteiraRFDTO operacaoRendaFixaDto, Map<Integer, AtivoCarteira> ativoCarteiraMap) {
        if(!precosTesouro.isEmpty()) {
            calcularValorDeMercadoTesouroDireto(operacaoRendaFixaDto, precosTesouro);
        } else {
            AtivoCarteira ac = ativoCarteiraMap.get(operacaoRendaFixaDto.getIdAtivo());
            if (ac != null) {
                double custodiaTotal = ac.getCustodia();
                double percentualCustodiaAtivo = (operacaoRendaFixaDto.getCustodia() / custodiaTotal);
                operacaoRendaFixaDto.setValorMercado(ac.getValorMercado() * percentualCustodiaAtivo);
            } else {
                operacaoRendaFixaDto.setValorMercado(0.0);
            }
        }
    }

    public List<OperacaoRendaFixa> verificarVendaParcial(AtivoCarteiraRFDTO ativoCarteiraRFDTO) {

        List<OperacaoRendaFixa> operacoesAtivoVendaParcial = operacaoRendaFixaRepository.listarOperacoesAtivoVendaParcial(ativoCarteiraRFDTO.getIdAtivo(), SecurityUtils.getCurrentUserId());

        double totalListado = operacoesAtivoVendaParcial.stream()
                .filter(op -> op.getTipoOperacao().getId() != 2)
                .mapToDouble(OperacaoRendaFixa::getQuantidadeNegociada)
                .sum();
        double custodia = operacaoRendaFixaRepository.getCustodiaByIdAtivo(ativoCarteiraRFDTO.getIdAtivo(), SecurityUtils.getCurrentUserId());

        Iterator<OperacaoRendaFixa> iterator = operacoesAtivoVendaParcial.iterator();
        while (custodia < totalListado && iterator.hasNext()) {
            OperacaoRendaFixa operacao = iterator.next();
            totalListado -= operacao.getQuantidadeNegociada();
            iterator.remove();
        }
        return operacoesAtivoVendaParcial;

    }

    private void calcularValorDeMercadoTesouroDireto(AtivoCarteiraRFDTO ativoCarteiraRFDTO, List<PuTesouroDireto> titulos) {
        Optional<PuTesouroDireto> titulo = titulos.stream()
                .filter(val -> val.getTipoTitulo().equals(ativoCarteiraRFDTO.getNomeAtivo()))
                .findFirst();
        titulo.ifPresent(tituloTesouroDTO -> {
            ativoCarteiraRFDTO.setCotacao(tituloTesouroDTO.getPuBaseManha());
            var valorBruto = tituloTesouroDTO.getPuBaseManha() * ativoCarteiraRFDTO.getCustodia();
            var valorLiquido = this.calculaImpostoService.getValorLiquidoDeImposto(ativoCarteiraRFDTO, valorBruto);
            ativoCarteiraRFDTO.setValorMercado(valorLiquido);

        });
    }

    public List<AtivoCarteiraDTO> listarCarteiraDeFiis() {
        var carteira = this.operacaoRendaVariavelRepository.listarCarteiraDeFiis(SecurityUtils.getCurrentUserId());
        carteira
                .forEach(ativoCarteira -> {
                            ativoCarteira.setTotalEmProventos(this.eventoService.getTotalProventosPorAtivo(ativoCarteira.getAtivo().getId()));
                            ativoCarteira.setGanhoDeCapital(this.calcularGanhoDeCapital(ativoCarteira.getAtivo().getId()));
                        }
                );

        return carteira;
    }

    public List<PosicaoEncerradaDTO> listarPosicoesEncerradas() {
        return this.operacaoRendaVariavelRepository.listarAtivosComOperacoesFechadas(SecurityUtils.getCurrentUserId());
    }

    private double calcularGanhoDeCapital(int idAtivo) {
        var operacoes = operacaoRendaVariavelRepository.listarOperacoesPorAtivo(idAtivo, SecurityUtils.getCurrentUserId());
        var operacoesDto = OperacaoRendaVariavelMapper.INSTANCE.toListDTO(operacoes);
        return operacoesDto.stream()
                .mapToDouble(op -> op.getValorTotal() - op.getCustoTotal())
                .sum();
    }

    public List<Integer> listarAnosComOperacoes() {
        return this.operacaoRendaVariavelRepository.listarAnosComOperacoes(SecurityUtils.getCurrentUserId());
    }

    public List<Integer> listarAnosComOperacoes(Optional<Boolean> rendaFixa) {
        return this.operacaoRendaFixaRepository.listarAnosComOperacoes(SecurityUtils.getCurrentUserId());
    }


    public List<MesDTO> listarMesesComOperacoes(@PathVariable(value = "ano") Integer ano) {
        return this.operacaoRendaVariavelRepository.listarMesesComOperacoesOuEventosPorAno(ano, SecurityUtils.getCurrentUserId());
    }

    public List<MesDTO> listarMesesComOperacoesRF(@PathVariable(value = "ano") Integer ano) {
        return this.operacaoRendaFixaRepository.listarMesesComOperacoesPorAno(ano, SecurityUtils.getCurrentUserId());
    }

    public List<OperacaoRendaVariavelDTO> listarOperacoesPorAtivo(int idAtivo) {
        var operacoes = this.operacaoRendaVariavelRepository.listarOperacoesPorAtivo(idAtivo, SecurityUtils.getCurrentUserId());
        return operacoes.stream().map(OperacaoRendaVariavelMapper.INSTANCE::toDTO).collect(Collectors.toList());

    }


}
