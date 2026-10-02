package com.imobiliaria_api.service;

import com.imobiliaria_api.entity.Cliente;
import com.imobiliaria_api.entity.Imovel;
import com.imobiliaria_api.entity.Locacao;
import com.imobiliaria_api.repository.ClienteRepository;
import com.imobiliaria_api.repository.ImovelRepository;
import com.imobiliaria_api.repository.LocacaoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocacaoService {

    private final LocacaoRepository locacaoRepository;
    private final ClienteRepository clienteRepository;
    private final ImovelRepository imovelRepository;

    public LocacaoService(
            LocacaoRepository locacaoRepository,
            ClienteRepository clienteRepository,
            ImovelRepository imovelRepository) {

        this.locacaoRepository = locacaoRepository;
        this.clienteRepository = clienteRepository;
        this.imovelRepository = imovelRepository;
    }

    public List<Locacao> listarTodos() {
        return locacaoRepository.findAll();
    }

    public Optional<Locacao> buscarPorId(Integer id) {
        return locacaoRepository.findById(id);
    }

    public Locacao salvar(Locacao locacao) {

        if (locacao.getCliente() == null || locacao.getCliente().getId() == null) {
            throw new RuntimeException("Cliente é obrigatório");
        }

        if (locacao.getImovel() == null || locacao.getImovel().getId() == null) {
            throw new RuntimeException("Imóvel é obrigatório");
        }

        Cliente cliente = clienteRepository.findById(locacao.getCliente().getId())
                .orElseThrow(() ->
                        new RuntimeException("Cliente não encontrado"));

        Imovel imovel = imovelRepository.findById(locacao.getImovel().getId())
                .orElseThrow(() ->
                        new RuntimeException("Imóvel não encontrado"));

        locacao.setCliente(cliente);
        locacao.setImovel(imovel);

        return locacaoRepository.save(locacao);
    }

    public void excluir(Integer id) {
        locacaoRepository.deleteById(id);
    }
}