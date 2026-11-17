package com.imobiliaria_api.api.dto.input;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record ClienteRequest(

        @NotBlank(message = "O nome é obrigatório")
        @Size(min = 2, max = 255, message = "O nome deve ter entre 2 e 255 caracteres")
        String nomeCliente,

        @NotBlank(message = "O CPF é obrigatório")
        @Pattern(regexp = "\\d{11}", message = "O CPF deve conter exatamente 11 dígitos numéricos")
        String cpf,

        @NotBlank(message = "O telefone é obrigatório")
        @Pattern(regexp = "\\(\\d{2}\\) \\d{4,5}-\\d{4}", message = "O telefone deve estar no formato (99) 99999-9999")
        String telefone,

        @NotBlank(message = "O email é obrigatório")
        @Email(message = "O email deve ser um endereço válido")
        @Size(max = 100, message = "O email deve ter no máximo 100 caracteres")
        String email,

        @NotNull(message = "A data de nascimento é obrigatória")
        @Past(message = "A data de nascimento deve estar no passado")
        LocalDate dtNascimento
) {
}