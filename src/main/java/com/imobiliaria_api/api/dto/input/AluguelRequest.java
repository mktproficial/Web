package com.imobiliaria_api.api.dto.input;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AluguelRequest(

        @NotNull(message = "A locação é obrigatória")
        @Positive(message = "O id da locação deve ser positivo")
        Integer locacaoId,

        @NotNull(message = "A data de vencimento é obrigatória")
        LocalDate dtVencimento,

        @NotNull(message = "O valor pago é obrigatório")
        @DecimalMin(value = "0.01", message = "O valor pago deve ser maior que 0")
        @Digits(integer = 8, fraction = 2, message = "O valor pago deve ter no máximo 8 dígitos inteiros e 2 decimais")
        BigDecimal valorPago,

        @Size(max = 1000, message = "A observação deve ter no máximo 1000 caracteres")
        String obs
) {
}