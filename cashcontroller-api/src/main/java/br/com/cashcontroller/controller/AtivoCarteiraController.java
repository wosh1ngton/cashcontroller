package br.com.cashcontroller.controller;

import br.com.cashcontroller.dto.AtivoCarteiraDTO;
import br.com.cashcontroller.dto.PatrimonioCategoriaDTO;
import br.com.cashcontroller.dto.ProventosMesDTO;
import br.com.cashcontroller.dto.TopPagadoraProventosDTO;
import br.com.cashcontroller.external.service.RendaVariavelService;
import br.com.cashcontroller.service.AtivoCarteiraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ativo-carteira")
public class AtivoCarteiraController {

    @Autowired
    AtivoCarteiraService service;

    @Autowired
    RendaVariavelService rendaVariavelService;
    @PostMapping
    public ResponseEntity<AtivoCarteiraDTO> cadastrarAtivoCarteira(@RequestBody() AtivoCarteiraDTO ativoCarteiraDTO) {
        return ResponseEntity.ok(service.cadastrarAtivoCarteira(ativoCarteiraDTO));
    }

    @GetMapping
    public ResponseEntity<?> listarCarteiraPrincipal() {
        try {
            List<AtivoCarteiraDTO> ativosCarteira = service.listarCarteiraAcoes();
            return ResponseEntity.ok(ativosCarteira);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    @GetMapping("/fiis")
    public ResponseEntity<?> listarCarteiraFiis() {
        try {
            List<AtivoCarteiraDTO> ativosCarteira = service.listarAtivosCarteiraFiis();
            return ResponseEntity.ok(ativosCarteira);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    @GetMapping("/renda-fixa")
    public ResponseEntity<?> listarCarteiraRendaFixa() {

            List<AtivoCarteiraDTO> ativosCarteira = service.listarAtivosCarteiraRendaFixa();
            return ResponseEntity.ok(ativosCarteira);

    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<AtivoCarteiraDTO> getAtivoCarteiraById(@PathVariable("id") int idAtivoCarteira) {
        return ResponseEntity.ok(service.getAtivoCarteiraById(idAtivoCarteira));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> excluirAtivoCarteira(@PathVariable("id") int idAtivoCarteira) {
        service.excluirAtivoCarteira(idAtivoCarteira);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<AtivoCarteiraDTO> atualizarAtivoCarteira(@PathVariable Integer id, @RequestBody AtivoCarteiraDTO ativoCarteiraDTO) {
        return ResponseEntity.ok(service.atualizarAtivoCarteira(id, ativoCarteiraDTO));
    }

    @GetMapping(value = "/patrimonio")
    public ResponseEntity<List<PatrimonioCategoriaDTO>> getValorPatrimonial() {
        return ResponseEntity.ok(service.getPatrimonioPorCategoria());
    }


    @GetMapping(value = "/proventos")
    public ResponseEntity<List<ProventosMesDTO>> getProventosMes() {
        return ResponseEntity.ok(service.listarProventos());
    }

    @GetMapping(value = "/top-pagadoras")
    public ResponseEntity<List<TopPagadoraProventosDTO>> getTopPagadoras() {
        return ResponseEntity.ok(service.listarPagadoras());
    }

    @GetMapping(value = "/update-carteira/{id}")
    ResponseEntity<?> updateCarteiraBySubclasse(@PathVariable("id") Integer id) {
        this.service.updateCarteira(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/ibov")
    public ResponseEntity<String> getIbov() {
        return ResponseEntity.ok(rendaVariavelService.getIbov());
    }

}
