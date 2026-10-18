package br.com.fiap.model.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record MeetingDTO(

        @NotNull
        LocalDateTime dataMeeting,

        @NotBlank
        @Size(max = 20)
        String formato,

        @NotBlank
        @Size(max = 30)
        String status,

        @NotNull
        @Positive
        Integer duracao,

        @Size(min = 2, max = 2)
        String uf,

        @Size(max = 100)
        String segmento,

        @NotNull
        @DecimalMin("0.0")
        @DecimalMax("10.0")
        Double notaNps

) {
}