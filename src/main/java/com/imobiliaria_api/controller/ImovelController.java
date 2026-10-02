package com.imobiliaria_api.controller;

import com.imobiliaria_api.entity.Imovel;
import com.imobiliaria_api.service.ImovelService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/imoveis")
public class ImovelController {

    private final ImovelService imovelService;

    public ImovelController(ImovelService imovelService) {
        this.imovelService = imovelService;
    }

    @GetMapping
    public ResponseEntity<List<Imovel>> listarTodos() {
        return ResponseEntity.ok(imovelService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Imovel>> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(imovelService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Imovel> criar(@Valid @RequestBody Imovel imovel) {
        return ResponseEntity.ok(imovelService.salvar(imovel));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Imovel> atualizar(
            @PathVariable Integer id,
            @Valid @RequestBody Imovel imovel) {

        return ResponseEntity.ok(imovelService.atualizar(id, imovel));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        imovelService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}