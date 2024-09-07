package br.com.cashcontroller.service;

import br.com.cashcontroller.dto.AtivoCarteiraDTO;
import br.com.cashcontroller.dto.AtivoCarteiraRFDTO;
import br.com.cashcontroller.entity.AtivoCarteira;
import br.com.cashcontroller.entity.OperacaoRendaFixa;
import br.com.cashcontroller.external.dto.stock.StocksDTO;
import br.com.cashcontroller.external.dto.tesouro.TesouroDiretoDTO;
import br.com.cashcontroller.external.dto.tesouro.TrsrBdTradgDTO;
import br.com.cashcontroller.external.service.RendaVariavelService;
import br.com.cashcontroller.external.service.TesouroService;
import br.com.cashcontroller.mapper.AtivoCarteiraMapper;
import br.com.cashcontroller.repository.AtivoCarteiraRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AtivoCarteiraService {
    @Autowired
    AtivoCarteiraRepository repository;
    private AtivoCarteiraMapper mapper;

    @Autowired
    CalcularRentabilidadeService calcularRentabilidadeService;

    @Autowired
    CalculaImpostoService calculaImpostoService;

    @Autowired
    TesouroService tesouroService;

    @Autowired
    RendaVariavelService rendaVariavelService;

    @Autowired
    OperacaoService operacaoService;

    public AtivoCarteiraDTO cadastrarAtivoCarteira(AtivoCarteiraDTO ativoCarteiraDTO) {
        var entity = this.repository.save(AtivoCarteiraMapper.INSTANCE.toEntity(ativoCarteiraDTO));
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
        var entities = this.repository.findAll();
        var ativosCarteiraDTO =  entities.stream().map(AtivoCarteiraMapper.INSTANCE::toListDTO).collect(Collectors.toList());
        return ativosCarteiraDTO;
    }


    public List<AtivoCarteiraDTO> listarAtivosCarteiraRendaFixa() {

        TesouroDiretoDTO response = this.tesouroService.getTitulos();
        var titulosTesouro = response.getResponse().getTrsrBdTradgList().stream().map(TrsrBdTradgDTO::getTrsrBd).collect(Collectors.toList());
        var ativosCarteiraDTO = getCarteira();
        ativosCarteiraDTO = ativosCarteiraDTO.stream().filter(a -> a.getAtivo().getSubclasseAtivo().getId() > 2).toList();
        //List<AtivoCarteiraRFDTO> carteira = this.operacaoRendaFixaRepository.listarCarteiraRendaFixa();

//        carteira = carteira.stream().filter(ativoCarteira -> {
//            List<OperacaoRendaFixa> operacoes = verificarVendaParcial(ativoCarteira);
//            return operacoes.isEmpty() || operacoes.stream().anyMatch(operacao -> operacao.getId() == ativoCarteira.getIdOperacao());
//
//        }).collect(Collectors.toList());

        ativosCarteiraDTO.forEach(
                ativoCarteiraRFDTO -> {
                    var rfdto = AtivoCarteiraMapper.INSTANCE.toRFDTO(ativoCarteiraRFDTO);
                    //ativoCarteiraRFDTO.setCusto(ativoCarteiraRFDTO.getCustodia() * ativoCarteiraRFDTO.getPrecoMedio());
                    //ativoCarteiraRFDTO.setTotalEmProventos(eventoService.getTotalProventosPorAtivoRendaFixa(ativoCarteiraRFDTO.getIdAtivo()));
                    if (ativoCarteiraRFDTO.getAtivo().getSubclasseAtivo().getId() == 3) {
                        CalcularValorTesouroDireto.calcularValorDeMercadoTesouroDireto(ativoCarteiraRFDTO, titulosTesouro);
                    }

                }
        );

        return ativosCarteiraDTO;
    }

//    public List<AtivoCarteiraDTO> listarAtivosCarteiraRendaFixa() {
//        var ativosCarteiraDTO = getCarteira();
//        ativosCarteiraDTO = ativosCarteiraDTO.stream().filter(a -> a.getAtivo().getSubclasseAtivo().getId() > 2).toList();
//        return ativosCarteiraDTO;
//    }

    public AtivoCarteiraDTO getAtivoCarteiraById(int id) {
        Optional<AtivoCarteira> optionalAtivoCarteira = repository.findById(id);
        if(optionalAtivoCarteira.isPresent()) {
            return AtivoCarteiraMapper.INSTANCE.toDTO(optionalAtivoCarteira.get());
        } else {
            throw new NullPointerException("Nenhum ativo encontrado");
        }

    }

    public void excluirAtivoCarteira(int id) {
       var ativoCarteira = getAtivoCarteiraById(id);
       this.repository.delete(AtivoCarteiraMapper.INSTANCE.toEntity(ativoCarteira));
    }
    public AtivoCarteiraDTO atualizarAtivoCarteira(int id, AtivoCarteiraDTO ativoCarteiraAddDTO) {
        Optional<AtivoCarteira> ativoCarteiraOptional = repository.findById(id);
        if(ativoCarteiraOptional.isPresent()) {
        AtivoCarteiraMapper.INSTANCE.updateEntityFromDto(ativoCarteiraAddDTO, ativoCarteiraOptional.get());
        AtivoCarteira savedAtivoCarteira = repository.save(ativoCarteiraOptional.get());
        return AtivoCarteiraMapper.INSTANCE.toDTO(savedAtivoCarteira);
        } else {
            throw new NullPointerException("Ativo não localizado");
        }

    }

}
