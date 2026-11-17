package com.imobiliaria_api.api.controller;

import com.imobiliaria_api.api.dto.input.AluguelRequest;
import com.imobiliaria_api.api.dto.output.AluguelResponse;
import com.imobiliaria_api.api.dto.output.PaginaResponse;
import com.imobiliaria_api.api.mapper.AluguelMapper;
import com.imobiliaria_api.entity.Aluguel;
import com.imobiliaria_api.entity.Locacao;
import com.imobiliaria_api.service.AluguelService;
import com.imobiliaria_api.service.LocacaoService;
import com.imobiliaria_api.security.AutorizacaoService;
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
@RequestMapping("/alugueis")
@Tag(name = "Aluguéis", description = "Gerenciamento de pagamentos mensais de locação")
@PreAuthorize("hasAnyRole('ADMIN', 'ATENDENTE', 'CLIENTE')")
public class AluguelController {

    private final AluguelService service;
    private final LocacaoService locacaoService;
    private final AluguelMapper mapper;
    private final AutorizacaoService autorizacaoService;

    public AluguelController(AluguelService service,
                             LocacaoService locacaoService,
                             AluguelMapper mapper,
                             AutorizacaoService autorizacaoService) {
        this.service = service;
        this.locacaoService = locacaoService;
        this.mapper = mapper;
        this.autorizacaoService = autorizacaoService;
    }

    @GetMapping
    public PaginaResponse<AluguelResponse> lista(@RequestParam(required = false) Integer locacaoId,
                                                 @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        Integer clienteIdRestricao = autorizacaoService.clienteIdRestricao();
        return PaginaResponse.de(service
                .listar(locacaoId, clienteIdRestricao, pageable)
                .map(mapper::paraResponse));
    }

    @GetMapping("/{id}")
    @PreAuthorize("@autorizacao.podeAcessarAluguel(#id)")
    public AluguelResponse buscaPor(@PathVariable Integer id) {
        return mapper.paraResponse(service.buscarOuFalhar(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ATENDENTE')")
    public ResponseEntity<AluguelResponse> cadastro(@Valid @RequestBody AluguelRequest request,
                                                    UriComponentsBuilder builder) {
        Locacao locacao = locacaoService.buscarOuFalhar(request.locacaoId());
        Aluguel salvo = service.salvar(mapper.paraEntidade(request, locacao));
        URI uri = builder.path("/alugueis/{id}").buildAndExpand(salvo.getId()).toUri();
        return ResponseEntity.created(uri).body(mapper.paraResponse(salvo));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ATENDENTE')")
    public AluguelResponse atualiza(@Valid @RequestBody AluguelRequest request, @PathVariable Integer id) {
        Aluguel aluguel = service.buscarOuFalhar(id);
        Locacao locacao = locacaoService.buscarOuFalhar(request.locacaoId());
        mapper.atualizarEntidade(aluguel, request, locacao);
        return mapper.paraResponse(service.salvar(aluguel));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void remove(@PathVariable Integer id) {
        service.remover(id);
    }
}