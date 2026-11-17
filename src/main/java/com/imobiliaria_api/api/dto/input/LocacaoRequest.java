package com.imobiliaria_api.api.dto.input;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LocacaoRequest(

        @NotNull(message = "O imóvel é obrigatório")
        @Positive(message = "O id do imóvel deve ser positivo")
        Integer imovelId,

        @NotNull(message = "O inquilino é obrigatório")
        @Positive(message = "O id do inquilino deve ser positivo")
        Integer inquilinoId,

        @NotNull(message = "A data de início é obrigatória")
        LocalDate dataInicio,

        @NotNull(message = "A data de fim é obrigatória")
        LocalDate dataFim,

        @NotNull(message = "O dia de vencimento é obrigatório")
        @Min(value = 1, message = "O dia de vencimento deve estar entre 1 e 31")
        @Max(value = 31, message = "O dia de vencimento deve estar entre 1 e 31")
        Integer diaVencimento,

        @NotNull(message = "O percentual de multa é obrigatório")
        @DecimalMin(value = "0.0", message = "O percentual de multa deve ser maior ou igual a 0")
        @DecimalMax(value = "100.0", message = "O percentual de multa deve ser no máximo 100")
        @Digits(integer = 3, fraction = 2, message = "O percentual de multa deve ter no máximo 3 dígitos inteiros e 2 decimais")
        BigDecimal percMulta,

        @NotNull(message = "O valor do aluguel é obrigatório")
        @DecimalMin(value = "0.01", message = "O valor do aluguel deve ser maior que 0")
        @Digits(integer = 8, fraction = 2, message = "O valor do aluguel deve ter no máximo 8 dígitos inteiros e 2 decimais")
        BigDecimal valorAluguel,

        @Size(max = 1000, message = "A observação deve ter no máximo 1000 caracteres")
        String obs
) {
}