package com.imobiliaria_api.api.dto.output;

import com.imobiliaria_api.entity.PerfilNome;

public record PerfilResponse(
        Integer id,
        PerfilNome nome
) {
}