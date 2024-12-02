package br.com.cashcontroller.controller;

import br.com.cashcontroller.dto.IndexadorDTO;
import br.com.cashcontroller.entity.IpcaMes;
import br.com.cashcontroller.entity.SelicMes;
import br.com.cashcontroller.external.service.IndicesService;
import br.com.cashcontroller.service.IndexadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/indexadores")
public class IndexadorController {

    @Autowired
    IndexadorService service;

    @Autowired
    IndicesService indicesService;


    @GetMapping
    public ResponseEntity<List<IndexadorDTO>> listarIndexadores() {
        return  ResponseEntity.ok(service.listarIndexadores());
    }
    @PostMapping("/ipca-mes")
    public ResponseEntity<List<IpcaMes>> ipcaMes(@RequestBody List<IpcaMes> ipcaMes) {
        return ResponseEntity.ok(service.cadastrarIpcaMesEmLote(ipcaMes));
    }

    @PostMapping("/selic-mes")
    public ResponseEntity<List<SelicMes>> selicMes(@RequestBody List<SelicMes> selicMes) {
        return ResponseEntity.ok(service.cadastrarSelicMesEmLote(selicMes));
    }

    @GetMapping("/mes-atual")
    public ResponseEntity<Void> selicMes() {
        indicesService.saveSelicMesAtual();
        return ResponseEntity.noContent().build();
    }

}
