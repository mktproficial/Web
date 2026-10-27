package com.imobiliaria_api.service;

import com.imobiliaria_api.entity.Cliente;
import com.imobiliaria_api.entity.Imovel;
import com.imobiliaria_api.entity.Locacao;
import com.imobiliaria_api.repository.ClienteRepository;
import com.imobiliaria_api.repository.ImovelRepository;
import com.imobiliaria_api.repository.LocacaoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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

        if (locacao.getCliente() == null ||
                locacao.getCliente().getId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Cliente é obrigatório");
        }

        if (locacao.getImovel() == null ||
                locacao.getImovel().getId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Imóvel é obrigatório");
        }

        LocalDate inicio = locacao.getDataInicio();
        LocalDate fim = locacao.getDataFim();

        if (inicio == null || fim == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "As datas de início e fim são obrigatórias");
        }

        if (!fim.isAfter(inicio)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A data final deve ser posterior à data inicial");
        }

        Cliente cliente = clienteRepository.findById(
                locacao.getCliente().getId()
        ).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Cliente não encontrado"));

        Imovel imovel = imovelRepository.findById(
                locacao.getImovel().getId()
        ).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Imóvel não encontrado"));

        BigDecimal valorDiaria = imovel.getValorAluguelSug();

        if (valorDiaria == null ||
                valorDiaria.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O imóvel não possui um valor de diária válido");
        }

        long diarias = ChronoUnit.DAYS.between(inicio, fim);

        BigDecimal valorTotal = valorDiaria.multiply(
                BigDecimal.valueOf(diarias)
        );

        // O valor enviado pelo cliente é ignorado.
        locacao.setValorAluguel(valorTotal);
        locacao.setCliente(cliente);
        locacao.setImovel(imovel);

        // Ao cadastrar ou confirmar, verificar disponibilidade.
        if (Boolean.TRUE.equals(locacao.getAtivo())) {
            boolean conflito = locacaoRepository.existeConflito(
                    imovel.getId(),
                    inicio,
                    fim,
                    locacao.getId()
            );

            if (conflito) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "O imóvel já possui uma locação confirmada "
                                + "para o período solicitado");
            }
        }

        return locacaoRepository.save(locacao);
    }


    public void excluir(Integer id) {
        locacaoRepository.deleteById(id);
    }
}