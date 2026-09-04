package br.com.fiap.service;

import java.util.Arrays;
import java.util.List;

public class ProcessadorTexto {

    public String limparTexto(String texto) {
        if (texto == null) return "";
        texto = texto.toLowerCase();
        return removerPontuacao(texto);
    }

    public String removerPontuacao(String texto) {
        return texto.replaceAll("[^a-zA-ZÀ-ÿ0-9 ]", "");
    }

    public List<String> tokenizar(String texto) {
        texto = limparTexto(texto);
        return Arrays.asList(texto.split("\\s+"));
    }
}