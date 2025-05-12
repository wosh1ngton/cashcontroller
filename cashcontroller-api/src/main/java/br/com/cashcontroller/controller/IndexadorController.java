package br.com.cashcontroller.controller;

import br.com.cashcontroller.dto.IndexadorDTO;
import br.com.cashcontroller.dto.IndiceDTO;
import br.com.cashcontroller.entity.IpcaMes;
import br.com.cashcontroller.entity.SelicMes;
import br.com.cashcontroller.external.service.IndicesService;
import br.com.cashcontroller.service.IndexadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
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

    @PostMapping("/ipca-mes-unitario")
    public ResponseEntity<IndiceDTO> ipcaMes(@RequestBody IndiceDTO ipcaMes) {
        return ResponseEntity.ok(service.cadastrarIpcaMes(ipcaMes));
    }

    @PutMapping("/ipca-mes-unitario")
    public ResponseEntity<Void> editarIpcaMes(@RequestBody IndiceDTO ipcaMes) {
        service.editarIPCAMes(ipcaMes);
        return ResponseEntity.noContent().build();
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

    @GetMapping("/listar-valores-indice/{indice}")
    public ResponseEntity<List<IndiceDTO>> listarIndices(@PathVariable("indice") String indice)  {
        return ResponseEntity.ok(indicesService.listarHistoricoIndice(indice));
    }

}
