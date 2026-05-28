package br.com.fiap.bean;

import java.util.ArrayList;
import java.util.List;

public class ResultadoAnalise {

    private String resumo;
    private String sentimento;
    private List<Alerta> alertas;
    private List<String> palavrasChave;
    private String classificacao;

    public ResultadoAnalise() {
        alertas = new ArrayList<>();
        palavrasChave = new ArrayList<>();
    }

    public String getResumo() { return resumo; }
    public void setResumo(String resumo) { this.resumo = resumo; }
    public String getSentimento() { return sentimento; }
    public void setSentimento(String sentimento) { this.sentimento = sentimento; }
    public List<Alerta> getAlertas() { return alertas; }
    public void adicionarAlerta(Alerta alerta) { alertas.add(alerta); }
    public List<String> getPalavrasChave() { return palavrasChave; }
    public void setPalavrasChave(List<String> palavrasChave) { this.palavrasChave = palavrasChave; }
    public String getClassificacao() { return classificacao; }
    public void setClassificacao(String classificacao) { this.classificacao = classificacao; }

    public String gerarRelatorio() {
        StringBuilder relatorio = new StringBuilder();
        relatorio.append("🎯 INTELIGÊNCIA ACIONÁVEL EXTRAÍDA\n");
        relatorio.append("=========================================\n\n");

        relatorio.append("🔹 Sentimento da Conversa:\n").append(sentimento).append("\n\n");
        relatorio.append("🔹 Classificação de Contexto:\n").append(classificacao).append("\n\n");
        relatorio.append("🔹 Palavras-chave Identificadas:\n[").append(String.join(", ", palavrasChave)).append("]\n\n");

        relatorio.append("🔹 Sumário Executivo (Ruído Limpo):\n\"").append(resumo).append("\"\n\n");

        relatorio.append("🚨 Descobertas Comerciais e Gatilhos:\n");
        if (alertas.isEmpty()) {
            relatorio.append("- Nenhum sinal de risco ou oportunidade detectado.");
        } else {
            for (Alerta alerta : alertas) {
                relatorio.append("• [").append(alerta.getTipo()).append("] ").append(alerta.getDescricao()).append("\n");
            }
        }
        return relatorio.toString();
    }
}