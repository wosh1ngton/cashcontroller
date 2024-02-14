package br.com.cashcontroller.controller;

import br.com.cashcontroller.dto.OperacaoRendaFixaDTO;
import br.com.cashcontroller.dto.OperacaoRendaVariavelDTO;
import br.com.cashcontroller.service.OperacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/operacoes")
public class OperacaoController {

    @Autowired
    OperacaoService operacaoService;

    @PostMapping
    public ResponseEntity<OperacaoRendaVariavelDTO> cadastrarOperacaoRendaVariavel(@RequestBody OperacaoRendaVariavelDTO operacaoRendaVariavelDTO) {
        return ResponseEntity.ok(this.operacaoService.cadastrarOperacaoRendaVariavel(operacaoRendaVariavelDTO));
    }

    @PutMapping
    public ResponseEntity<OperacaoRendaVariavelDTO> editarOperacaoRendaVariavel(@RequestBody OperacaoRendaVariavelDTO operacaoRendaVariavelDTO) {
        return ResponseEntity.ok(this.operacaoService.atualizarOperacaoRendaVariavel(operacaoRendaVariavelDTO));
    }
    @DeleteMapping(value = "/{id}")
    ResponseEntity<?> excluirOperacaoRendaVariavel(@PathVariable(value="id") Integer id) {
        this.operacaoService.excluirOperacaoRendaVariavel(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/renda-fixa")
    public ResponseEntity<OperacaoRendaFixaDTO> cadastrarOperacaoRendaFixa(@RequestBody OperacaoRendaFixaDTO operacaoRendaFixaDto) {
        return ResponseEntity.ok(this.operacaoService.cadastrarOperacaoRendaFixa(operacaoRendaFixaDto));
    }


	
}
