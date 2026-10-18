package br.com.fiap.model.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ResultadoAnaliseDTO(

        @NotNull
        @Positive
        Integer idTranscricao,

        @NotBlank
        String resumo,

        @NotBlank
        String sentimento,

        @NotNull
        @DecimalMin("0.0")
        @DecimalMax("100.0")
        Double riscoChurn,

        @NotBlank
        String classificacao

) {
}