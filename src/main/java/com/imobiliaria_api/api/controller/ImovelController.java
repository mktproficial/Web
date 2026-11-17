package com.imobiliaria_api.api.controller;

import com.imobiliaria_api.api.dto.input.ImovelRequest;
import com.imobiliaria_api.api.dto.output.ImovelResponse;
import com.imobiliaria_api.api.dto.output.PaginaResponse;
import com.imobiliaria_api.api.mapper.ImovelMapper;
import com.imobiliaria_api.filtro.ImovelFiltro;
import com.imobiliaria_api.entity.Imovel;
import com.imobiliaria_api.service.ImovelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
@RequestMapping("/imoveis")
@Tag(name = "Imóveis", description = "Gerenciamento e pesquisa de imóveis")
public class ImovelController {

    private final ImovelService service;
    private final ImovelMapper mapper;

    public ImovelController(ImovelService service, ImovelMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Lista imóveis com paginação e pesquisa por exemplo",
            description = "Todos os campos do filtro são opcionais e combinados com AND")
    public PaginaResponse<ImovelResponse> lista(@ModelAttribute ImovelFiltro filtro,
                                                @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return PaginaResponse.de(service.listar(filtro, pageable).map(mapper::paraResponse));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ImovelResponse buscaPor(@PathVariable Integer id) {
        return mapper.paraResponse(service.buscarOuFalhar(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ATENDENTE')")
    public ResponseEntity<ImovelResponse> cadastro(@Valid @RequestBody ImovelRequest request,
                                                   UriComponentsBuilder builder) {
        Imovel salvo = service.salvar(mapper.paraEntidade(request));
        URI uri = builder.path("/imoveis/{id}").buildAndExpand(salvo.getId()).toUri();
        return ResponseEntity.created(uri).body(mapper.paraResponse(salvo));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ATENDENTE')")
    public ImovelResponse atualiza(@Valid @RequestBody ImovelRequest request, @PathVariable Integer id) {
        Imovel imovel = service.buscarOuFalhar(id);
        mapper.atualizarEntidade(imovel, request);
        return mapper.paraResponse(service.salvar(imovel));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void remove(@PathVariable Integer id) {
        service.remover(id);
    }
}