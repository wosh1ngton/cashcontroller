package br.com.cashcontroller.controller;

import br.com.cashcontroller.dto.*;
import br.com.cashcontroller.service.OperacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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

        return ResponseEntity.ok(this.operacaoService.listarCarteiraDeAcoes());
    }

    @GetMapping("/anos-com-operacoes")
    ResponseEntity<List<Integer>> listarAnosComOperacoes() {
        return ResponseEntity.ok(this.operacaoService.listarAnosComOperacoes());
    }
    @GetMapping("/MesesComOperacoesPorAno/{ano}")
    ResponseEntity<List<MesDTO>> listarMesesComDespesas(@PathVariable(value = "ano") Integer ano) {
        return ResponseEntity.ok(this.operacaoService.listarMesesComOperacoes(ano));
    }
}
