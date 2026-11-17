package com.imobiliaria_api.api.mapper;

import com.imobiliaria_api.api.dto.input.AluguelRequest;
import com.imobiliaria_api.api.dto.output.AluguelResponse;
import com.imobiliaria_api.entity.Aluguel;
import com.imobiliaria_api.entity.Locacao;
import org.springframework.stereotype.Component;

@Component
public class AluguelMapper {

    public Aluguel paraEntidade(AluguelRequest request, Locacao locacao) {
        Aluguel aluguel = new Aluguel();
        atualizarEntidade(aluguel, request, locacao);
        return aluguel;
    }

    public void atualizarEntidade(Aluguel aluguel, AluguelRequest request, Locacao locacao) {
        aluguel.setLocacao(locacao);
        aluguel.setDtVencimento(request.dtVencimento());
        aluguel.setValorPago(request.valorPago());
        aluguel.setObs(request.obs());
    }

    public AluguelResponse paraResponse(Aluguel aluguel) {
        return new AluguelResponse(
                aluguel.getId(),
                aluguel.getLocacao().getId(),
                aluguel.getDtVencimento(),
                aluguel.getValorPago(),
                aluguel.getObs()
        );
    }
}