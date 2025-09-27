package br.com.cashcontroller.controller;

import br.com.cashcontroller.dto.IndexadorDTO;
import br.com.cashcontroller.dto.IndiceDTO;
import br.com.cashcontroller.entity.IndiceMesBase;
import br.com.cashcontroller.entity.IpcaMes;
import br.com.cashcontroller.entity.SelicMes;
import br.com.cashcontroller.external.service.IndicesService;
import br.com.cashcontroller.service.IndexadorService;
import br.com.cashcontroller.service.IndiceMesFacadeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/indexadores")
public class IndexadorController {

    @Autowired
    IndexadorService service;

    @Autowired
    IndicesService indicesService;
    @Autowired
    ObjectMapper objectMapper;


    @Autowired
    private IndiceMesFacadeService facade;


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

    @PutMapping("/indice-mes")
    public ResponseEntity<Void> editarIndice(@RequestBody IndiceDTO indiceDTO) {
        service.editarIndiceMes(indiceDTO);
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

    @PostMapping("/{tipo}")
    public ResponseEntity<?> save(
            @PathVariable String tipo,
            @RequestBody Map<String, Object> json) {
        IndiceMesBase entity = convertToEntity(tipo, json);
        return ResponseEntity.ok(facade.save(tipo, entity));
    }

    @PutMapping("/{tipo}/{id}")
    public ResponseEntity<?> update(
            @PathVariable String tipo,
            @PathVariable int id,
            @RequestBody Map<String, Object> json
            ) {
        IndiceMesBase entity = convertToEntity(tipo, json);
        return ResponseEntity.ok(facade.update(tipo,id,entity));
    }

    private IndiceMesBase convertToEntity(String tipo, Map<String, Object> json) {

        switch (tipo.toLowerCase()) {
            case "ipca":
                return objectMapper.convertValue(json, IpcaMes.class);
            case "selic":
                return objectMapper.convertValue(json, SelicMes.class);
            default:
                throw new IllegalArgumentException("Tipo desconhecido");
        }
    }



    @PostConstruct
    public void logModules() {
        System.out.println("Registered Jackson modules: ");
        objectMapper.getRegisteredModuleIds().forEach(System.out::println);
    }
}
