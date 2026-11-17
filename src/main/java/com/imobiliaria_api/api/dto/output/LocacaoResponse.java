package com.imobiliaria_api.api.dto.output;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LocacaoResponse(
        Integer id,
        ImovelResumoResponse imovel,
        ClienteResumoResponse inquilino,
        Boolean ativo,
        LocalDate dataInicio,
        LocalDate dataFim,
        Integer diaVencimento,
        BigDecimal percMulta,
        BigDecimal valorAluguel,
        String obs
) {
}