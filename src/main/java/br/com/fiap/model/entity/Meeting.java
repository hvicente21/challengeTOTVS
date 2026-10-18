package br.com.fiap.model.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.Duration;
import javax.swing.*;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Entity(name = "Meeting")
@Table(name = "MEETING")
public class Meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_MEETING")
    private int idMeeting;
    @Column(name = "DT_MEETING", nullable = false)
    private LocalDateTime dataMeeting;
    @Column(name = "FORMATO", nullable = false, length = 20)
    private String formato;
    @Column(name = "STATUS", nullable = false, length = 30)
    private String status;
    @JdbcTypeCode(SqlTypes.INTERVAL_SECOND)
    @Column(name = "DURACAO", nullable = false)
    private Duration duracao;
    @Transient
    private Transcricao transcricao;
    @Column(name = "UF", length = 2)
    private String uf;
    @Column(name = "NOME_SEGMENTO", length = 100)
    private String segmento;
    @Column(name = "NOTA_NPS")
    private double notaNps;
    @ManyToMany
    @JoinTable(
            name = "MEETING_PALAVRA_CHAVE",
            joinColumns = @JoinColumn(name = "ID_MEETING"),
            inverseJoinColumns = @JoinColumn(name = "ID_PALAVRA_CHAVE")
    )
    private List<PalavraChave> palavrasChave = new ArrayList<>();

    public Meeting() {
    }

    public Meeting(int idMeeting, LocalDateTime dataMeeting, String formato, String status, int duracao, Transcricao transcricao, String uf, String segmento, double notaNps, List<PalavraChave> palavrasChave) {
        this.idMeeting = idMeeting;
        setDataMeeting(dataMeeting);
        setFormato(formato);
        setStatus(status);
        setDuracao(duracao);
        setTranscricao(transcricao);
        setUf(uf);
        setSegmento(segmento);
        setNotaNps(notaNps);
        setPalavrasChave(palavrasChave);

    }

    public int getIdMeeting() {
        return idMeeting;
    }

    public void setIdMeeting(int idMeeting) {
        this.idMeeting = idMeeting;
    }

    public LocalDateTime getDataMeeting() {
        return dataMeeting;
    }

    public void setDataMeeting(LocalDateTime dataMeeting) {
        if (dataMeeting == null){
            throw new IllegalArgumentException("A data da reunião não pode ser nula.");
        }
        this.dataMeeting = dataMeeting;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        if (formato == null || formato.trim().isEmpty()) {
            throw new IllegalArgumentException("O formato não pode ser nulo ou vazio.");
        }

        this.formato = formato.trim().toUpperCase();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("O status da reunião não pode ser nulo ou vazio.");
        }
        this.status = status.trim();
    }

    public int getDuracao() {
        if (duracao == null) {
            return 0;
        }

        return Math.toIntExact(duracao.getSeconds());
    }

    public void setDuracao(int duracao) {
        if (duracao <= 0) {
            throw new IllegalArgumentException(
                    "A duração deve ser maior que zero."
            );
        }

        this.duracao = Duration.ofSeconds(duracao);
    }

    public Transcricao getTranscricao() {
        return transcricao;
    }

    public void setTranscricao(Transcricao transcricao) {
        this.transcricao = transcricao;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        if (uf == null || uf.trim().isEmpty()) {
            this.uf = null;
            return;
        }
        uf = uf.trim().toUpperCase();
        String ufsValidas =
                "|AC|AL|AP|AM|BA|CE|DF|ES|GO|MA|MT|MS|MG|PA|PB|PR|PE|PI|RJ|RN|RS|RO|RR|SC|SP|SE|TO|";
        if (uf.length() != 2 || !ufsValidas.contains("|" + uf + "|")) {
            throw new IllegalArgumentException("UF inválida.");
        }
        this.uf = uf;
    }

    public String getSegmento() {
        return segmento;
    }

    public void setSegmento(String segmento) {
        if (segmento != null && segmento.trim().isEmpty()) {
            throw new IllegalArgumentException("O segmento não pode conter apenas espaços.");
        }
        this.segmento = segmento == null ? null : segmento.trim();
    }

    public double getNotaNps() {
        return notaNps;
    }

    public void setNotaNps(double notaNps) {
        if (notaNps < 0 || notaNps > 10) {
            throw new IllegalArgumentException("A nota NPS deve estar entre 0 e 10.");
        }
        this.notaNps = notaNps;
    }

    public List<PalavraChave> getPalavrasChave() {
        return palavrasChave;
    }

    public void setPalavrasChave(List<PalavraChave> palavrasChave) {
        this.palavrasChave = new ArrayList<>();
        if (palavrasChave != null) {
            for (PalavraChave palavra : palavrasChave) {
                adicionarPalavraChave(palavra);
            }
        }
    }

    public String classificarNps() {
        if (notaNps >= 9) {
            return "Promotor";
        }
        if (notaNps >= 7) {
            return "Neutro";
        }
        return "Detrator";
    }

    public void exibirResumoMeeting() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");;
        String dataFormatada = dataMeeting.format(formatter);

        JOptionPane.showMessageDialog(null,
                "===== DADOS CADASTRAIS DA MEETING =====\n" +
                        "\nID: " + idMeeting +
                        "\nData: " + dataFormatada +
                        "\nFormato: " + formato +
                        "\nStatus: " + status +
                        "\nDuração: " + getDuracao() + " segundos" +
                        "\nUF: " + uf +
                        "\nSegmento: " + segmento+
                        "\nClassificação: " + classificarNps()+
                        "\nQuantidade de Palavras-chave: " + palavrasChave.size(),
                "Resumo dos Dados de Entrada",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void adicionarPalavraChave(PalavraChave palavraChave){
        if (palavraChave == null ||
                palavraChave.getPalavra() == null ||
                palavraChave.getPalavra().trim().isEmpty()) {
            return;
        }
        for (PalavraChave palavraExistente : palavrasChave) {
            if (palavraExistente.getPalavra().equalsIgnoreCase(palavraChave.getPalavra().trim())) {
                return;
            }
        }
        palavrasChave.add(palavraChave);
    }

    public String toString() {
        return "Meeting{" +
                "idMeeting=" + idMeeting +
                ", dataMeeting=" + dataMeeting +
                ", formato='" + formato + '\'' +
                ", status='" + status + '\'' +
                ", duracao=" + getDuracao() +
                ", uf='" + uf + '\'' +
                ", segmento='" + segmento + '\'' +
                ", notaNps=" + notaNps +
                ", idTranscricao=" +
                (transcricao != null ? transcricao.getIdTranscricao() : 0) +
                ", quantidadePalavrasChave=" + palavrasChave.size() +
                '}';
    }

}