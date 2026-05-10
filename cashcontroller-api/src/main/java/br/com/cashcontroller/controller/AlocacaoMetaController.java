package br.com.cashcontroller.controller;

import br.com.cashcontroller.dto.AlocacaoMetaDTO;
import br.com.cashcontroller.service.AlocacaoMetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alocacao-meta")
public class AlocacaoMetaController {

    @Autowired
    private AlocacaoMetaService service;

    @GetMapping
    public ResponseEntity<List<AlocacaoMetaDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @PutMapping
    public ResponseEntity<List<AlocacaoMetaDTO>> salvar(@RequestBody List<AlocacaoMetaDTO> metas) {
        return ResponseEntity.ok(service.salvar(metas));
    }
}
