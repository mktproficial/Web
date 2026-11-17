package com.imobiliaria_api.api.dto.output;

import java.math.BigDecimal;

public record ImovelResponse(
        Integer id,
        String tipoImovel,
        String endereco,
        String cep,
        Integer dormitorios,
        Integer banheiros,
        Integer suites,
        Integer metragem,
        BigDecimal valorAluguelSug,
        String obs
) {
}