package br.com.fiap.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record AlertaDTO(

        @NotNull
        @Positive
        Integer idResultadoAnalise,

        @NotBlank
        @Size(max = 30)
        String tipoAlerta,

        @Size(max = 20)
        String nivelRisco,

        @NotBlank
        @Size(max = 500)
        String descricao

) {
}