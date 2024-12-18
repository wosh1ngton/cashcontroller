package br.com.cashcontroller.controller;

import br.com.cashcontroller.dto.*;
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

	@PostMapping("/renda-fixa")
	ResponseEntity<?> cadastrarEventoRendaFixa(@RequestBody EventoAddRendaFixaDTO eventoDTO) {
		this.eventoService.cadastrarEvento(eventoDTO);
		return ResponseEntity.noContent().build();
	}
	@PostMapping
	ResponseEntity<?> cadastrarEvento(@RequestBody EventoAddRendaVariavelDTO eventoDTO, @RequestParam String periodosDeRecorrencia) {
		this.eventoService.cadastrarEvento(eventoDTO,periodosDeRecorrencia);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping
	ResponseEntity<EventoAddRendaVariavelDTO> editarEvento(@RequestBody EventoAddRendaVariavelDTO eventoDto) {
		return ResponseEntity.ok(this.eventoService.atualizarEvento(eventoDto));
	}

	@PutMapping("/renda-fixa")
	ResponseEntity<EventoAddRendaFixaDTO> editarEventoRendaFixa(@RequestBody EventoAddRendaFixaDTO eventoDto) {
		return ResponseEntity.ok(this.eventoService.atualizarEventoRendaFixa(eventoDto));
	}

	@DeleteMapping(value = "/{id}")
	ResponseEntity<?> excluirEvento(@PathVariable(value="id") Integer id) {
		this.eventoService.excluirEvento(id);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping(value = "/renda-fixa/{id}")
	ResponseEntity<?> excluirEventoRendaFixa(@PathVariable(value="id") Integer id) {
		this.eventoService.excluirEventoRendaFixa(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/tipo-eventos")
	ResponseEntity<List<TipoEventoDTO>> getTipoEventos() {
		List<TipoEventoDTO> subclasses = this.eventoService.listarTipoEventos();
		return ResponseEntity.ok(subclasses);
	}

	@GetMapping
	ResponseEntity<List<EventoListRendaVariavelDTO>> getEventos() {
		List<EventoListRendaVariavelDTO> ativos = this.eventoService.listarEventos();
		return ResponseEntity.ok(ativos);
	}

	@PostMapping("/filter")
	public ResponseEntity<List<EventoListRendaVariavelDTO>> filter(@RequestBody Filter filter) {

		List<EventoListRendaVariavelDTO> operacoes = eventoService.listarEventosRendaVariavelPorData(filter);
		return ResponseEntity.ok(operacoes);
	}

	@PostMapping("/filter/renda-fixa")
	public ResponseEntity<List<EventoListRendaFixaDTO>> filterRendaFixa(@RequestBody Filter filter) {

		List<EventoListRendaFixaDTO> operacoes = eventoService.listarEventosRendaFixaPorData(filter);
		return ResponseEntity.ok(operacoes);
	}

	@PostMapping("/parametro-fii")
	ResponseEntity<ParametroEventoFIIAddDTO> cadastrarParametroFII(@RequestBody ParametroEventoFIIAddDTO parametroDto) {
		return ResponseEntity.ok(this.eventoService.cadastrarParametro(parametroDto));
	}
}
