package br.com.cashcontroller.controller;

import br.com.cashcontroller.entity.Aporte;
import br.com.cashcontroller.service.OperacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/aportes")
@RestController
public class AporteController {

    @Autowired
    OperacaoService operacaoService;
    @PostMapping
    public ResponseEntity<Aporte> cadastrarAporte(@RequestBody Aporte aporte) {
        return ResponseEntity.ok(operacaoService.cadastrarAporte(aporte));
    }

    @GetMapping
    public ResponseEntity<List<Aporte>> listarAportes() {
        return ResponseEntity.ok(operacaoService.listarAportes());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> listarAportes(@PathVariable("id") Integer id) {
        operacaoService.excluirAporte(id);
        return ResponseEntity.noContent().build();
    }
}
