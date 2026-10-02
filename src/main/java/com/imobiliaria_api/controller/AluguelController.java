package com.imobiliaria_api.controller;

import com.imobiliaria_api.entity.Aluguel;
import com.imobiliaria_api.service.AluguelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alugueis")
public class AluguelController {

    private final AluguelService aluguelService;

    public AluguelController(AluguelService aluguelService) {
        this.aluguelService = aluguelService;
    }

    // GET /alugueis
    @GetMapping
    public List<Aluguel> listarTodos() {
        return aluguelService.listarTodos();
    }

    // GET /alugueis/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Aluguel> buscarPorId(@PathVariable Integer id) {
        return aluguelService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /alugueis
    @PostMapping
    public ResponseEntity<Aluguel> criar(@RequestBody Aluguel aluguel) {
        Aluguel novoAluguel = aluguelService.salvar(aluguel);
        return ResponseEntity.ok(novoAluguel);
    }

    // PUT /alugueis/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Aluguel> atualizar(
            @PathVariable Integer id,
            @RequestBody Aluguel aluguel) {

        return aluguelService.buscarPorId(id)
                .map(aluguelExistente -> {
                    aluguel.setId(id);
                    Aluguel aluguelAtualizado = aluguelService.salvar(aluguel);
                    return ResponseEntity.ok(aluguelAtualizado);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /alugueis/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {

        if (aluguelService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        aluguelService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}