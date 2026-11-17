package com.imobiliaria_api.api.dto.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegistroRequest(

        @NotBlank(message = "O login é obrigatório")
        @Size(min = 3, max = 100, message = "O login deve ter entre 3 e 100 caracteres")
        String login,

        @NotBlank(message = "A senha é obrigatória")
        @Size(min = 6, max = 100, message = "A senha deve ter entre 6 e 100 caracteres")
        String senha
) {
}