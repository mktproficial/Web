package com.imobiliaria_api.api.dto.output;

public record ImovelResumoResponse(
        Integer id,
        String tipoImovel,
        String endereco
) {
}