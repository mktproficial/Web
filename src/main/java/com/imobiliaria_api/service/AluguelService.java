package com.imobiliaria_api.service;

import com.imobiliaria_api.exception.EntidadeNaoEncontradaException;
import com.imobiliaria_api.entity.Aluguel;
import com.imobiliaria_api.repository.AluguelRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AluguelService {

    private final AluguelRepository repository;

    public AluguelService(AluguelRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Page<Aluguel> listar(Integer locacaoId, Integer clienteIdRestricao, Pageable pageable) {
        return repository.buscar(locacaoId, clienteIdRestricao, pageable);
    }

    @Transactional(readOnly = true)
    public Aluguel buscarOuFalhar(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Aluguel não encontrado"));
    }

    @Transactional
    public Aluguel salvar(Aluguel aluguel) {
        return repository.save(aluguel);
    }

    @Transactional
    public void remover(Integer id) {
        Aluguel aluguel = buscarOuFalhar(id);
        repository.delete(aluguel);
    }
}