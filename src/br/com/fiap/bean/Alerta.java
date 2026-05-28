package br.com.fiap.bean;

import javax.swing.*;

public class Alerta {

    private String tipo;
    private String nivel;
    private String descricao;

    public Alerta(String tipo, String nivel, String descricao) {
        this.tipo = tipo;
        this.nivel = nivel;
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void exibirAlerta() {
        JOptionPane.showMessageDialog(
                null,
                "Tipo: " + tipo + "\nNível: " + nivel + "\nDescrição: " + descricao,
                "Alerta",
                JOptionPane.WARNING_MESSAGE
        );
    }
}