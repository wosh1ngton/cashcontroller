package br.com.cashcontroller.controller;

import java.util.List;

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
	
	
	@PostMapping
	ResponseEntity<AtivoDTO> cadastrarAtivo(@RequestBody AtivoDTO ativoDto) {		
		return ResponseEntity.ok(this.ativoService.cadastrarAtivo(ativoDto));
	}
	
	@PutMapping
	ResponseEntity<AtivoDTO> editarAtivo(@RequestBody AtivoDTO ativoDto) {		
		return ResponseEntity.ok(this.ativoService.atualizarAtivo(ativoDto));
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
	
}
