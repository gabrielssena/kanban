package com.example.kanban.controller;

import com.example.kanban.dto.LoginRequestDTO;
import com.example.kanban.model.Usuario;
import com.example.kanban.repository.UsuarioRepository;
import com.example.kanban.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @Autowired
    public AuthController(UsuarioRepository repository, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequestDTO body) {
        Usuario usuario = repository.findByUsername(body.username())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));

        if (passwordEncoder.matches(body.password(), usuario.getPassword())) {
            String token = tokenService.gerarToken(usuario);
            return ResponseEntity.ok().body("Bearer " + token);
        }

        return ResponseEntity.badRequest().body("Senha inválida");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody LoginRequestDTO body) {
        if (repository.findByUsername(body.username()).isPresent()) {
            return ResponseEntity.badRequest().body("Usuário já existe");
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(body.username());
        usuario.setPassword(passwordEncoder.encode(body.password()));
        repository.save(usuario);

        return ResponseEntity.ok("Usuário registrado com sucesso");
    }

}
