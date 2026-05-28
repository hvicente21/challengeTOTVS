package br.com.fiap.bean;

import javax.swing.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Meeting {

    private int idMeeting;
    private LocalDate dataMeeting;
    private String formato;
    private String status;
    private int duracao;
    private String transcricao;
    private String uf;
    private String segmento;
    private double notaNps; // Mantido pela UML, mas agora calculado automaticamente

    public Meeting() {
    }

    public Meeting(int idMeeting, LocalDate dataMeeting, String formato, String status, int duracao, String transcricao, String uf, String segmento, double notaNps) {
        this.idMeeting = idMeeting;
        this.dataMeeting = dataMeeting;
        this.formato = formato;
        this.status = status;
        this.duracao = duracao;
        this.transcricao = transcricao;
        this.uf = uf;
        this.segmento = segmento;
        this.notaNps = notaNps;
    }

    public int getIdMeeting() { return idMeeting; }
    public LocalDate getDataMeeting() { return dataMeeting; }
    public String getFormato() { return formato; }
    public String getStatus() { return status; }
    public int getDuracao() { return duracao; }
    public String getTranscricao() { return transcricao; }
    public String getUf() { return uf; }
    public String getSegmento() { return segmento; }
    public double getNotaNps() { return notaNps; }
    public void setNotaNps(double notaNps) { this.notaNps = notaNps; }

    public String classificarNps() {
        if (notaNps >= 9) return "Promotor 🌟 (Satisfeito)";
        if (notaNps >= 7) return "Neutro 😐 (Misto/Vulnerável)";
        return "Detrator ⚠️ (Frustrado/Risco)";
    }

    public void exibirResumoMeeting() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dataFormatada = dataMeeting.format(formatter);

        JOptionPane.showMessageDialog(null,
                "===== DADOS CADASTRAIS DA MEETING =====\n" +
                        "\nID: " + idMeeting +
                        "\nData: " + dataFormatada +
                        "\nFormato: " + formato +
                        "\nStatus: " + status +
                        "\nDuração: " + duracao + " minutos" +
                        "\nUF: " + uf +
                        "\nSegmento: " + segmento,
                "Resumo dos Dados de Entrada",
                JOptionPane.INFORMATION_MESSAGE);
    }
}