package com.imobiliaria_api.api.dto.output;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AluguelResponse(
        Integer id,
        Integer locacaoId,
        LocalDate dtVencimento,
        BigDecimal valorPago,
        String obs
) {
}