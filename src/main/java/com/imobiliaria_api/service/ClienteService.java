package com.imobiliaria_api.service;

import com.imobiliaria_api.exception.EntidadeNaoEncontradaException;
import com.imobiliaria_api.exception.NegocioException;
import com.imobiliaria_api.entity.Cliente;
import com.imobiliaria_api.repository.ClienteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class ClienteService {

    private final ClienteRepository repository;

    public ClienteService(ClienteRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Page<Cliente> listar(String nome, Pageable pageable) {
        if (StringUtils.hasText(nome)) {
            return repository.findByNomeContainingIgnoreCase(nome, pageable);
        }
        return repository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Cliente buscarOuFalhar(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Cliente não encontrado"));
    }

    @Transactional
    public Cliente salvar(Cliente cliente) {
        boolean cpfEmUso = cliente.getId() == null
                ? repository.existsByCpf(cliente.getCpf())
                : repository.existsByCpfAndIdNot(cliente.getCpf(), cliente.getId());

        if (cpfEmUso) {
            throw new NegocioException("Já existe um cliente cadastrado com este CPF");
        }

        return repository.save(cliente);
    }

    @Transactional
    public void remover(Integer id) {
        Cliente cliente = buscarOuFalhar(id);
        repository.delete(cliente);
    }
}