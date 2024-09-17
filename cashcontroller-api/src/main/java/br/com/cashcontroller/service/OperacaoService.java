package br.com.cashcontroller.service;

import br.com.cashcontroller.dto.*;
import br.com.cashcontroller.entity.*;
import br.com.cashcontroller.external.dto.FeriadoDTO;
import br.com.cashcontroller.external.dto.tesouro.TesouroDiretoDTO;
import br.com.cashcontroller.external.dto.tesouro.TituloTesouroDTO;
import br.com.cashcontroller.external.dto.tesouro.TrsrBdTradgDTO;
import br.com.cashcontroller.external.service.FeriadosService;
import br.com.cashcontroller.external.service.TesouroService;
import br.com.cashcontroller.mapper.OperacaoRendaFixaMapper;
import br.com.cashcontroller.mapper.OperacaoRendaVariavelMapper;
import br.com.cashcontroller.mapper.TipoOperacaoMapper;
import br.com.cashcontroller.repository.*;
import br.com.cashcontroller.utils.Taxa;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.TextStyle;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class OperacaoService {

    @Autowired
    IpcaMesRepository ipcaMesRepository;

    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    CalculaImpostoService calculaImpostoService;

    @Autowired
    SelicMesRepository selicMesRepository;
    @Autowired
    OperacaoRendaVariavelRepository operacaoRendaVariavelRepository;
    @Autowired
    OperacaoRendaFixaRepository operacaoRendaFixaRepository;

    @Autowired
    TipoOperacaoRepository tipoOperacaoRepository;

    @Autowired
    EventoService eventoService;

    @Autowired
    CalcularRentabilidadeService calcularRentabilidadeService;

    @Autowired
    TesouroService tesouroService;

    @Autowired
    AtivoCarteiraRepository ativoCarteiraRepository;

    @Autowired
    AtivoRepository ativoRepository;
    private final FeriadosService feriadosService;

    private List<FeriadoDTO> listaFeriados;


    @Autowired
    public OperacaoService(FeriadosService feriadosService) {
        this.feriadosService = feriadosService;

        //  listaFeriados = feriadosService.getFeriadosPorIntervalo(LocalDate.now());
    }

    public List<OperacaoRendaVariavelDTO> listarOperacoesRendaVariavel() {
        List<OperacaoRendaVariavel> operacoes = operacaoRendaVariavelRepository.findAll();
        return OperacaoRendaVariavelMapper.INSTANCE.toListDTO(operacoes);
    }

    public List<OperacaoRendaVariavelDTO> listarOperacoesRendaVariavelPorData(Filter filter) {
        List<OperacaoRendaVariavel> operacoes = operacaoRendaVariavelRepository.findOperacoesByData(filter.getStartDate(), filter.getEndDate(), filter.getSubclasse(), filter.getAno(), filter.getMes());
        return OperacaoRendaVariavelMapper.INSTANCE.toListDTO(operacoes);
    }

    public List<OperacaoRendaFixaDTO> listarOperacoesRendaFixaPorData(Filter filter) {

        List<OperacaoRendaFixa> operacoes = operacaoRendaFixaRepository.findOperacoesByData(filter.getStartDate(), filter.getEndDate(), filter.getSubclasse(), filter.getAno(), filter.getMes());
        return OperacaoRendaFixaMapper.INSTANCE.toListDTO(operacoes);
    }


    public double valorTotalOperacao(
            OperacaoRendaVariavelSaveDTO operacaoDto) {

        double valorBase = operacaoDto.getValorUnitario() * operacaoDto.getQuantidadeNegociada();
        return calcularTaxas(operacaoDto, valorBase);
    }

    public double custoTotalOperacao(OperacaoRendaVariavelSaveDTO operacaoDto) {

        double valorBase = 0.0;
        if (operacaoDto.getTipoOperacaoDto() == 2) {
            double pmAtivo;
            LocalDate startDate = LocalDate.of(2015, 1, 1);
            pmAtivo = this.operacaoRendaVariavelRepository.calcularPrecoMedio(operacaoDto.getAtivoDto(), operacaoDto.getDataOperacao(), startDate).getPrecoMedio();
            valorBase = pmAtivo * operacaoDto.getQuantidadeNegociada();
        } else {
            valorBase = operacaoDto.getValorUnitario() * operacaoDto.getQuantidadeNegociada();
        }

        return calcularTaxas(operacaoDto, valorBase);
    }

    private static double calcularTaxas(OperacaoRendaVariavelSaveDTO operacaoDto, double valorBase) {
        double taxas = Taxa.TAXA_LIQUIDACAO_B3 + Taxa.TAXA_EMOLUMENTOS;
        double impostos = (operacaoDto.getValorCorretagem() / Taxa.IMPOSTOS) - operacaoDto.getValorCorretagem();
        double taxaCorretora = operacaoDto.getValorCorretagem() * Taxa.TAXA_OPERACIONAL_XP;
        double custos = impostos + (valorBase * (taxas / 100));
        return valorBase + custos + operacaoDto.getValorCorretagem() + taxaCorretora;
    }

    public List<OperacaoRendaFixaDTO> listarOperacoesRendaFixa() {
        List<OperacaoRendaFixa> operacoes = operacaoRendaFixaRepository.findAll();
        return OperacaoRendaFixaMapper.INSTANCE.toListDTO(operacoes);
    }

    public List<TipoOperacaoDTO> listarTipoOperacao() {
        List<TipoOperacao> tiposOperacao = tipoOperacaoRepository.findAll();
        return TipoOperacaoMapper.INSTANCE.toListDTO(tiposOperacao);
    }

    public OperacaoRendaVariavelDTO cadastrarOperacaoRendaVariavel(OperacaoRendaVariavelSaveDTO operacaoRendaVariavelSaveDTO) {

        if (operacaoRendaVariavelSaveDTO.getTipoOperacaoDto() == 3) {
            operacaoRendaVariavelSaveDTO.setQuantidadeNegociada(desdobrarAtivo(operacaoRendaVariavelSaveDTO));
        }

        if (operacaoRendaVariavelSaveDTO.getTipoOperacaoDto() == 5) {
            operacaoRendaVariavelSaveDTO.setQuantidadeNegociada(agruparAtivo(operacaoRendaVariavelSaveDTO));
        }

        operacaoRendaVariavelSaveDTO.setCustoTotal(custoTotalOperacao(operacaoRendaVariavelSaveDTO));
        operacaoRendaVariavelSaveDTO.setValorTotal(valorTotalOperacao(operacaoRendaVariavelSaveDTO));


        updateOrCreateAtivoCarteira(operacaoRendaVariavelSaveDTO.getAtivoDto(), operacaoRendaVariavelSaveDTO.getQuantidadeNegociada(), operacaoRendaVariavelSaveDTO.getCustoTotal(), operacaoRendaVariavelSaveDTO.getTipoOperacaoDto());

        OperacaoRendaVariavel operacao = OperacaoRendaVariavelMapper.INSTANCE.toSaveEntity(operacaoRendaVariavelSaveDTO);
        return OperacaoRendaVariavelMapper.INSTANCE.toDTO(operacaoRendaVariavelRepository.save(operacao));
    }


    private Long desdobrarAtivo(OperacaoRendaVariavelSaveDTO operacaoRendaVariavelSaveDTO) {
        Long fatorDeProporcao = (long) operacaoRendaVariavelSaveDTO.getQuantidadeNegociada();
        Long totalAtualDeAcoes = this.operacaoRendaVariavelRepository.getCustodiaPorAtivo(operacaoRendaVariavelSaveDTO.getAtivoDto());
        Long totalDoAumento = (fatorDeProporcao * totalAtualDeAcoes) - totalAtualDeAcoes;
        return totalDoAumento;
    }

    private Long agruparAtivo(OperacaoRendaVariavelSaveDTO operacaoRendaVariavelSaveDTO) {
        Long fatorDeProporcao = (long) operacaoRendaVariavelSaveDTO.getQuantidadeNegociada();
        Long totalAtualDeAcoes = this.operacaoRendaVariavelRepository.getCustodiaPorAtivo(operacaoRendaVariavelSaveDTO.getAtivoDto());
        Long totalDecrescimo = totalAtualDeAcoes - (totalAtualDeAcoes / fatorDeProporcao);
        return totalDecrescimo;
    }


    public OperacaoRendaVariavelDTO atualizarOperacaoRendaVariavel(OperacaoRendaVariavelDTO operacaoRendaVariavelDTO) {

        var saveDto = OperacaoRendaVariavelMapper.INSTANCE.FromDTOtoSaveDTO(operacaoRendaVariavelDTO);
        operacaoRendaVariavelDTO.setCustoTotal(custoTotalOperacao(saveDto));
        operacaoRendaVariavelDTO.setValorTotal(valorTotalOperacao(saveDto));

        var operacao = this.operacaoRendaVariavelRepository.findById(operacaoRendaVariavelDTO.getId())
                .orElseThrow(() -> new NullPointerException("registro.nao.encontrado"));


            double quantidadeDiff = operacaoRendaVariavelDTO.getQuantidadeNegociada() - operacao.getQuantidadeNegociada();
            double custoDiff = operacaoRendaVariavelDTO.getCustoTotal() - operacao.getCustoTotal();
            operacao = OperacaoRendaVariavelMapper.INSTANCE.toEntity(operacaoRendaVariavelDTO);

        if(!posicaoLiquidada(operacao.getAtivo().getId())) {
            updateOrCreateAtivoCarteira(operacao.getAtivo().getId(), quantidadeDiff, custoDiff, operacaoRendaVariavelDTO.getTipoOperacaoDto().getId());
        }
        var operacaoDto = OperacaoRendaVariavelMapper.INSTANCE.toDTO(operacaoRendaVariavelRepository.save(operacao));
        return operacaoDto;
    }


    private boolean posicaoLiquidada(int idAtivo) {
        Long custodia = operacaoRendaVariavelRepository.getCustodiaPorAtivo(idAtivo);
        return custodia == 0;
    }
    private void updateOrCreateAtivoCarteira(int ativo, double quantidadeDiff, double custoDiff, int tipoOperacao) {
        Optional<AtivoCarteira> ativoCarteira = ativoCarteiraRepository.findByIdAtivo(ativo);

        if(!ativoCarteira.isPresent()) {
            AtivoCarteira novoAtivoCarteira = new AtivoCarteira();
            novoAtivoCarteira.setAtivo(ativoRepository.findById(ativo).get());
            novoAtivoCarteira.setCustodia(quantidadeDiff);
            novoAtivoCarteira.setCusto(custoDiff);
            ativoCarteiraRepository.save(novoAtivoCarteira);
        } else {

            if (tipoOperacao == 1) {
                ativoCarteira.get().setCustodia(ativoCarteira.get().getCustodia() + quantidadeDiff);
                ativoCarteira.get().setCusto(ativoCarteira.get().getCusto() + custoDiff);
            } else if (tipoOperacao == 2) {
                ativoCarteira.get().setCustodia(ativoCarteira.get().getCustodia() - quantidadeDiff);
                ativoCarteira.get().setCusto(ativoCarteira.get().getCusto() - custoDiff);
            }
            ativoCarteiraRepository.save(ativoCarteira.get());
        }

    }

    public OperacaoRendaFixaDTO cadastrarOperacaoRendaFixa(OperacaoRendaFixaDTO operacaoRendaFixaDto) {

        operacaoRendaFixaDto.setCustoTotal(calcularCustoOperacaoRendaFixa(operacaoRendaFixaDto));
        operacaoRendaFixaDto.setValorTotal(cadastrarValorVendaOperacaoRendaFixa(operacaoRendaFixaDto));
        OperacaoRendaFixa operacao = OperacaoRendaFixaMapper.INSTANCE.toEntity(operacaoRendaFixaDto);
        return OperacaoRendaFixaMapper.INSTANCE.toDTO(operacaoRendaFixaRepository.save(operacao));
    }


    private Double cadastrarValorVendaOperacaoRendaFixa(OperacaoRendaFixaDTO operacaoRendaFixaDto) {
        var valorTotal = 0.0;
        if (operacaoRendaFixaDto.getTipoOperacaoDto().getId() == 2) {
            valorTotal = operacaoRendaFixaDto.getQuantidadeNegociada() * operacaoRendaFixaDto.getValorUnitario()
                    - operacaoRendaFixaDto.getValorCorretagem();
            return valorTotal;
        }
        return valorTotal;
    }

    private Double calcularCustoOperacaoRendaFixa(OperacaoRendaFixaDTO operacaoRendaFixaDto) {
        var custoTotal = 0.0;
        if (operacaoRendaFixaDto.getTipoOperacaoDto().getId() == 1) {
            custoTotal = operacaoRendaFixaDto.getQuantidadeNegociada() * operacaoRendaFixaDto.getValorUnitario()
                    + operacaoRendaFixaDto.getValorCorretagem();
            return custoTotal;
        } else if (operacaoRendaFixaDto.getTipoOperacaoDto().getId() == 2) {
            double valorBase = 0.0;

            double pmAtivo;
            LocalDate startDate = LocalDate.of(2015, 1, 1);
            pmAtivo = this.operacaoRendaFixaRepository.calcularPrecoMedio(operacaoRendaFixaDto.getAtivoDto().getId(),
                    operacaoRendaFixaDto.getDataOperacao(),
                    startDate).getPrecoMedio();
            valorBase = pmAtivo * operacaoRendaFixaDto.getQuantidadeNegociada();

            return valorBase;
        }
        return custoTotal;
    }

    @Transactional
    public OperacaoRendaFixaDTO atualizarOperacaoRendaFixa(OperacaoRendaFixaDTO operacaoRendaFixaDTO) {

        Optional<OperacaoRendaFixa> operacaoOptional = this.operacaoRendaFixaRepository.findById(operacaoRendaFixaDTO.getId());
        if (operacaoOptional.isPresent()) {
            OperacaoRendaFixa operacaoUpdated;
            operacaoUpdated = OperacaoRendaFixaMapper.INSTANCE.toEntity(operacaoRendaFixaDTO);
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

        Optional<OperacaoRendaVariavel> operacao = operacaoRendaVariavelRepository.findById(id);
        operacao.ifPresent(value -> {
            updateOrCreateAtivoCarteira(value.getAtivo().getId(),value.getQuantidadeNegociada(),value.getCustoTotal(),2);
            this.operacaoRendaVariavelRepository.delete(value);
        });

    }

    public void excluirOperacaoRendaFixa(int id) {

        Optional<OperacaoRendaFixa> operacao = operacaoRendaFixaRepository.findById(id);
        operacao.ifPresent(value -> this.operacaoRendaFixaRepository.delete(value));

    }


    public OperacaoRendaVariavelDTO findById(Integer id) {
        return OperacaoRendaVariavelMapper.INSTANCE.toDTO(operacaoRendaVariavelRepository.findById(id).get());
    }

    public IrpfMesDTO calcularImpostoMensal(Filter filter) {

        var vendasDoMes = listarVendasDoMes(filter);
        oterResultadoOperacao(vendasDoMes);
        double resultadoMes = vendasDoMes.stream().mapToDouble(op -> op.getResultadoOperacao()).sum();

        IrpfMesDTO irMes = new IrpfMesDTO();

        String monthName = filter.getEndDate().getMonth().getDisplayName(TextStyle.FULL, new Locale("pt"));
        irMes.setMes(monthName);
        var valorTotalEmVendas = vendasDoMes.stream().mapToDouble(op -> op.getValorTotal()).sum();
        irMes.setAtivosVendidos(vendasDoMes);
        irMes.setResultadoMes(resultadoMes);
        irMes.setTotalVendido(valorTotalEmVendas);
        if (valorTotalEmVendas > Taxa.LIMITE_IR && resultadoMes > 0) {
            irMes.setImposto(true);
            irMes.setValorAPagar(resultadoMes * Taxa.ALIQUOTA_IR);
        }
        return irMes;
    }


    private void oterResultadoOperacao(List<OperacaoRendaVariavelDTO> vendasDoMes) {
        for (OperacaoRendaVariavelDTO op : vendasDoMes) {
            double resultadoOperacao = op.getValorTotal() - op.getCustoTotal();
            op.setResultadoOperacao(resultadoOperacao);
        }
    }


    private AtivoDTO calcularPrecoMedio(int idAtivo, LocalDate dataDeCorte) {
        LocalDate startDate = LocalDate.of(2015, 1, 1);
        return this.operacaoRendaVariavelRepository.calcularPrecoMedio(idAtivo, dataDeCorte, startDate);

    }


    private List<OperacaoRendaVariavelDTO> listarVendasDoMes(Filter filter) {
        var mes = filter.getEndDate().getMonth();
        var operacoes = listarOperacoesRendaVariavelPorData(filter);

        var operacoesDeVenda = operacoes.stream().filter(op -> op.getTipoOperacaoDto().getNome().contains("Venda") && op.getDataOperacao().getMonth() == mes).collect(Collectors.toList());
        return operacoesDeVenda;
    }

    public List<AtivoCarteiraDTO> listarCarteiraDeAcoes() {
        var carteira = this.operacaoRendaVariavelRepository.listarCarteiraDeAcoes();
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

        TesouroDiretoDTO response = this.tesouroService.getTitulos();
        var titulosTesouro = response.getResponse().getTrsrBdTradgList().stream().map(TrsrBdTradgDTO::getTrsrBd).collect(Collectors.toList());

        List<AtivoCarteiraRFDTO> carteira = this.operacaoRendaFixaRepository.listarCarteiraRendaFixa();

        carteira = carteira.stream().filter(ativoCarteira -> {
            List<OperacaoRendaFixa> operacoes = verificarVendaParcial(ativoCarteira);
            return operacoes.isEmpty() || operacoes.stream().anyMatch(operacao -> operacao.getId() == ativoCarteira.getIdOperacao());

        }).collect(Collectors.toList());

        carteira.forEach(
                ativoCarteiraRFDTO -> {
                    ativoCarteiraRFDTO.setCusto(ativoCarteiraRFDTO.getCustodia() * ativoCarteiraRFDTO.getPrecoMedio());
                    ativoCarteiraRFDTO.setTotalEmProventos(eventoService.getTotalProventosPorAtivoRendaFixa(ativoCarteiraRFDTO.getIdAtivo()));
                    if (ativoCarteiraRFDTO.getIdSubclasseAtivo() == 3) {
                        calcularValorDeMercadoTesouroDireto(ativoCarteiraRFDTO, titulosTesouro);
                        ativoCarteiraRFDTO.setValorMercado(this.calculaImpostoService.getValorLiquidoDeImposto(ativoCarteiraRFDTO, ativoCarteiraRFDTO.getValorMercado()));
                    } else {
                        ativoCarteiraRFDTO.setValorMercado(calcularRentabilidadeService.calcularRentabilidade(ativoCarteiraRFDTO));
                    }
                    ativoCarteiraRFDTO.setValorContratado(calcularRentabilidadeService.calcularRentabilidade(ativoCarteiraRFDTO));
                }
        );

        return carteira;
    }

    public List<OperacaoRendaFixa> verificarVendaParcial(AtivoCarteiraRFDTO ativoCarteiraRFDTO) {

        List<OperacaoRendaFixa> operacoesAtivoVendaParcial = operacaoRendaFixaRepository.listarOperacoesAtivoVendaParcial(ativoCarteiraRFDTO.getIdAtivo());

        double totalListado = operacoesAtivoVendaParcial.stream()
                .filter(op -> op.getTipoOperacao().getId() != 2)
                .mapToDouble(OperacaoRendaFixa::getQuantidadeNegociada)
                .sum();
        double custodia = operacaoRendaFixaRepository.getCustodiaByIdAtivo(ativoCarteiraRFDTO.getIdAtivo());

        Iterator<OperacaoRendaFixa> iterator = operacoesAtivoVendaParcial.iterator();
        while (custodia < totalListado && iterator.hasNext()) {
            OperacaoRendaFixa operacao = iterator.next();
            totalListado -= operacao.getQuantidadeNegociada();
            iterator.remove();
        }
        return operacoesAtivoVendaParcial;

    }

    private static void calcularValorDeMercadoTesouroDireto(AtivoCarteiraRFDTO ativoCarteiraRFDTO, List<TituloTesouroDTO> titulos) {
        Optional<TituloTesouroDTO> titulo = titulos.stream()
                .filter(val -> val.getNm().equals(ativoCarteiraRFDTO.getNomeAtivo()))
                .findFirst();
        titulo.ifPresent(tituloTesouroDTO -> {
            ativoCarteiraRFDTO.setCotacao(tituloTesouroDTO.getUntrRedVal());
            ativoCarteiraRFDTO.setValorMercado(tituloTesouroDTO.getUntrRedVal() * ativoCarteiraRFDTO.getCustodia());

        });
    }

    public List<AtivoCarteiraDTO> listarCarteiraDeFiis() {
        var carteira = this.operacaoRendaVariavelRepository.listarCarteiraDeFiis();
        carteira
                .forEach(ativoCarteira -> {
                            ativoCarteira.setTotalEmProventos(this.eventoService.getTotalProventosPorAtivo(ativoCarteira.getAtivo().getId()));
                            ativoCarteira.setGanhoDeCapital(this.calcularGanhoDeCapital(ativoCarteira.getAtivo().getId()));
                        }
                );

        return carteira;
    }

    public List<PosicaoEncerradaDTO> listarPosicoesEncerradas() {
        return this.operacaoRendaVariavelRepository.listarAtivosComOperacoesFechadas();
    }

    private double calcularGanhoDeCapital(int idAtivo) {
        var operacoes = operacaoRendaVariavelRepository.listarOperacoesPorAtivo(idAtivo);
        var operacoesDto = OperacaoRendaVariavelMapper.INSTANCE.toListDTO(operacoes);
        return operacoesDto.stream()
                .mapToDouble(op -> op.getValorTotal() - op.getCustoTotal())
                .sum();
    }

    public List<Integer> listarAnosComOperacoes() {
        return this.operacaoRendaVariavelRepository.listarAnosComOperacoes();
    }

    public List<Integer> listarAnosComOperacoes(Optional<Boolean> rendaFixa) {
        return this.operacaoRendaFixaRepository.listarAnosComOperacoes();
    }


    public List<MesDTO> listarMesesComOperacoes(@PathVariable(value = "ano") Integer ano) {
        return this.operacaoRendaVariavelRepository.listarMesesComOperacoesOuEventosPorAno(ano);
    }

    public List<MesDTO> listarMesesComOperacoesRF(@PathVariable(value = "ano") Integer ano) {
        return this.operacaoRendaFixaRepository.listarMesesComOperacoesPorAno(ano);
    }

    public List<OperacaoRendaVariavelDTO> listarOperacoesPorAtivo(int idAtivo) {
        var operacoes = this.operacaoRendaVariavelRepository.listarOperacoesPorAtivo(idAtivo);
        return operacoes.stream().map(OperacaoRendaVariavelMapper.INSTANCE::toDTO).collect(Collectors.toList());

    }


    public List<IpcaMes> cadastrarIpcaMesEmLote(List<IpcaMes> ipcas) {
        try {
            return ipcaMesRepository.saveAll(ipcas);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<SelicMes> cadastrarSelicMesEmLote(List<SelicMes> selics) {
        try {
            return selicMesRepository.saveAll(selics);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
