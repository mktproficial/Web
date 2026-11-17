package com.imobiliaria_api.api.dto.output;

import com.imobiliaria_api.entity.PerfilNome;

import java.util.Set;

public record UsuarioResponse(
        Integer id,
        String login,
        boolean ativo,
        Set<PerfilNome> perfis,
        Integer clienteId
) {
}