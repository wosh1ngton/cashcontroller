package br.com.cashcontroller.controller;

import br.com.cashcontroller.dto.*;
import br.com.cashcontroller.entity.Aporte;
import br.com.cashcontroller.entity.IpcaMes;
import br.com.cashcontroller.entity.SelicMes;
import br.com.cashcontroller.service.OperacaoService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/operacoes")
public class OperacaoController {

    @Autowired
    OperacaoService operacaoService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<OperacaoRendaVariavelDTO> findById(@PathVariable(value="id") Integer id) {

        OperacaoRendaVariavelDTO operacao = operacaoService.findById(id);
        return ResponseEntity.ok(operacao);
    }

    @GetMapping("/renda-variavel")
    public ResponseEntity<List<OperacaoRendaVariavelDTO>> getOperacoesRendaVariavel() {

        List<OperacaoRendaVariavelDTO> operacoes = operacaoService.listarOperacoesRendaVariavel();
        return ResponseEntity.ok(operacoes);
    }

    @PostMapping("/renda-variavel/filter")
    public ResponseEntity<List<OperacaoRendaVariavelDTO>> filter(@RequestBody Filter filter) {

        List<OperacaoRendaVariavelDTO> operacoes = operacaoService.listarOperacoesRendaVariavelPorData(filter);
        return ResponseEntity.ok(operacoes);
    }

    @PostMapping("/renda-variavel/irpf")
    public ResponseEntity<IrpfMesDTO> irpf(@RequestBody Filter filter) {

        IrpfMesDTO operacoes = operacaoService.calcularImpostoMensal(filter);
        return ResponseEntity.ok(operacoes);
    }

    @GetMapping("/renda-fixa")
    public ResponseEntity<List<OperacaoRendaFixaDTO>> getOperacoesRendaFixa() {
        return ResponseEntity.ok(this.operacaoService.listarOperacoesRendaFixa());
    }
    @GetMapping("/tipo-operacoes")
    public ResponseEntity<List<TipoOperacaoDTO>> getTiposOperacao() {
        return ResponseEntity.ok(this.operacaoService.listarTipoOperacao());
    }
    @GetMapping("/ativos-operados")
    public ResponseEntity<List<ItemLabelDTO>> getAtivosOperados() {
        return ResponseEntity.ok(this.operacaoService.listarAtivosOperados());
    }


    @PostMapping
    public ResponseEntity<OperacaoRendaVariavelDTO> cadastrarOperacaoRendaVariavel(@RequestBody OperacaoRendaVariavelSaveDTO operacaoRendaVariavelSaveDTO) {
        return ResponseEntity.ok(this.operacaoService.cadastrarOperacaoRendaVariavel(operacaoRendaVariavelSaveDTO));
    }

    @PostMapping(value = "/renda-fixa")
    public ResponseEntity<OperacaoRendaFixaDTO> cadastrarOperacaoRendaFixa(@RequestBody OperacaoRendaFixaDTO operacaoRendaFixaDTO) {
        return ResponseEntity.ok(this.operacaoService.cadastrarOperacaoRendaFixa(operacaoRendaFixaDTO));
    }

    @PutMapping
    public ResponseEntity<OperacaoRendaVariavelDTO> editarOperacaoRendaVariavel(@RequestBody OperacaoRendaVariavelDTO operacaoRendaVariavelDTO) {
        return ResponseEntity.ok(this.operacaoService.atualizarOperacaoRendaVariavel(operacaoRendaVariavelDTO));
    }

    @PutMapping(value = "/renda-fixa")
    public ResponseEntity<OperacaoRendaFixaDTO> editarOperacaoRendaFixa(@RequestBody OperacaoRendaFixaDTO operacaoRendaFixaDTO) {
        return ResponseEntity.ok(this.operacaoService.atualizarOperacaoRendaFixa(operacaoRendaFixaDTO));
    }
    @DeleteMapping(value = "/{id}")
    ResponseEntity<?> excluirOperacaoRendaVariavel(@PathVariable(value="id") Integer id) {
        this.operacaoService.excluirOperacaoRendaVariavel(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/renda-fixa/{id}")
    ResponseEntity<?> excluirOperacaoRendaFixa(@PathVariable(value="id") Integer id) {
        this.operacaoService.excluirOperacaoRendaFixa(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/carteira-acoes")
    ResponseEntity<List<AtivoCarteiraDTO>> listarCarteiraAcoes() {
        var carteira = this.operacaoService.listarCarteiraDeAcoes();
        return ResponseEntity.ok(carteira);
    }

    @GetMapping(value = "/carteira-renda-fixa")
    ResponseEntity<List<AtivoCarteiraRFDTO>> listarCarteiraRendaFixa() {
        var carteira = this.operacaoService.listarCarteiraRendaFixa();
        return ResponseEntity.ok(carteira);
    }

    @GetMapping(value = "/carteira-fiis")
    ResponseEntity<List<AtivoCarteiraDTO>> listarCarteiraFiis() {
        var carteira = this.operacaoService.listarCarteiraDeFiis();
        return ResponseEntity.ok(carteira);
    }


    @GetMapping(value = "/posicoes-encerradas")
    ResponseEntity<List<PosicaoEncerradaDTO>> listarPosicoesEncerradas() {
        var encerradaDTOS = this.operacaoService.listarPosicoesEncerradas();
        return ResponseEntity.ok(encerradaDTOS);
    }

    @GetMapping("/anos-com-operacoes")
    ResponseEntity<List<Integer>> listarAnosComOperacoes() {
        var anosOrdenados = this.operacaoService.listarAnosComOperacoes().stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        return ResponseEntity.ok(anosOrdenados);
    }

    @GetMapping("/anos-com-operacoes-rf")
    ResponseEntity<List<Integer>> listarAnosComOperacoesRF() {
        var anosOrdenados = this.operacaoService.listarAnosComOperacoes(Optional.of(true)).stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        return ResponseEntity.ok(anosOrdenados);
    }
    @GetMapping("/MesesComOperacoesPorAno/{ano}")
    ResponseEntity<List<MesDTO>> listarMesesComDespesas(@PathVariable(value = "ano") Integer ano) {
        return ResponseEntity.ok(this.operacaoService.listarMesesComOperacoes(ano));
    }

    @GetMapping("/meses-com-operacoes-rf/{ano}")
    ResponseEntity<List<MesDTO>> listarMesesComDespesasRF(@PathVariable(value = "ano") Integer ano) {
        return ResponseEntity.ok(this.operacaoService.listarMesesComOperacoesRF(ano));
    }

    @GetMapping("/por-ativo/{idAtivo}")
    ResponseEntity<List<OperacaoRendaVariavelDTO>> listarOperacoesPorAtivo(@PathVariable(value = "idAtivo") Integer idAtivo) {
        return ResponseEntity.ok(this.operacaoService.listarOperacoesPorAtivo(idAtivo));
    }

    @PostMapping("/renda-fixa/filter")
    public ResponseEntity<List<OperacaoRendaFixaDTO>> filterRF(@RequestBody Filter filter) {

        List<OperacaoRendaFixaDTO> operacoes = operacaoService.listarOperacoesRendaFixaPorData(filter);
        return ResponseEntity.ok(operacoes);
    }




}
