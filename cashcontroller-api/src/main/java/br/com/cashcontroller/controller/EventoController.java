package br.com.cashcontroller.controller;

import br.com.cashcontroller.dto.EventoAddRendaVariavelDTO;
import br.com.cashcontroller.dto.EventoListRendaVariavelDTO;
import br.com.cashcontroller.dto.TipoEventoDTO;
import br.com.cashcontroller.service.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/eventos")
public class EventoController {

	@Autowired
	private EventoService eventoService;
	@PostMapping
	ResponseEntity<EventoAddRendaVariavelDTO> cadastrarAtivo(@RequestBody EventoAddRendaVariavelDTO eventoDTO) {
		return ResponseEntity.ok(this.eventoService.cadastrarEvento(eventoDTO));
	}
	
//	@PutMapping
//	ResponseEntity<AtivoDTO> editarAtivo(@RequestBody AtivoDTO ativoDto) {
//		return ResponseEntity.ok(this.ativoService.atualizarAtivo(ativoDto));
//	}
//
//	@DeleteMapping(value = "/{id}")
//	ResponseEntity<?> excluirAtivo(@PathVariable(value="id") Integer id) {
//		this.ativoService.excluirAtivo(id);
//		return ResponseEntity.noContent().build();
//	}
//
//
	@GetMapping("/tipo-eventos")
	ResponseEntity<List<TipoEventoDTO>> getTipoEventos() {
		List<TipoEventoDTO> subclasses = this.eventoService.listarTipoEventos();
		return ResponseEntity.ok(subclasses);
	}

	@GetMapping
	ResponseEntity<List<EventoListRendaVariavelDTO>> getAtivos() {
		List<EventoListRendaVariavelDTO> ativos = this.eventoService.listarEventos();
		return ResponseEntity.ok(ativos);
	}
//
//	@GetMapping(value = "/por-classe/{id}")
//	ResponseEntity<List<AtivoDTO>> getAtivosPorClasse(@PathVariable(value="id")  Integer id) {
//		List<AtivoDTO> ativos = this.ativoService.listarAtivosPorClasse(id);
//		return ResponseEntity.ok(ativos);
//	}
	
}
