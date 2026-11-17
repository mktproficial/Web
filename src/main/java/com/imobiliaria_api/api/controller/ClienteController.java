package com.imobiliaria_api.api.controller;

import com.imobiliaria_api.api.dto.input.ClienteRequest;
import com.imobiliaria_api.api.dto.output.ClienteResponse;
import com.imobiliaria_api.api.dto.output.PaginaResponse;
import com.imobiliaria_api.api.mapper.ClienteMapper;
import com.imobiliaria_api.entity.Cliente;
import com.imobiliaria_api.service.ClienteService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/clientes")
@Tag(name = "Clientes", description = "Gerenciamento de clientes/inquilinos")
@PreAuthorize("hasAnyRole('ADMIN', 'ATENDENTE')")
public class ClienteController {

    private final ClienteService service;
    private final ClienteMapper mapper;

    public ClienteController(ClienteService service, ClienteMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public PaginaResponse<ClienteResponse> lista(@RequestParam(required = false) String nome,
                                                 @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return PaginaResponse.de(service.listar(nome, pageable).map(mapper::paraResponse));
    }

    @GetMapping("/{id}")
    public ClienteResponse buscaPor(@PathVariable Integer id) {
        return mapper.paraResponse(service.buscarOuFalhar(id));
    }

    @PostMapping
    public ResponseEntity<ClienteResponse> cadastro(@Valid @RequestBody ClienteRequest request,
                                                    UriComponentsBuilder builder) {
        Cliente salvo = service.salvar(mapper.paraEntidade(request));
        URI uri = builder.path("/clientes/{id}").buildAndExpand(salvo.getId()).toUri();
        return ResponseEntity.created(uri).body(mapper.paraResponse(salvo));
    }

    @PutMapping("/{id}")
    public ClienteResponse atualiza(@Valid @RequestBody ClienteRequest request, @PathVariable Integer id) {
        Cliente cliente = service.buscarOuFalhar(id);
        mapper.atualizarEntidade(cliente, request);
        return mapper.paraResponse(service.salvar(cliente));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void remove(@PathVariable Integer id) {
        service.remover(id);
    }
}