package com.imobiliaria_api.api.mapper;

import com.imobiliaria_api.api.dto.output.PerfilResponse;
import com.imobiliaria_api.entity.Perfil;
import org.springframework.stereotype.Component;

@Component
public class PerfilMapper {

    public PerfilResponse paraResponse(Perfil perfil) {
        return new PerfilResponse(perfil.getId(), perfil.getNome());
    }
}