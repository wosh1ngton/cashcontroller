package br.com.cashcontroller.service;

import br.com.cashcontroller.dto.*;
import br.com.cashcontroller.entity.OperacaoRendaFixa;
import br.com.cashcontroller.entity.OperacaoRendaVariavel;
import br.com.cashcontroller.entity.TipoOperacao;
import br.com.cashcontroller.mapper.OperacaoRendaFixaMapper;
import br.com.cashcontroller.mapper.OperacaoRendaVariavelMapper;
import br.com.cashcontroller.mapper.TipoOperacaoMapper;
import br.com.cashcontroller.repository.OperacaoRendaFixaRepository;
import br.com.cashcontroller.repository.OperacaoRendaVariavelRepository;
import br.com.cashcontroller.repository.TipoOperacaoRepository;
import br.com.cashcontroller.utils.Taxa;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class OperacaoService {

    @Autowired
    OperacaoRendaVariavelRepository operacaoRendaVariavelRepository;
    @Autowired
    OperacaoRendaFixaRepository operacaoRendaFixaRepository;

    @Autowired
    TipoOperacaoRepository tipoOperacaoRepository;

    public List<OperacaoRendaVariavelDTO> listarOperacoesRendaVariavel() {
        List<OperacaoRendaVariavel> operacoes = operacaoRendaVariavelRepository.findAll();
        return OperacaoRendaVariavelMapper.INSTANCE.toListDTO(operacoes);
    }

    public List<OperacaoRendaVariavelDTO> listarOperacoesRendaVariavelPorData(Filter filter) {

        List<OperacaoRendaVariavel> operacoes = operacaoRendaVariavelRepository.findOperacoesByData(filter.getStartDate(), filter.getEndDate(), filter.getSubclasse(), filter.getAno(), filter.getMes());
        return OperacaoRendaVariavelMapper.INSTANCE.toListDTO(operacoes);
    }

    public double valorTotalOperacao(
            OperacaoRendaVariavelSaveDTO operacaoDto) {

        double   valorBase = operacaoDto.getValorUnitario() * operacaoDto.getQuantidadeNegociada();
        return calcularTaxas(operacaoDto, valorBase);
    }

    public double custoTotalOperacao(OperacaoRendaVariavelSaveDTO operacaoDto) {

        double valorBase = 0.0;
        if(operacaoDto.getTipoOperacaoDto() == 2) {
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
        double impostos = (operacaoDto.getValorCorretagem()/Taxa.IMPOSTOS)- operacaoDto.getValorCorretagem();
        double taxaCorretora = operacaoDto.getValorCorretagem() * Taxa.TAXA_OPERACIONAL_XP;
        double custos =  impostos + (valorBase * (taxas/100));
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

        if(operacaoRendaVariavelSaveDTO.getTipoOperacaoDto() == 3) {
           operacaoRendaVariavelSaveDTO.setQuantidadeNegociada(desdobrarAtivo(operacaoRendaVariavelSaveDTO));
        }

        operacaoRendaVariavelSaveDTO.setCustoTotal(custoTotalOperacao(operacaoRendaVariavelSaveDTO));
        operacaoRendaVariavelSaveDTO.setValorTotal(valorTotalOperacao(operacaoRendaVariavelSaveDTO));
        OperacaoRendaVariavel operacao = OperacaoRendaVariavelMapper.INSTANCE.toSaveEntity(operacaoRendaVariavelSaveDTO);
        return OperacaoRendaVariavelMapper.INSTANCE.toDTO(operacaoRendaVariavelRepository.save(operacao));
    }



    private Long desdobrarAtivo(OperacaoRendaVariavelSaveDTO operacaoRendaVariavelSaveDTO) {
        Long fatorDeProporcao = (long) operacaoRendaVariavelSaveDTO.getQuantidadeNegociada();
        Long totalAtualDeAcoes = this.operacaoRendaVariavelRepository.getCustodiaPorAtivo(operacaoRendaVariavelSaveDTO.getAtivoDto());
        Long totalDoAumento = (fatorDeProporcao * totalAtualDeAcoes )- totalAtualDeAcoes;
        return  totalDoAumento;
    }

    public OperacaoRendaVariavelDTO atualizarOperacaoRendaVariavel(OperacaoRendaVariavelDTO operacaoRendaVariavelDTO) {

        var saveDto = OperacaoRendaVariavelMapper.INSTANCE.FromDTOtoSaveDTO(operacaoRendaVariavelDTO);
        operacaoRendaVariavelDTO.setCustoTotal(custoTotalOperacao(saveDto));
        operacaoRendaVariavelDTO.setValorTotal(valorTotalOperacao(saveDto));
        OperacaoRendaVariavel operacao = new OperacaoRendaVariavel();
        if(operacaoRendaVariavelDTO.getId() != 0) {
            this.operacaoRendaVariavelRepository.findById(operacaoRendaVariavelDTO.getId()).get();
            operacao = OperacaoRendaVariavelMapper.INSTANCE.toEntity(operacaoRendaVariavelDTO);
        }
        return OperacaoRendaVariavelMapper.INSTANCE.toDTO(operacaoRendaVariavelRepository.save(operacao));
    }

    public OperacaoRendaFixaDTO atualizarOperacaoRendaFixa(OperacaoRendaFixaDTO operacaoRendaFixaDTO) {
        OperacaoRendaFixa operacao = new OperacaoRendaFixa();
        if(operacaoRendaFixaDTO.getId() != 0) {
            this.operacaoRendaFixaRepository.findById(operacaoRendaFixaDTO.getId()).get();
            operacao = OperacaoRendaFixaMapper.INSTANCE.toEntity(operacaoRendaFixaDTO);
        }
        return OperacaoRendaFixaMapper.INSTANCE.toDTO(operacaoRendaFixaRepository.save(operacao));
    }

    public void excluirOperacaoRendaVariavel(int id) {

        Optional<OperacaoRendaVariavel> operacao = operacaoRendaVariavelRepository.findById(id);
        operacao.ifPresent(value -> this.operacaoRendaVariavelRepository.delete(value));

    }

    public void excluirOperacaoRendaFixa(int id) {

        Optional<OperacaoRendaFixa> operacao = operacaoRendaFixaRepository.findById(id);
        operacao.ifPresent(value -> this.operacaoRendaFixaRepository.delete(value));

    }

    public OperacaoRendaFixaDTO cadastrarOperacaoRendaFixa(OperacaoRendaFixaDTO operacaoRendaFixaDto) {
        OperacaoRendaFixa operacao = OperacaoRendaFixaMapper.INSTANCE.toEntity(operacaoRendaFixaDto);
        return OperacaoRendaFixaMapper.INSTANCE.toDTO(operacaoRendaFixaRepository.save(operacao));
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
        if(valorTotalEmVendas > Taxa.LIMITE_IR && resultadoMes > 0) {
            irMes.setImposto(true);
            irMes.setValorAPagar(resultadoMes * Taxa.ALIQUOTA_IR);
        }
        return  irMes;
    }



    private void oterResultadoOperacao(List<OperacaoRendaVariavelDTO> vendasDoMes) {
        for (OperacaoRendaVariavelDTO op : vendasDoMes) {
            double resultadoOperacao = op.getValorTotal() - op.getCustoTotal();
            op.setResultadoOperacao(resultadoOperacao);
        }
    }


    private AtivoDTO calcularPrecoMedio(int idAtivo, LocalDate dataDeCorte) {
        LocalDate startDate = LocalDate.of(2015, 1, 1);
        return this.operacaoRendaVariavelRepository.calcularPrecoMedio(idAtivo,dataDeCorte, startDate);

    }


    private List<OperacaoRendaVariavelDTO> listarVendasDoMes(Filter filter) {
        var mes = filter.getEndDate().getMonth();
        var operacoes = listarOperacoesRendaVariavelPorData(filter);

        var operacoesDeVenda = operacoes.stream().filter(op -> op.getTipoOperacaoDto().getNome().contains("Venda") && op.getDataOperacao().getMonth() == mes).collect(Collectors.toList());
        return operacoesDeVenda;
    }

    public List<AtivoCarteiraDTO> listarCarteiraDeAcoes() {
        return  this.operacaoRendaVariavelRepository.listarCarteiraDeAcoes();
    }

    public List<PosicaoEncerradaDTO> listarPosicoesEncerradas() {
        return this.operacaoRendaVariavelRepository.listarAtivosComOperacoesFechadas();
    }

    public List<Integer> listarAnosComOperacoes() {
        return this.operacaoRendaVariavelRepository.listarAnosComOperacoes();
    }


    public List<MesDTO> listarMesesComOperacoes(@PathVariable(value = "ano") Integer ano) {
        return this.operacaoRendaVariavelRepository.listarMesesComOperacoesPorAno(ano);
    }
}
