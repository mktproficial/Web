package com.imobiliaria_api.api.controller;

import com.imobiliaria_api.api.dto.input.LocacaoRequest;
import com.imobiliaria_api.api.dto.output.LocacaoResponse;
import com.imobiliaria_api.api.dto.output.PaginaResponse;
import com.imobiliaria_api.api.mapper.LocacaoMapper;
import com.imobiliaria_api.entity.Cliente;
import com.imobiliaria_api.entity.Imovel;
import com.imobiliaria_api.entity.Locacao;
import com.imobiliaria_api.service.ClienteService;
import com.imobiliaria_api.service.ImovelService;
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
import org.springframework.web.bind.annotation.PatchMapping;
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
@RequestMapping("/locacoes")
@Tag(name = "Locações", description = "Gerenciamento de contratos de locação")
@PreAuthorize("hasAnyRole('ADMIN', 'ATENDENTE', 'CLIENTE')")
public class LocacaoController {

    private final LocacaoService service;
    private final ImovelService imovelService;
    private final ClienteService clienteService;
    private final LocacaoMapper mapper;
    private final AutorizacaoService autorizacaoService;

    public LocacaoController(LocacaoService service,
                             ImovelService imovelService,
                             ClienteService clienteService,
                             LocacaoMapper mapper,
                             AutorizacaoService autorizacaoService) {
        this.service = service;
        this.imovelService = imovelService;
        this.clienteService = clienteService;
        this.mapper = mapper;
        this.autorizacaoService = autorizacaoService;
    }

    @GetMapping
    public PaginaResponse<LocacaoResponse> lista(@RequestParam(required = false) String nomeInquilino,
                                                 @RequestParam(required = false) Boolean ativo,
                                                 @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        Integer clienteIdRestricao = autorizacaoService.clienteIdRestricao();
        return PaginaResponse.de(service
                .listar(clienteIdRestricao, nomeInquilino, ativo, pageable)
                .map(mapper::paraResponse));
    }

    @GetMapping("/{id}")
    @PreAuthorize("@autorizacao.podeAcessarLocacao(#id)")
    public LocacaoResponse buscaPor(@PathVariable Integer id) {
        return mapper.paraResponse(service.buscarOuFalhar(id));
    }

    @PostMapping
    @PreAuthorize("@autorizacao.podeCadastrarLocacao(#request.inquilinoId())")
    public ResponseEntity<LocacaoResponse> cadastro(@Valid @RequestBody LocacaoRequest request,
                                                    UriComponentsBuilder builder) {
        Imovel imovel = imovelService.buscarOuFalhar(request.imovelId());
        Cliente inquilino = clienteService.buscarOuFalhar(request.inquilinoId());

        Locacao locacao = mapper.paraEntidade(request, imovel, inquilino);

        if (autorizacaoService.isCliente()) {
            locacao.setAtivo(false);
            locacao.setValorAluguel(imovel.getValorAluguelSug());
        }

        Locacao salva = service.salvar(locacao);
        URI uri = builder.path("/locacoes/{id}").buildAndExpand(salva.getId()).toUri();
        return ResponseEntity.created(uri).body(mapper.paraResponse(salva));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ATENDENTE')")
    public LocacaoResponse atualiza(@Valid @RequestBody LocacaoRequest request, @PathVariable Integer id) {
        Locacao locacao = service.buscarOuFalhar(id);
        Imovel imovel = imovelService.buscarOuFalhar(request.imovelId());
        Cliente inquilino = clienteService.buscarOuFalhar(request.inquilinoId());

        mapper.atualizarEntidade(locacao, request, imovel, inquilino);
        return mapper.paraResponse(service.salvar(locacao));
    }

    @PatchMapping("/{id}/ativacao")
    @PreAuthorize("hasAnyRole('ADMIN', 'ATENDENTE')")
    public LocacaoResponse alterarAtivacao(@PathVariable Integer id, @RequestParam Boolean ativo) {
        return mapper.paraResponse(service.alterarAtivacao(id, ativo));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void remove(@PathVariable Integer id) {
        service.remover(id);
    }
}