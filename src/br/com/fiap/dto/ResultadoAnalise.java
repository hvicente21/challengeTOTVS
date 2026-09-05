package br.com.fiap.dto;

import java.util.ArrayList;
import java.util.List;

public class ResultadoAnalise {

    private int idResultadoAnalise;
    private Transcricao transcricao;
    private String resumo;
    private String sentimento;
    private List<Alerta> alertas;
    private double riscoChurn;
    private String classificacao;

    public ResultadoAnalise() {
        alertas = new ArrayList<>();
    }

    public ResultadoAnalise(int idResultadoAnalise, Transcricao transcricao, String resumo, String sentimento, List<Alerta> alertas, double riscoChurn, String classificacao) {
        this.idResultadoAnalise = idResultadoAnalise;
        setTranscricao(transcricao);
        setResumo(resumo);
        setSentimento(sentimento);
        setAlertas(alertas);
        setRiscoChurn(riscoChurn);
        setClassificacao(classificacao);
    }

    public int getIdResultadoAnalise() {
        return idResultadoAnalise;
    }

    public void setIdResultadoAnalise(int idResultadoAnalise) {
        this.idResultadoAnalise = idResultadoAnalise;
    }

    public Transcricao getTranscricao() {
        return transcricao;
    }

    public void setTranscricao(Transcricao transcricao) {
        if (transcricao == null) {
            throw new IllegalArgumentException(
                    "O resultado da análise deve possuir uma transcrição."
            );
        }
        this.transcricao = transcricao;
    }

    public String getResumo() {
        return resumo;
    }

    public void setResumo(String resumo) {
        if (resumo == null || resumo.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "O resumo não pode ser nulo ou vazio."
            );
        }
        this.resumo = resumo.trim();
    }

    public String getSentimento() {
        return sentimento;
    }

    public void setSentimento(String sentimento) {
        if (sentimento == null) {
            throw new IllegalArgumentException(
                    "O sentimento deve ser Positivo, Negativo, Neutro ou Misto."
            );
        }
        sentimento = sentimento.trim();
        if (!sentimento.equals("Positivo") &&
                !sentimento.equals("Negativo") &&
                !sentimento.equals("Neutro") &&
                !sentimento.equals("Misto")) {
            throw new IllegalArgumentException(
                    "O sentimento deve ser Positivo, Negativo, Neutro ou Misto."
            );
        }

        this.sentimento = sentimento;
    }

    public List<Alerta> getAlertas() {
        return alertas;
    }

    public void setAlertas(List<Alerta> alertas) {
        this.alertas = new ArrayList<>();

        if (alertas != null) {
            for (Alerta alerta : alertas) {
                adicionarAlerta(alerta);
            }
        }
    }

    public double getRiscoChurn() {
        return riscoChurn;
    }

    public void setRiscoChurn(double riscoChurn) {
        if (riscoChurn < 0 || riscoChurn > 100) {
            throw new IllegalArgumentException("O risco de churn deve estar entre 0 e 100."
            );
        }

        this.riscoChurn = riscoChurn;
    }

    public String getClassificacao() {
        return classificacao;
    }

    public void setClassificacao(String classificacao) {
        if (classificacao == null) {
            throw new IllegalArgumentException(
                    "A classificação deve ser Risco, Oportunidade ou Estável."
            );
        }
        classificacao = classificacao.trim();
        if (!classificacao.equals("Risco") &&
                !classificacao.equals("Oportunidade") &&
                !classificacao.equals("Estável")) {
            throw new IllegalArgumentException(
                    "A classificação deve ser Risco, Oportunidade ou Estável."
            );
        }
        this.classificacao = classificacao;
    }

    public String gerarRelatorio() {
        StringBuilder relatorio = new StringBuilder();
        relatorio.append("🎯 INTELIGÊNCIA ACIONÁVEL EXTRAÍDA\n");
        relatorio.append("=========================================\n\n");

        relatorio.append("🔹 Sentimento da Conversa:\n").append(sentimento).append("\n\n");
        relatorio.append("🔹 Classificação de Contexto:\n").append(classificacao).append("\n\n");

        relatorio.append("🔹 Sumário Executivo (Ruído Limpo):\n\"").append(resumo).append("\"\n\n");

        relatorio.append("🔹 Risco de Churn:\n").append(riscoChurn).append("%\n\n");

        relatorio.append("🚨 Descobertas Comerciais e Gatilhos:\n");
        if (alertas.isEmpty()) {
            relatorio.append("- Nenhum sinal de risco ou oportunidade detectado.");
        } else {
            for (Alerta alerta : alertas) {
                relatorio.append("• ").append(alerta.exibirAlerta()).append("\n");
            }
        }
        return relatorio.toString();
    }

    public void adicionarAlerta(Alerta alerta) {
        if (alerta == null ||
                alerta.getTipoAlerta() == null ||
                alerta.getDescricao() == null ||
                alerta.getDescricao().trim().isEmpty()) {
            return;
        }
        if (alerta.getTipoAlerta().equals("Risco")
                && alerta.getNivelRisco() == null) {
            return;
        }
        for (Alerta alertaExistente : alertas) {
            boolean mesmoTipo =
                    alertaExistente.getTipoAlerta()
                            .equalsIgnoreCase(alerta.getTipoAlerta());
            boolean mesmoNivel =
                    (alertaExistente.getNivelRisco() == null &&
                            alerta.getNivelRisco() == null) ||
                            (alertaExistente.getNivelRisco() != null &&
                                    alertaExistente.getNivelRisco()
                                            .equalsIgnoreCase(alerta.getNivelRisco()));
            boolean mesmaDescricao =
                    alertaExistente.getDescricao()
                            .equalsIgnoreCase(alerta.getDescricao());
            if (mesmoTipo && mesmoNivel && mesmaDescricao) {
                return;
            }
        }
        alerta.setResultadoAnalise(this);
        alertas.add(alerta);
    }
    public String toString() {
        return "ResultadoAnalise{" +
                "idResultadoAnalise=" + idResultadoAnalise +
                ", idTranscricao=" +
                (transcricao != null ? transcricao.getIdTranscricao() : 0) +
                ", resumo='" + resumo + '\'' +
                ", sentimento='" + sentimento + '\'' +
                ", quantidadeAlertas=" + alertas.size() +
                ", riscoChurn=" + riscoChurn +
                ", classificacao='" + classificacao + '\'' +
                '}';
    }
}