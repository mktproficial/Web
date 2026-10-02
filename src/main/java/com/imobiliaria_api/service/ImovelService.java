package com.imobiliaria_api.service;

import com.imobiliaria_api.entity.Imovel;
import com.imobiliaria_api.repository.ImovelRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ImovelService {

    private final ImovelRepository imovelRepository;

    public ImovelService(ImovelRepository imovelRepository) {
        this.imovelRepository = imovelRepository;
    }

    public List<Imovel> listarTodos() {
        return imovelRepository.findAll();
    }

    public Optional<Imovel> buscarPorId(Integer id) {
        return imovelRepository.findById(id);
    }

    public Imovel salvar(Imovel imovel) {
        return imovelRepository.save(imovel);
    }

    public void excluir(Integer id) {
        imovelRepository.deleteById(id);
    }

    public Imovel atualizar(Integer id, @Valid Imovel imovel) {
        Imovel imovelExistente = imovelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Imovel não encontrado"));

        imovelExistente.setTipo_imovel(imovel.getTipo_imovel());
        imovelExistente.setEndereco(imovel.getEndereco());
        imovelExistente.setCep(imovel.getCep());
        imovelExistente.setDormitorios(imovel.getDormitorios());
        imovelExistente.setBanheiros(imovel.getBanheiros());
        imovelExistente.setSuites(imovel.getSuites());
        imovelExistente.setMetragem(imovel.getMetragem());
        imovelExistente.setValorAluguelSug(imovel.getValorAluguelSug());
        imovelExistente.setObs(imovel.getObs());
        return imovelRepository.save(imovelExistente);
    }
}