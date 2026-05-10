package br.com.cashcontroller.service;

import br.com.cashcontroller.dto.*;
import br.com.cashcontroller.entity.Ativo;
import br.com.cashcontroller.entity.AtivoCarteira;
import br.com.cashcontroller.entity.enums.CategoriaAlocacao;
import br.com.cashcontroller.external.dto.stock.BrapiDTO;
import br.com.cashcontroller.external.dto.stock.StocksDTO;
import br.com.cashcontroller.external.service.RendaVariavelService;
import br.com.cashcontroller.external.service.TesouroService;
import br.com.cashcontroller.mapper.AtivoCarteiraMapper;
import br.com.cashcontroller.repository.AtivoCarteiraRepository;
import br.com.cashcontroller.repository.AtivoRepository;
import br.com.cashcontroller.repository.OperacaoRendaFixaRepository;
import br.com.cashcontroller.security.SecurityUtils;
import br.com.cashcontroller.service.util.CalculaImpostoService;
import br.com.cashcontroller.service.util.CalcularRentabilidade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AtivoCarteiraService {
    @Autowired
    AtivoCarteiraRepository repository;

    @Autowired
    AtivoRepository ativoRepository;

    @Autowired
    CalcularRentabilidade calcularRentabilidade;

    @Autowired
    CalculaImpostoService calculaImpostoService;

    @Autowired
    TesouroService tesouroService;

    @Autowired
    RendaVariavelService rendaVariavelService;

    @Autowired
    OperacaoService operacaoService;

    @Autowired
    OperacaoRendaFixaRepository operacaoRendaFixaRepository;

    @Autowired
    TirService tirService;

    @Autowired
    AlocacaoMetaService alocacaoMetaService;

    public AtivoCarteiraDTO cadastrarAtivoCarteira(AtivoCarteiraDTO ativoCarteiraDTO) {
        var entity = AtivoCarteiraMapper.INSTANCE.toEntity(ativoCarteiraDTO);
        entity.setUser(SecurityUtils.getCurrentUser());
        entity = this.repository.save(entity);
        return AtivoCarteiraMapper.INSTANCE.toDTO(entity);
    }

    public List<AtivoCarteiraDTO> listarCarteiraAcoes() {

        var ativosCarteiraDTO = getCarteira();
        ativosCarteiraDTO = ativosCarteiraDTO.stream().filter(a -> a.getAtivo().getSubclasseAtivo().getId() == 2).toList();
        var stocks = Arrays.stream(this.rendaVariavelService.getStocksBrapi().getStocks()).toList();

        return setValoresFromAPI(ativosCarteiraDTO, stocks);

    }

    public List<AtivoCarteiraDTO> listarAtivosCarteiraFiis() {
        var ativosCarteiraDTO = getCarteira();
        ativosCarteiraDTO = ativosCarteiraDTO.stream().filter(a -> a.getAtivo().getSubclasseAtivo().getId() == 1).toList();
        var fiisComValordeMercado = Arrays.stream(this.rendaVariavelService.getFiisBrapi().getStocks()).toList();
        return setValoresFromAPI(ativosCarteiraDTO, fiisComValordeMercado);
    }

    public List<AtivoCarteiraDTO> getCotacaoMeusFiis() {
        var brapiDTO = rendaVariavelService.getFiisBrapi();
        List<AtivoCarteiraDTO> meusFiis = operacaoService.listarCarteiraDeFiis();
        setCotacoes(brapiDTO, meusFiis);
        tirService.calcularTirParaCarteira(meusFiis);
        return meusFiis;
    }

    public List<AtivoCarteiraDTO> getIVVB11() {
        var brapiDTO = rendaVariavelService.getIVVB11();
        List<AtivoCarteiraDTO> ivvb11 = operacaoService.listarCarteiraDeAcoes().stream()
                .filter(ativo -> ativo.getAtivo().getSigla().equals("IVVB11")).collect(Collectors.toList());
        setCotacoes(brapiDTO, ivvb11);
        return ivvb11;
    }

    public List<AtivoCarteiraDTO> getCotacaoMinhasAcoes() {
        var brapiDTO = rendaVariavelService.getStocksBrapi();

        List<AtivoCarteiraDTO> minhasAcoes = operacaoService.listarCarteiraDeAcoes();
        minhasAcoes.removeIf(ativo -> ativo.getAtivo().getSigla().equals("IVVB11"));
        setCotacoes(brapiDTO, minhasAcoes);
        minhasAcoes.addAll(getIVVB11());
        tirService.calcularTirParaCarteira(minhasAcoes);
        return minhasAcoes;
    }

    private static void setCotacoes(BrapiDTO brapiDTO, List<AtivoCarteiraDTO> meusAtivos) {
        Arrays.stream(brapiDTO.getStocks()).forEach(ativoBrapi -> {
            meusAtivos.forEach(ativo -> {
                if(ativo.getAtivo().getSigla().equals(ativoBrapi.getStock())) {
                    ativo.setCotacao(ativoBrapi.getClose());
                    ativo.setOscilacaoDia(ativoBrapi.getChange());
                    ativo.setValorMercado(ativoBrapi.getClose() * ativo.getCustodia());
                    ativo.setCusto(ativo.getCustodia() * ativo.getPrecoMedio());
                    ativo.setValorizacao(ativoBrapi.getClose() * ativo.getCustodia() - ativo.getCustodia() * ativo.getPrecoMedio());

                }
            });
        });
    }

    private List<AtivoCarteiraDTO> setValoresFromAPI(List<AtivoCarteiraDTO> ativoCarteiraDto, List<StocksDTO> ativosBrapi) {

        ativoCarteiraDto.stream()
                .forEach(ativoCarteira -> {
                    ativosBrapi.stream().filter(a -> a.getStock().equals(ativoCarteira.getAtivo().getSigla())).findFirst()
                            .ifPresent((ativoBrapi) -> {
                                if(ativoBrapi.getClose() != null) {
                                    ativoCarteira.setValorMercado(ativoCarteira.getCustodia() * ativoBrapi.getClose());
                                }
                                ativoCarteira.setPrecoMedio(ativoCarteira.getCusto() / ativoCarteira.getCustodia());
                            });

                });
        return ativoCarteiraDto;
    }


    private List<AtivoCarteiraDTO> getCarteira() {
        var entities = this.repository.findAllByUser(SecurityUtils.getCurrentUserId()).stream()
                .filter(item -> item.getCustodia() > 0).toList();
        var ativosCarteiraDTO =  entities.stream().map(AtivoCarteiraMapper.INSTANCE::toListDTO).collect(Collectors.toList());
        return ativosCarteiraDTO;
    }


    public List<AtivoCarteiraDTO> listarAtivosCarteiraRendaFixa() {
        long inicio = System.currentTimeMillis();

        long t0 = System.currentTimeMillis();
        List<AtivoCarteiraRFDTO> carteiraRendaFixa = operacaoService.listarCarteiraRendaFixa();
        log.info("[PERF] operacaoService.listarCarteiraRendaFixa(): {}ms", System.currentTimeMillis() - t0);

        t0 = System.currentTimeMillis();
        Map<String, AtivoCarteiraRFDTO> groupedBySiglaAtivo = agruparPorAtivo(carteiraRendaFixa);
        log.info("[PERF] agruparPorAtivo: {}ms", System.currentTimeMillis() - t0);

        // Use HashMap for O(1) lookup instead of O(n) stream search per item
        Map<Integer, AtivoCarteiraRFDTO> aggregatedByAtivoId = new HashMap<>();
        groupedBySiglaAtivo.values().forEach(item -> aggregatedByAtivoId.put(item.getIdAtivo(), item));

        t0 = System.currentTimeMillis();
        var ativosCarteiraDTO = getCarteira();
        ativosCarteiraDTO = ativosCarteiraDTO.stream().filter(a -> a.getAtivo().getSubclasseAtivo().getId() > 2).toList();
        log.info("[PERF] getCarteira + filtro: {}ms ({} ativos)", System.currentTimeMillis() - t0, ativosCarteiraDTO.size());

        ativosCarteiraDTO.forEach(ativoCarteiraRFDTO -> {
            AtivoCarteiraRFDTO ativoAtualizado = aggregatedByAtivoId.get(ativoCarteiraRFDTO.getAtivo().getId());
            if (ativoAtualizado != null) {
                ativoCarteiraRFDTO.setCusto(ativoAtualizado.getCusto());
                ativoCarteiraRFDTO.setValorMercado(ativoAtualizado.getValorMercado());
                ativoCarteiraRFDTO.setCustodia(ativoAtualizado.getCustodia());
                ativoCarteiraRFDTO.setPrecoMedio(ativoAtualizado.getPrecoMedio());
            }
        });

        log.info("[PERF] === listarAtivosCarteiraRendaFixa TOTAL: {}ms ===", System.currentTimeMillis() - inicio);
        return ativosCarteiraDTO;
    }

    private static Map<String, AtivoCarteiraRFDTO> agruparPorAtivo(List<AtivoCarteiraRFDTO> carteiraRendaFixa) {
        Map<String, AtivoCarteiraRFDTO> groupedBySiglaAtivo = carteiraRendaFixa.stream()
                .collect(Collectors.groupingBy(
                        AtivoCarteiraRFDTO::getSiglaAtivo,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                lista -> {
                                    AtivoCarteiraRFDTO result = new AtivoCarteiraRFDTO();

                                    result.setSiglaAtivo(lista.get(0).getSiglaAtivo());
                                    result.setNomeAtivo(lista.get(0).getNomeAtivo());
                                    result.setIdAtivo(lista.get(0).getIdAtivo());

                                    double totalValorMercado = lista.stream().mapToDouble(AtivoCarteiraRFDTO::getValorMercado).sum();
                                    double custodia = lista.stream().mapToDouble(AtivoCarteiraRFDTO::getCustodia).sum();
                                    double custo = lista.stream().mapToDouble(AtivoCarteiraRFDTO::getCusto).sum();
                                    double precoMedio = lista.stream().mapToDouble(AtivoCarteiraRFDTO::getPrecoMedio).sum();

                                    result.setCusto(custo);
                                    result.setCustodia(custodia);
                                    result.setValorMercado(totalValorMercado);
                                    result.setPrecoMedio(precoMedio);

                                    return result;

                                }
                        )
                ));
        return groupedBySiglaAtivo;
    }


    public AtivoCarteiraDTO getAtivoCarteiraById(int id) {
        Optional<AtivoCarteira> optionalAtivoCarteira = repository.findByIdAndUser(id, SecurityUtils.getCurrentUserId());
        if(optionalAtivoCarteira.isPresent()) {
            return AtivoCarteiraMapper.INSTANCE.toDTO(optionalAtivoCarteira.get());
        } else {
            throw new NullPointerException("Nenhum ativo encontrado");
        }

    }

    public List<PatrimonioCategoriaDTO> getPatrimonioPorCategoria() {
        var patrimonioPorCategoria = repository.getPatrimonioPorCategoria(SecurityUtils.getCurrentUserId());

        var rendaInternacional = patrimonioPorCategoria.stream().filter(PatrimonioCategoriaDTO::isInternacional)
                .mapToDouble(PatrimonioCategoriaDTO::getValor).reduce(0.0, Double::sum);

        var rendaFixa = patrimonioPorCategoria.stream().filter(val -> val.getSubClasseId() > 2 && !val.isInternacional())
                .mapToDouble(PatrimonioCategoriaDTO::getValor).reduce(0.0, Double::sum);

        var acoes = patrimonioPorCategoria.stream().filter(val -> val.getSubClasseId() == 2 && !val.isInternacional())
                .mapToDouble(PatrimonioCategoriaDTO::getValor).sum();

        var fiis = patrimonioPorCategoria.stream().filter(val -> val.getSubClasseId() == 1 && !val.isInternacional())
                .mapToDouble(PatrimonioCategoriaDTO::getValor).sum();

        var metas = alocacaoMetaService.obterPercentuaisPorCategoria();

        List<PatrimonioCategoriaDTO> lista = Arrays.asList(
                new PatrimonioCategoriaDTO(0, CategoriaAlocacao.ACOES.getDescricao(), metas.get(CategoriaAlocacao.ACOES), acoes),
                new PatrimonioCategoriaDTO(0, CategoriaAlocacao.FIIS.getDescricao(), metas.get(CategoriaAlocacao.FIIS), fiis),
                new PatrimonioCategoriaDTO(0, CategoriaAlocacao.RENDA_FIXA.getDescricao(), metas.get(CategoriaAlocacao.RENDA_FIXA), rendaFixa),
                new PatrimonioCategoriaDTO(0, CategoriaAlocacao.RENDA_INTERNACIONAL.getDescricao(), metas.get(CategoriaAlocacao.RENDA_INTERNACIONAL), rendaInternacional)
        );

        return lista;

    }

    public List<ProventosMesDTO> listarProventos() {
        var proventos = repository.listarProventosAnoMes(SecurityUtils.getCurrentUserId());
        return  proventos;
    }

    public List<TopPagadoraProventosDTO> listarPagadoras() {
        var topPagadoras = repository.listarTopPagadoras(SecurityUtils.getCurrentUserId());
        topPagadoras = topPagadoras.stream().map(res -> {
            var logo = ativoRepository.findUrlLogoBySigla(res.getAtivo());
            logo.ifPresent(res::setLogo);
            return res;
        }).sorted(Comparator.comparingDouble(TopPagadoraProventosDTO::getValor).reversed()).collect(Collectors.toList());
        return  topPagadoras;
    }

    public void excluirAtivoCarteira(int id) {
       var ativoCarteira = getAtivoCarteiraById(id);
       this.repository.delete(AtivoCarteiraMapper.INSTANCE.toEntity(ativoCarteira));
    }
    public AtivoCarteiraDTO atualizarAtivoCarteira(int id, AtivoCarteiraDTO ativoCarteiraAddDTO) {
        Optional<AtivoCarteira> ativoCarteiraOptional = repository.findByIdAndUser(id, SecurityUtils.getCurrentUserId());
        if(ativoCarteiraOptional.isPresent()) {
        AtivoCarteiraMapper.INSTANCE.updateEntityFromDto(ativoCarteiraAddDTO, ativoCarteiraOptional.get());
        AtivoCarteira savedAtivoCarteira = repository.save(ativoCarteiraOptional.get());
        return AtivoCarteiraMapper.INSTANCE.toDTO(savedAtivoCarteira);
        } else {
            throw new NullPointerException("Ativo não localizado");
        }

    }

    public void updateCarteira(Integer idSubclasse) {

        List<AtivoCarteiraDTO> carteiraAlterada;

        if(idSubclasse == 1) { carteiraAlterada = listarAtivosCarteiraFiis(); }
        else if(idSubclasse == 2) { carteiraAlterada = listarCarteiraAcoes(); } else if(idSubclasse > 2) {
            carteiraAlterada = listarAtivosCarteiraRendaFixa();
        } else {
            carteiraAlterada = new ArrayList<>();
        }

        List<AtivoCarteira> entidades = repository.findAllByUser(SecurityUtils.getCurrentUserId()).stream()
                .filter(ativo -> ativo.getCustodia() > 0)
                .filter(ativo -> idSubclasse > 2
                        ? ativo.getAtivo().getSubclasseAtivo().getId() > 2
                        : ativo.getAtivo().getSubclasseAtivo().getId() == idSubclasse)
                .collect(Collectors.toList());

        entidades.forEach(entity -> {
            double valorMercado = carteiraAlterada.stream()
                    .filter(item -> item.getAtivo().getId() == entity.getAtivo().getId())
                    .mapToDouble(AtivoCarteiraDTO::getValorMercado).sum();
            entity.setValorMercado(valorMercado);
        });

        repository.saveAll(entidades);
    }

}
