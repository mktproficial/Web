package com.imobiliaria_api.api.mapper;

import com.imobiliaria_api.api.dto.output.UsuarioResponse;
import com.imobiliaria_api.entity.Perfil;
import com.imobiliaria_api.entity.PerfilNome;
import com.imobiliaria_api.entity.Usuario;
import org.springframework.stereotype.Component;

import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UsuarioMapper {

    public UsuarioResponse paraResponse(Usuario usuario) {
        Set<PerfilNome> perfis = usuario.getPerfis().stream()
                .map(Perfil::getNome)
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(PerfilNome.class)));

        Integer clienteId = usuario.getCliente() == null ? null : usuario.getCliente().getId();

        return new UsuarioResponse(
                usuario.getId(),
                usuario.getLogin(),
                usuario.isAtivo(),
                perfis,
                clienteId
        );
    }
}
