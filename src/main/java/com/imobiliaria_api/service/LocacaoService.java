package com.imobiliaria_api.service;

import com.imobiliaria_api.exception.EntidadeNaoEncontradaException;
import com.imobiliaria_api.exception.NegocioException;
import com.imobiliaria_api.entity.Locacao;
import com.imobiliaria_api.repository.LocacaoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LocacaoService {

    private final LocacaoRepository repository;

    public LocacaoService(LocacaoRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Page<Locacao> listar(Integer clienteIdRestricao, String nomeInquilino, Boolean ativo, Pageable pageable) {
        return repository.buscar(clienteIdRestricao, nomeInquilino, ativo, pageable);
    }

    @Transactional(readOnly = true)
    public Locacao buscarOuFalhar(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Locação não encontrada"));
    }

    @Transactional
    public Locacao salvar(Locacao locacao) {
        validarPeriodo(locacao);

        if (locacao.getAtivo() == null) {
            locacao.setAtivo(true);
        }

        if (Boolean.TRUE.equals(locacao.getAtivo())) {
            validarDisponibilidade(locacao);
        }

        return repository.save(locacao);
    }

    @Transactional
    public Locacao alterarAtivacao(Integer id, boolean ativo) {
        Locacao locacao = buscarOuFalhar(id);

        if (ativo) {
            validarDisponibilidade(locacao);
        }

        locacao.setAtivo(ativo);
        return locacao;
    }

    @Transactional
    public void remover(Integer id) {
        Locacao locacao = buscarOuFalhar(id);
        repository.delete(locacao);
    }

    private void validarDisponibilidade(Locacao locacao) {
        if (repository.existeSobreposicao(
                locacao.getImovel().getId(),
                locacao.getDataInicio(),
                locacao.getDataFim(),
                locacao.getId())) {
            throw new NegocioException("O imóvel já possui uma locação ativa no período informado");
        }
    }

    private void validarPeriodo(Locacao locacao) {
        if (locacao.getDataInicio() == null || locacao.getDataFim() == null) {
            throw new NegocioException("As datas de início e fim são obrigatórias");
        }
        if (!locacao.getDataFim().isAfter(locacao.getDataInicio())) {
            throw new NegocioException("A data de fim deve ser posterior à data de início");
        }
    }
}