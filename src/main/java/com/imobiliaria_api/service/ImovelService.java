package com.imobiliaria_api.service;

import com.imobiliaria_api.filtro.ImovelFiltro;
import com.imobiliaria_api.exception.EntidadeNaoEncontradaException;
import com.imobiliaria_api.entity.Imovel;
import com.imobiliaria_api.repository.ImovelRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ImovelService {

    private final ImovelRepository repository;

    public ImovelService(ImovelRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Page<Imovel> listar(ImovelFiltro filtro, Pageable pageable) {
        Imovel probe = new Imovel();
        probe.setTipoImovel(filtro.getTipoImovel());
        probe.setEndereco(filtro.getEndereco());
        probe.setCep(filtro.getCep());
        probe.setDormitorios(filtro.getDormitorios());
        probe.setBanheiros(filtro.getBanheiros());
        probe.setSuites(filtro.getSuites());
        probe.setMetragem(filtro.getMetragem());
        probe.setValorAluguelSug(filtro.getValorAluguelSug());
        probe.setObs(filtro.getObs());

        ExampleMatcher matcher = ExampleMatcher.matchingAll()
                .withIgnoreNullValues()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withMatcher("cep", ExampleMatcher.GenericPropertyMatchers.exact());

        return repository.findAll(Example.of(probe, matcher), pageable);
    }

    @Transactional(readOnly = true)
    public Imovel buscarOuFalhar(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Imóvel não encontrado"));
    }

    @Transactional
    public Imovel salvar(Imovel imovel) {
        return repository.save(imovel);
    }

    @Transactional
    public void remover(Integer id) {
        Imovel imovel = buscarOuFalhar(id);
        repository.delete(imovel);
    }
}