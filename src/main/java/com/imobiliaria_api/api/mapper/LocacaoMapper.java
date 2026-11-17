package com.imobiliaria_api.api.mapper;

import com.imobiliaria_api.api.dto.input.LocacaoRequest;
import com.imobiliaria_api.api.dto.output.LocacaoResponse;
import com.imobiliaria_api.entity.Cliente;
import com.imobiliaria_api.entity.Imovel;
import com.imobiliaria_api.entity.Locacao;
import org.springframework.stereotype.Component;

@Component
public class LocacaoMapper {

    private final ImovelMapper imovelMapper;
    private final ClienteMapper clienteMapper;

    public LocacaoMapper(ImovelMapper imovelMapper, ClienteMapper clienteMapper) {
        this.imovelMapper = imovelMapper;
        this.clienteMapper = clienteMapper;
    }

    public Locacao paraEntidade(LocacaoRequest request, Imovel imovel, Cliente cliente) {
        Locacao locacao = new Locacao();
        locacao.setAtivo(true);
        atualizarEntidade(locacao, request, imovel, cliente);
        return locacao;
    }

    public void atualizarEntidade(Locacao locacao, LocacaoRequest request, Imovel imovel, Cliente cliente) {
        locacao.setImovel(imovel);
        locacao.setCliente(cliente);
        locacao.setDataInicio(request.dataInicio());
        locacao.setDataFim(request.dataFim());
        locacao.setDataVencimento(request.diaVencimento());
        locacao.setPercMulta(request.percMulta());
        locacao.setValorAluguel(request.valorAluguel());
        locacao.setObs(request.obs());
    }

    public LocacaoResponse paraResponse(Locacao locacao) {
        return new LocacaoResponse(
                locacao.getId(),
                imovelMapper.paraResumo(locacao.getImovel()),
                clienteMapper.paraResumo(locacao.getCliente()),
                locacao.getAtivo(),
                locacao.getDataInicio(),
                locacao.getDataFim(),
                locacao.getDataVencimento(),
                locacao.getPercMulta(),
                locacao.getValorAluguel(),
                locacao.getObs()
        );
    }
}