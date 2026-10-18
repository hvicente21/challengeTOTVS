package br.com.fiap.model.entity;

import br.com.fiap.model.dto.TranscricaoDTO;
import jakarta.persistence.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@Entity(name = "Transcricao")
@Table(name = "TRANSCRICAO")
public class Transcricao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_TRANSCRICAO")
    private int idTranscricao;
    @OneToOne
    @JoinColumn(
            name = "ID_MEETING",
            nullable = false,
            unique = true
    )
    private Meeting meeting;
    @Lob
    @Column(name = "TEXTO", nullable = false)
    private String texto;
    @OneToOne(mappedBy = "transcricao")
    private ResultadoAnalise resultadoAnalise;

    public Transcricao() {
    }

    public Transcricao(TranscricaoDTO transcricaoDTO, Meeting meeting) {
        setMeeting(meeting);
        setTexto(transcricaoDTO.texto());
    }

    public Transcricao(int idTranscricao, Meeting meeting, String texto, ResultadoAnalise resultadoAnalise) {
        this.idTranscricao = idTranscricao;
        this.meeting = meeting;
        setTexto(texto);
        this.resultadoAnalise = resultadoAnalise;
    }

    public int getIdTranscricao() {
        return idTranscricao;
    }

    public void setIdTranscricao(int idTranscricao) {
        this.idTranscricao = idTranscricao;
    }

    public Meeting getMeeting() {
        return meeting;
    }

    public void setMeeting(Meeting meeting) {
        this.meeting = meeting;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("O texto não pode ser nulo ou vazio.");
        }

        this.texto = texto;
    }

    public ResultadoAnalise getResultadoAnalise() {
        return resultadoAnalise;
    }

    public void setResultadoAnalise(ResultadoAnalise resultadoAnalise) {
        this.resultadoAnalise = resultadoAnalise;
    }

    public Transcricao lerTranscricao(String caminho) throws IOException {
        if (caminho == null || caminho.trim().isEmpty()) {
            throw new IllegalArgumentException("O caminho não pode ser nulo ou vazio.");
        }

        boolean marcadorEncontrado = false;
        StringBuilder conteudo = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(caminho))) {
            String linha;
            while ((linha = br.readLine()) != null) {

                if (linha.trim().equals("--- TRANSCRIÇÃO ---")) {
                    marcadorEncontrado = true;
                } else if (marcadorEncontrado) {
                    conteudo.append(linha).append("\n");
                }
            }
        }

        if (!marcadorEncontrado) {
            throw new IOException("Marcador de transcrição não encontrado no arquivo.");
        }
        String textoLido = conteudo.toString().trim();
        if (textoLido.isEmpty()) {
            throw new IOException("Nenhum texto de transcrição encontrado após o marcador.");
        }
        setTexto(textoLido);
        return this;
    }

    public String toString() {
        return "Transcricao{" +
                "idTranscricao=" + idTranscricao +
                ", idMeeting=" + (meeting != null ? meeting.getIdMeeting() : 0) +
                ", tamanhoTexto=" + (texto != null ? texto.length() : 0) +
                '}';
    }
}