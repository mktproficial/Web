package com.imobiliaria_api.api.controller;

import com.imobiliaria_api.api.dto.input.RegistroRequest;
import com.imobiliaria_api.api.dto.output.UsuarioResponse;
import com.imobiliaria_api.api.mapper.UsuarioMapper;
import com.imobiliaria_api.entity.Usuario;
import com.imobiliaria_api.service.UsuarioService;
import com.imobiliaria_api.security.AutorizacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Registro público e dados do usuário autenticado")
public class AuthController {

    private final UsuarioService service;
    private final UsuarioMapper mapper;
    private final AutorizacaoService autorizacaoService;

    public AuthController(UsuarioService service,
                          UsuarioMapper mapper,
                          AutorizacaoService autorizacaoService) {
        this.service = service;
        this.mapper = mapper;
        this.autorizacaoService = autorizacaoService;
    }

    @PostMapping("/registro")
    @Operation(summary = "Registra um usuário CLIENTE sem vínculo com cliente",
            description = "Endpoint público que cria sempre o perfil CLIENTE. O vínculo com um cliente e a concessão de outros perfis são feitos por um ADMIN em /usuarios")
    public ResponseEntity<UsuarioResponse> registro(@Valid @RequestBody RegistroRequest request) {
        Usuario usuario = service.registrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.paraResponse(usuario));
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public UsuarioResponse me() {
        return mapper.paraResponse(service.buscarOuFalhar(autorizacaoService.idUsuarioAtual()));
    }
}