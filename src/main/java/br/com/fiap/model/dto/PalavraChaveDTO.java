package br.com.fiap.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PalavraChaveDTO(

        @NotBlank
        @Size(max = 100)
        String palavra

) {
}