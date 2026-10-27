package com.imobiliaria_api.controller;

import com.imobiliaria_api.entity.Imovel;
import com.imobiliaria_api.service.ImovelService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

    @GetMapping("/pesquisar")
    public ResponseEntity<List<Imovel>> pesquisaByExample(
            @RequestParam(required = false) String tipo_imovel,
            @RequestParam(required = false) String endereco,
            @RequestParam(required = false) String cep,
            @RequestParam(required = false) Integer dormitorios,
            @RequestParam(required = false) Integer banheiros,
            @RequestParam(required = false) Integer suites,
            @RequestParam(required = false) Integer metragem) {

        return ResponseEntity.ok(
                imovelService.pesquisaByExample(
                        tipo_imovel,
                        endereco,
                        cep,
                        dormitorios,
                        banheiros,
                        suites,
                        metragem
                )
        );
    }

    @GetMapping("/pagina")
    public Page<Imovel> listarPaginado(
            @PageableDefault(
                    size = 10,
                    sort = "id",
                    direction = Sort.Direction.ASC
            ) Pageable pageable) {
        return imovelService.listarPaginado(pageable);
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