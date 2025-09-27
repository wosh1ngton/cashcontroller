package br.com.cashcontroller.controller;

import br.com.cashcontroller.dto.AlterarSenhaDTO;
import br.com.cashcontroller.model.User;
import br.com.cashcontroller.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuariosController {

    @Autowired
    UserDetailsServiceImpl userDetailsService;
    @GetMapping
    public ResponseEntity<List<User>> listarUsuarios() {
        var usuarios = userDetailsService.listarUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userDetailsService.findById(id));
    }

    @PostMapping("/alterar-senha")
    public ResponseEntity<String> alterarSenha(@RequestBody() AlterarSenhaDTO user) {
            String result = userDetailsService.alterarSenha(user);
            return ResponseEntity.ok(result);
    }
}
