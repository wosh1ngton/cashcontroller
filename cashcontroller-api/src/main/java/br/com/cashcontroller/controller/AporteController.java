package br.com.cashcontroller.controller;

import br.com.cashcontroller.dto.AporteDTO;
import br.com.cashcontroller.entity.Aporte;
import br.com.cashcontroller.service.AporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/aportes")
@RestController
public class AporteController {

    @Autowired
    AporteService aporteService;
    @PostMapping
    public ResponseEntity<Aporte> cadastrarAporte(@RequestBody Aporte aporte) {
        return ResponseEntity.ok(aporteService.cadastrarAporte(aporte));
    }

    @PutMapping
    public ResponseEntity<AporteDTO> editarAporte(@RequestBody AporteDTO aporteDTO) {
        return ResponseEntity.ok(aporteService.editarAporte(aporteDTO));
    }

    @GetMapping
    public ResponseEntity<List<Aporte>> listarAportes() {

        return ResponseEntity.ok(aporteService.listarAportes());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> listarAportes(@PathVariable("id") Integer id) {
        aporteService.excluirAporte(id);
        return ResponseEntity.noContent().build();
    }
}
