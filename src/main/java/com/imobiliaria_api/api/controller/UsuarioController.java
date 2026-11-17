package com.imobiliaria_api.api.controller;

import com.imobiliaria_api.api.dto.input.UsuarioRequest;
import com.imobiliaria_api.api.dto.input.UsuarioUpdateRequest;
import com.imobiliaria_api.api.dto.output.PaginaResponse;
import com.imobiliaria_api.api.dto.output.UsuarioResponse;
import com.imobiliaria_api.api.mapper.UsuarioMapper;
import com.imobiliaria_api.entity.Usuario;
import com.imobiliaria_api.service.UsuarioService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuários", description = "Gerenciamento de usuários e seus perfis (ADMIN)")
@PreAuthorize("hasRole('ADMIN')")
public class UsuarioController {

    private final UsuarioService service;
    private final UsuarioMapper mapper;

    public UsuarioController(UsuarioService service, UsuarioMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public PaginaResponse<UsuarioResponse> lista(@PageableDefault(size = 10, sort = "login") Pageable pageable) {
        return PaginaResponse.de(service.listar(pageable).map(mapper::paraResponse));
    }

    @GetMapping("/{id}")
    public UsuarioResponse buscaPor(@PathVariable Integer id) {
        return mapper.paraResponse(service.buscarOuFalhar(id));
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> cadastro(@Valid @RequestBody UsuarioRequest request,
                                                    UriComponentsBuilder builder) {
        Usuario salvo = service.criar(request);
        URI uri = builder.path("/usuarios/{id}").buildAndExpand(salvo.getId()).toUri();
        return ResponseEntity.created(uri).body(mapper.paraResponse(salvo));
    }

    @PutMapping("/{id}")
    public UsuarioResponse atualiza(@Valid @RequestBody UsuarioUpdateRequest request, @PathVariable Integer id) {
        return mapper.paraResponse(service.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable Integer id) {
        service.remover(id);
    }
}