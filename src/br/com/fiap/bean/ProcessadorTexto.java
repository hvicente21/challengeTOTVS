package br.com.fiap.bean;

import java.util.Arrays;
import java.util.List;

public class ProcessadorTexto {
    //construtor
    public ProcessadorTexto() {}
    //metodos da classe
    public String removerPontuacao(String texto){
        if (texto == null || texto.isBlank()){
            return "";
        }
        return texto.replaceAll("[^a-zA-ZÀ-ÿ0-9 ]", "");
    }

    public String limparTexto(String texto){
        if (texto == null || texto.isBlank()) {
            return "";
        }
        texto = texto.toLowerCase();
        texto = removerPontuacao(texto);
        return texto.trim();
    }

    public List<String> tokenizarTexto(String texto){
        texto = limparTexto(texto);
        return Arrays.asList(texto.split("\\s+"));

    }

}
