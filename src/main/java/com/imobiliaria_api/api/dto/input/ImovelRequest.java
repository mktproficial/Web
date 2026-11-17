package com.imobiliaria_api.api.dto.input;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ImovelRequest(

        @NotBlank(message = "O tipo do imóvel é obrigatório")
        @Size(max = 50, message = "O tipo do imóvel deve ter no máximo 50 caracteres")
        String tipoImovel,

        @NotBlank(message = "O endereço é obrigatório")
        @Size(max = 255, message = "O endereço deve ter no máximo 255 caracteres")
        String endereco,

        @NotBlank(message = "O CEP é obrigatório")
        @Pattern(regexp = "\\d{5}-\\d{3}", message = "O CEP deve estar no formato 99999-999")
        String cep,

        @NotNull(message = "A quantidade de dormitórios é obrigatória")
        @Min(value = 0, message = "A quantidade de dormitórios deve ser maior ou igual a 0")
        Integer dormitorios,

        @NotNull(message = "A quantidade de banheiros é obrigatória")
        @Min(value = 0, message = "A quantidade de banheiros deve ser maior ou igual a 0")
        Integer banheiros,

        @NotNull(message = "A quantidade de suítes é obrigatória")
        @Min(value = 0, message = "A quantidade de suítes deve ser maior ou igual a 0")
        Integer suites,

        @NotNull(message = "A metragem é obrigatória")
        @Min(value = 1, message = "A metragem deve ser maior que 0")
        Integer metragem,

        @NotNull(message = "O valor do aluguel sugerido é obrigatório")
        @DecimalMin(value = "0.01", message = "O valor do aluguel sugerido deve ser maior que 0")
        @Digits(integer = 8, fraction = 2, message = "O valor do aluguel sugerido deve ter no máximo 8 dígitos inteiros e 2 decimais")
        BigDecimal valorAluguelSug,

        @Size(max = 1000, message = "A observação deve ter no máximo 1000 caracteres")
        String obs
) {
}