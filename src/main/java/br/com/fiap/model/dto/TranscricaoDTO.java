package br.com.fiap.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TranscricaoDTO(

        @NotNull
        @Positive
        Integer idMeeting,

        @NotBlank
        String texto

) {
}