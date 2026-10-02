package com.imobiliaria_api.controller;

import com.imobiliaria_api.entity.Locacao;
import com.imobiliaria_api.service.LocacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/locacoes")
public class LocacaoController {

    private final LocacaoService locacaoService;

    public LocacaoController(LocacaoService locacaoService) {
        this.locacaoService = locacaoService;
    }

    // GET /locacoes
    @GetMapping
    public List<Locacao> listarTodos() {
        return locacaoService.listarTodos();
    }

    // GET /locacoes/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Locacao> buscarPorId(@PathVariable Integer id) {
        return locacaoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /locacoes
    @PostMapping
    public ResponseEntity<Locacao> criar(@RequestBody Locacao locacao) {
        Locacao novaLocacao = locacaoService.salvar(locacao);
        return ResponseEntity.ok(novaLocacao);
    }

    // PUT /locacoes/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Locacao> atualizar(
            @PathVariable Integer id,
            @RequestBody Locacao locacao) {

        return locacaoService.buscarPorId(id)
                .map(locacaoExistente -> {
                    locacao.setId(id);
                    Locacao locacaoAtualizada = locacaoService.salvar(locacao);
                    return ResponseEntity.ok(locacaoAtualizada);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /locacoes/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {

        if (locacaoService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        locacaoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}