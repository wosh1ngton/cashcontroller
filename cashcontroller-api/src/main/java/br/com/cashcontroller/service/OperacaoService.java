package br.com.cashcontroller.service;

import br.com.cashcontroller.dto.*;
import br.com.cashcontroller.dto.enums.TipoOperacaoEnum;
import br.com.cashcontroller.entity.*;
import br.com.cashcontroller.external.dto.FeriadoDTO;
import br.com.cashcontroller.external.dto.tesouro.ResponseDTO;
import br.com.cashcontroller.external.dto.tesouro.TesouroDiretoDTO;
import br.com.cashcontroller.external.dto.tesouro.TituloTesouroDTO;
import br.com.cashcontroller.external.dto.tesouro.TrsrBdTradgDTO;
import br.com.cashcontroller.external.service.FeriadosService;
import br.com.cashcontroller.external.service.TesouroService;
import br.com.cashcontroller.mapper.OperacaoRendaFixaMapper;
import br.com.cashcontroller.mapper.OperacaoRendaVariavelMapper;
import br.com.cashcontroller.mapper.TipoOperacaoMapper;
import br.com.cashcontroller.repository.*;
import br.com.cashcontroller.service.util.CalculaImpostoService;
import br.com.cashcontroller.service.util.CalcularRentabilidade;
import br.com.cashcontroller.utils.Taxa;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Internal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.TextStyle;
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
        List<OperacaoRendaVariavel> operacoes = operacaoRendaVariavelRepository.findAll();
        return OperacaoRendaVariavelMapper.INSTANCE.toListDTO(operacoes);
    }

    public List<OperacaoRendaVariavelDTO> listarOperacoesRendaVariavelPorData(Filter filter) {
        List<OperacaoRendaVariavel> operacoes = operacaoRendaVariavelRepository.findOperacoesByData(filter.getStartDate(), filter.getEndDate(), filter.getSubclasse(), filter.getAno(), filter.getMes(), filter.getAtivo());
        return OperacaoRendaVariavelMapper.INSTANCE.toListDTO(operacoes);
    }

    public List<OperacaoRendaFixaDTO> listarOperacoesRendaFixaPorData(Filter filter) {

        List<OperacaoRendaFixa> operacoes = operacaoRendaFixaRepository.findOperacoesByData(filter.getStartDate(), filter.getEndDate(), filter.getSubclasse(), filter.getAno(), filter.getMes());
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
            double pmAtivo = this.operacaoRendaVariavelRepository.calcularPrecoMedio(operacaoDto.getAtivoDto(), operacaoDto.getDataOperacao(), startDate).getPrecoMedio();
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

    public List<ItemLabelDTO> listarAtivosOperados() {
        return this.operacaoRendaVariavelRepository.findDistinctAtivo();
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
        updateOrCreateAtivoCarteira(operacaoRendaVariavelSaveDTO.getAtivoDto(), operacaoRendaVariavelSaveDTO.getQuantidadeNegociada(), operacaoRendaVariavelSaveDTO.getCustoTotal(), operacaoRendaVariavelSaveDTO.getTipoOperacaoDto());

        OperacaoRendaVariavel operacao = OperacaoRendaVariavelMapper.INSTANCE.toSaveEntity(operacaoRendaVariavelSaveDTO);
        return OperacaoRendaVariavelMapper.INSTANCE.toDTO(operacaoRendaVariavelRepository.save(operacao));
    }


    private int desdobrarAtivo(OperacaoRendaVariavelSaveDTO operacaoRendaVariavelSaveDTO) {
        int fatorDeProporcao = operacaoRendaVariavelSaveDTO.getQuantidadeNegociada();
        int totalAtualDeAcoes = this.operacaoRendaVariavelRepository.getCustodiaPorAtivo(operacaoRendaVariavelSaveDTO.getAtivoDto());
        int totalDoAumento = (fatorDeProporcao * totalAtualDeAcoes) - totalAtualDeAcoes;
        return totalDoAumento;
    }

    private int agruparAtivo(OperacaoRendaVariavelSaveDTO operacaoRendaVariavelSaveDTO) {
        int fatorDeProporcao = operacaoRendaVariavelSaveDTO.getQuantidadeNegociada();
        int totalAtualDeAcoes = this.operacaoRendaVariavelRepository.getCustodiaPorAtivo(operacaoRendaVariavelSaveDTO.getAtivoDto());
        int totalDecrescimo = totalAtualDeAcoes - (totalAtualDeAcoes / fatorDeProporcao);
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

        if (!posicaoLiquidada(operacao.getAtivo().getId())) {
            updateOrCreateAtivoCarteira(operacao.getAtivo().getId(), quantidadeDiff, custoDiff, operacaoRendaVariavelDTO.getTipoOperacaoDto().getId());
        }
        var operacaoDto = OperacaoRendaVariavelMapper.INSTANCE.toDTO(operacaoRendaVariavelRepository.save(operacao));
        return operacaoDto;
    }

    private boolean posicaoLiquidada(int idAtivo) {
        int custodia = operacaoRendaVariavelRepository.getCustodiaPorAtivo(idAtivo);
        return custodia == 0;
    }

    private void updateOrCreateAtivoCarteira(int ativo, double quantidadeDiff, double custoDiff, int tipoOperacao) {
        Optional<AtivoCarteira> ativoCarteira = ativoCarteiraRepository.findByIdAtivo(ativo);

        if (!ativoCarteira.isPresent()) {
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
            var operacoes = this.operacaoRendaVariavelRepository.listarOperacoesPorAtivo(operacaoRendaFixaDto.getAtivoDto().getId());
            if(!operacoes.isEmpty()) {
                pmAtivo = this.operacaoRendaFixaRepository.calcularPrecoMedio(operacaoRendaFixaDto.getAtivoDto().getId(),
                        operacaoRendaFixaDto.getDataOperacao(),
                        startDate).getPrecoMedio();
            }
            return pmAtivo * operacaoRendaFixaDto.getQuantidadeNegociada();

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
            updateOrCreateAtivoCarteira(value.getAtivo().getId(), value.getQuantidadeNegociada(), value.getCustoTotal(), 2);
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

        List<AtivoCarteiraRFDTO> carteira = this.operacaoRendaFixaRepository.listarCarteiraRendaFixa();
        List<PuTesouroDireto> precosTesouro = puTesouroDiretoRepository.listarValoresMaisRecentesPorTitulo();

//        TesouroDiretoDTO response = this.tesouroService.getTitulos();
//        List<TituloTesouroDTO> titulosTesouro = Optional.ofNullable(response.getResponse())
//                .map(ResponseDTO::getTrsrBdTradgList)
//                .orElse(Collections.emptyList())
//                .stream()
//                .map(TrsrBdTradgDTO::getTrsrBd)
//                .toList();


            carteira = carteira.stream().filter(ativoCarteira -> {
                List<OperacaoRendaFixa> operacoes = verificarVendaParcial(ativoCarteira);
                return operacoes.isEmpty() || operacoes.stream().anyMatch(operacao -> operacao.getId() == ativoCarteira.getIdOperacao());

            }).collect(Collectors.toList());

           // List<TituloTesouroDTO> finalTitulosTesouro = titulosTesouro;
            carteira.forEach(
                    operacaoRendaFixaDto -> {
                        operacaoRendaFixaDto.setCusto(operacaoRendaFixaDto.getCustodia() * operacaoRendaFixaDto.getPrecoMedio());
                        operacaoRendaFixaDto.setTotalEmProventos(eventoService.getTotalProventosPorAtivoRendaFixa(operacaoRendaFixaDto.getIdAtivo()));
                        if (operacaoRendaFixaDto.getIdSubclasseAtivo() == 3) {
                            if(!precosTesouro.isEmpty()) {
                                calcularValorDeMercadoTesouroDireto(operacaoRendaFixaDto, precosTesouro);
                                operacaoRendaFixaDto.setValorMercado(this.calculaImpostoService.getValorLiquidoDeImposto(operacaoRendaFixaDto, operacaoRendaFixaDto.getValorMercado()));
                            } else {
                                OptionalDouble optionalValorMercado = ativoCarteiraRepository.findByIdAtivo(operacaoRendaFixaDto.getIdAtivo())
                                        .stream()
                                        .mapToDouble(ativoCarteira -> {
                                            double custodiaTotal = ativoCarteira.getCustodia();
                                            double percentualCustodiaAtivo = (operacaoRendaFixaDto.getCustodia() / custodiaTotal);
                                            return ativoCarteira.getValorMercado() * percentualCustodiaAtivo;
                                        })
                                        .findFirst();

                                operacaoRendaFixaDto.setValorMercado(optionalValorMercado.orElse(0.0));
                            }
                        } else if (operacaoRendaFixaDto.getIdSubclasseAtivo() == 5) {
                            OptionalDouble optionalValorMercado = ativoCarteiraRepository.findByIdAtivo(operacaoRendaFixaDto.getIdAtivo())
                                    .stream()
                                    .mapToDouble(AtivoCarteira::getValorMercado)
                                    .findFirst();

                            operacaoRendaFixaDto.setValorMercado(optionalValorMercado.orElse(0.0));
                        } else {
                            operacaoRendaFixaDto.setValorMercado(calcularRentabilidade.calcularRentabilidade(operacaoRendaFixaDto));
                        }
                        operacaoRendaFixaDto.setValorContratado(calcularRentabilidade.calcularRentabilidade(operacaoRendaFixaDto));
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

    private static void calcularValorDeMercadoTesouroDireto(AtivoCarteiraRFDTO ativoCarteiraRFDTO, List<PuTesouroDireto> titulos) {
        Optional<PuTesouroDireto> titulo = titulos.stream()
                .filter(val -> val.getTipoTitulo().equals(ativoCarteiraRFDTO.getNomeAtivo()))
                .findFirst();
        titulo.ifPresent(tituloTesouroDTO -> {
            ativoCarteiraRFDTO.setCotacao(tituloTesouroDTO.getPuBaseManha());
            ativoCarteiraRFDTO.setValorMercado(tituloTesouroDTO.getPuBaseManha() * ativoCarteiraRFDTO.getCustodia());

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


}
