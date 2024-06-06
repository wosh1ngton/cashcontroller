package br.com.cashcontroller.controller;

import br.com.cashcontroller.dto.IndexadorDTO;
import br.com.cashcontroller.service.IndexadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/indexadores")
public class IndexadorController {

    @Autowired
    IndexadorService service;
    @GetMapping
    public ResponseEntity<List<IndexadorDTO>> listarIndexadores() {
        return  ResponseEntity.ok(service.listarIndexadores());
    }
}
