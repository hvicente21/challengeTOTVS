package br.com.fiap.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProcessadorTexto {

    public String limparTexto(String texto) {
        if (texto == null) {
            return "";
        }
        texto = texto.toLowerCase();
        texto = texto.trim();
        texto = texto.replaceAll("\\s+", " ");
        return texto;
    }

    public String removerPontuacao(String texto) {
        if (texto == null) {
            return "";
        }
        texto = texto.replaceAll("[^\\p{L}\\p{N}\\s]", " ");
        texto = texto.replaceAll("\\s+", " ").trim();

        return texto;
    }

    public List<String> tokenizar(String texto) {
        texto = limparTexto(texto);
        texto = removerPontuacao(texto);
        if (texto.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.asList(texto.split("\\s+"));
    }
    public List<String> removerStopwords(List<String> palavras) {
        List<String> palavrasFiltradas = new ArrayList<>();
        if (palavras == null) {
            return palavrasFiltradas;
        }
        List<String> stopwords = Arrays.asList(
                "a", "o", "os", "as",
                "de", "da", "do", "das", "dos",
                "e", "em", "para", "com",
                "um", "uma"
        );
        for (String palavra : palavras) {

            if (palavra != null && !palavra.trim().isEmpty()) {
                String palavraNormalizada = palavra.trim().toLowerCase();
                if (!stopwords.contains(palavraNormalizada)) {
                    palavrasFiltradas.add(palavraNormalizada);
                }
            }
        }
        return palavrasFiltradas;
    }
}