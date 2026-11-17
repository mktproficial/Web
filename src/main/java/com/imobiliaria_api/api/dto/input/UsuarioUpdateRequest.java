package com.imobiliaria_api.api.dto.input;

import com.imobiliaria_api.entity.PerfilNome;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record UsuarioUpdateRequest(

        @NotBlank(message = "O login é obrigatório")
        @Size(min = 3, max = 100, message = "O login deve ter entre 3 e 100 caracteres")
        String login,

        @Size(min = 6, max = 100, message = "A senha deve ter entre 6 e 100 caracteres")
        String senha,

        @NotNull(message = "O campo ativo é obrigatório")
        Boolean ativo,

        @NotEmpty(message = "Pelo menos um perfil é obrigatório")
        Set<PerfilNome> perfis,

        @Positive(message = "O id do cliente deve ser positivo")
        Integer clienteId
) {
}