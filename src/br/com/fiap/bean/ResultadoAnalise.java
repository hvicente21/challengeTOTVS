package br.com.fiap.bean;

import java.util.ArrayList;
import java.util.List;

public class ResultadoAnalise {
    //atributos
    private String resumo;
    private String sentimento;
    private List<Alerta> alertas;
    private List<String> palavrasChave;
    private String classificacao;
    //construtores
    public ResultadoAnalise() {
        this.alertas = new ArrayList<>();
    }

    public ResultadoAnalise(String resumo, String sentimento, List<Alerta> alertas, List<String> palavrasChave, String classificacao) {
        setResumo(resumo);
        setSentimento(sentimento);
        setAlertas(alertas);
        setPalavrasChave(palavrasChave);
        setClassificacao(classificacao);
    }
    //getters e setters

    public String getResumo() {
        return resumo;
    }

    public void setResumo(String resumo) {
        if (resumo != null && !resumo.isBlank()) {
            this.resumo = resumo;
        } else {
            System.out.println("Resumo inválido");
        }
    }

    public String getSentimento() {
        return sentimento;
    }

    public void setSentimento(String sentimento) {
        if (sentimento.equalsIgnoreCase("Positivo") || sentimento.equalsIgnoreCase("Negativo") || sentimento.equalsIgnoreCase("Neutro")) {

            this.sentimento = sentimento;
        } else {
            System.out.println("Sentimento inválido");
        }
    }

    public List<Alerta> getAlertas() {
        return alertas;
    }

    public void setAlertas(List<Alerta> alertas) {
        if (alertas != null) {
            this.alertas = alertas;
        } else {
            System.out.println("Lista de alertas inválida");
        }
    }

    public List<String> getPalavrasChave() {
        return palavrasChave;
    }

    public void setPalavrasChave(List<String> palavrasChave) {
        if (palavrasChave != null && !palavrasChave.isEmpty()) {
            this.palavrasChave = palavrasChave;
        } else {
            System.out.println("Palavras-chave inválidas");
        }
    }

    public String getClassificacao() {
        return classificacao;
    }

    public void setClassificacao(String classificacao) {
        if (classificacao.equalsIgnoreCase("Risco") || classificacao.equalsIgnoreCase("Oportunidade") || classificacao.equalsIgnoreCase("Neutro")) {

            this.classificacao = classificacao;
        } else {
            System.out.println("Classificação inválida");
        }
    }
    //metodos da classe
    public void adicionarAlerta(Alerta alerta){
        if (alerta != null){
            alertas.add(alerta);
        }else {
            System.out.println("Alerta inválido");
        }

    }

    public String gerarRelatorio(){
        return "Resumo: " + resumo + "\nSentimento: " + sentimento + "\nClassificação: " + classificacao + "\nPalavras-chave: " + palavrasChave + "\nQuantidade de alertas: " + alertas.size();
    }

}
