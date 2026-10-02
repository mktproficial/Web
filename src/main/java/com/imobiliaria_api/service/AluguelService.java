package com.imobiliaria_api.service;

import com.imobiliaria_api.entity.Aluguel;
import com.imobiliaria_api.entity.Locacao;
import com.imobiliaria_api.repository.AluguelRepository;
import com.imobiliaria_api.repository.LocacaoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AluguelService {

    private final AluguelRepository aluguelRepository;
    private final LocacaoRepository locacaoRepository;

    public AluguelService(
            AluguelRepository aluguelRepository,
            LocacaoRepository locacaoRepository) {

        this.aluguelRepository = aluguelRepository;
        this.locacaoRepository = locacaoRepository;
    }

    public List<Aluguel> listarTodos() {
        return aluguelRepository.findAll();
    }

    public Optional<Aluguel> buscarPorId(Integer id) {
        return aluguelRepository.findById(id);
    }

    public Aluguel salvar(Aluguel aluguel) {

        System.out.println("ALUGUEL RECEBIDO: " + aluguel);
        System.out.println("LOCAÇÃO: " + aluguel.getLocacao());

        if (aluguel.getLocacao() == null ||
                aluguel.getLocacao().getId() == null) {

            throw new RuntimeException("Locação é obrigatória");
        }

        Locacao locacao = locacaoRepository
                .findById(aluguel.getLocacao().getId())
                .orElseThrow(() ->
                        new RuntimeException("Locação não encontrada"));

        aluguel.setLocacao(locacao);

        return aluguelRepository.save(aluguel);
    }

    public void excluir(Integer id) {
        aluguelRepository.deleteById(id);
    }
}