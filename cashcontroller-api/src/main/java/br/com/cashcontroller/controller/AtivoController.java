package br.com.cashcontroller.controller;

import java.util.List;

import br.com.cashcontroller.dto.AtivoAddDTO;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.cashcontroller.dto.AtivoDTO;
import br.com.cashcontroller.dto.SubclasseAtivoDTO;
import br.com.cashcontroller.service.AtivoService;


@RestController
@RequestMapping("/api/ativos")
public class AtivoController {

	@Autowired
	private AtivoService ativoService;

	@PostMapping("/em-lote")
	ResponseEntity<List<AtivoAddDTO>> cadastrarAtivos(@RequestBody List<AtivoAddDTO> ativosDto) {
		ativosDto.forEach(ativoDto -> this.ativoService.cadastrarAtivo(ativoDto));
		return ResponseEntity.ok(ativosDto);
	}
	@PostMapping
	ResponseEntity<AtivoAddDTO> cadastrarAtivo(@RequestBody AtivoAddDTO ativoAddDto) {
		return ResponseEntity.ok(this.ativoService.cadastrarAtivo(ativoAddDto));
	}
	
	@PutMapping
	ResponseEntity<AtivoAddDTO> editarAtivo(@RequestBody AtivoAddDTO ativoAddDto) {
		return ResponseEntity.ok(this.ativoService.atualizarAtivo(ativoAddDto));
	}

	@DeleteMapping(value = "/{id}")
	ResponseEntity<?> excluirAtivo(@PathVariable(value="id") Integer id) {
		this.ativoService.excluirAtivo(id);
		return ResponseEntity.noContent().build();
	}
	
	
	@GetMapping("/subclasses")
	ResponseEntity<List<SubclasseAtivoDTO>> getSubClasseAtivos() {
		List<SubclasseAtivoDTO> subclasses = this.ativoService.listarSubclasseAtivos();
		return ResponseEntity.ok(subclasses);
	}
	
	@GetMapping
	ResponseEntity<List<AtivoDTO>> getAtivos() {
		List<AtivoDTO> ativos = this.ativoService.listarAtivos();		
		return ResponseEntity.ok(ativos);
	}

	@GetMapping(value = "/{id}")
	ResponseEntity<AtivoDTO> findById(@PathVariable(value="id") Integer id) {
		AtivoDTO ativo = this.ativoService.findById(id);
		return ResponseEntity.ok(ativo);
	}

	@GetMapping(value = "/por-classe/{id}")
	ResponseEntity<List<AtivoDTO>> getAtivosPorClasse(@PathVariable(value="id")  Integer id) {
		List<AtivoDTO> ativos = this.ativoService.listarAtivosPorClasse(id);
		return ResponseEntity.ok(ativos);
	}
	
}
