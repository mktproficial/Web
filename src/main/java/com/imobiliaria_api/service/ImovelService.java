package com.imobiliaria_api.service;

import com.imobiliaria_api.entity.Imovel;
import com.imobiliaria_api.repository.ImovelRepository;
import com.imobiliaria_api.repository.LocacaoRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ImovelService {

    private final ImovelRepository imovelRepository;
    private final LocacaoRepository locacaoRepository;

    public ImovelService(
            ImovelRepository imovelRepository,
            LocacaoRepository locacaoRepository) {
        this.imovelRepository = imovelRepository;
        this.locacaoRepository = locacaoRepository;
    }

    public List<Imovel> listarTodos() {
        return imovelRepository.findAll();
    }

    public Optional<Imovel> buscarPorId(Integer id) {
        return imovelRepository.findById(id);
    }

    public Page<Imovel> listarPaginado(Pageable pageable) {
        return imovelRepository.findAll(pageable);
    }


    public List<Imovel> pesquisaByExample(
            String tipo_imovel,
            String endereco,
            String cep,
            Integer dormitorios,
            Integer banheiros,
            Integer suites,
            Integer metragem) {

        var imovel = new Imovel();

        imovel.setTipo_imovel(tipo_imovel);
        imovel.setEndereco(endereco);
        imovel.setCep(cep);
        imovel.setDormitorios(dormitorios);
        imovel.setBanheiros(banheiros);
        imovel.setSuites(suites);
        imovel.setMetragem(metragem);

        ExampleMatcher matcher = ExampleMatcher
                .matchingAll()
                .withIgnoreNullValues()
                .withIgnoreCase()
                .withStringMatcher(
                        ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnorePaths("id", "valorAluguelSug", "obs");

        Example<Imovel> imovelExample = Example.of(imovel, matcher);

        return imovelRepository.findAll(imovelExample);
    }


    public Imovel salvar(Imovel imovel) {
        return imovelRepository.save(imovel);
    }


    public void excluir(Integer id) {

        Imovel imovel = imovelRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Imóvel não encontrado"));

        boolean possuiLocacaoAtivaOuFutura =
                locacaoRepository
                        .existsByImovel_IdAndAtivoTrueAndDataFimGreaterThanEqual(
                                id, LocalDate.now());

        if (possuiLocacaoAtivaOuFutura) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Não é possível excluir o imóvel: "
                            + "existe uma locação ativa ou futura vinculada a ele");
        }

        imovelRepository.delete(imovel);
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