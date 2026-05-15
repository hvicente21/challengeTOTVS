package br.com.fiap.bean;

import javax.swing.*;

public class Alerta {
    //atributos
    private String tipo;
    private String nivel;
    private String descricao;
    //contrutores
    public Alerta() {}

    public Alerta(String tipo, String nivel, String descricao) {
        setTipo(tipo);
        setNivel(nivel);
        setDescricao(descricao);
    }
    //getters e setters

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        if ("Churn".equalsIgnoreCase(tipo) || "Concorrência".equalsIgnoreCase(tipo) || "Upsell".equalsIgnoreCase(tipo) || "Reclamação".equalsIgnoreCase(tipo)){
            this.tipo = tipo;
        }
        else {
            System.out.println("Valor Invalido");
        }
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        if ("Baixo".equalsIgnoreCase(nivel) || "Médio".equalsIgnoreCase(nivel) || "Alto".equalsIgnoreCase(nivel) || "Crítico".equalsIgnoreCase(nivel)) {
            this.nivel = nivel;
        }
        else {
            System.out.println("Valor Invalido");
        }
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        if (descricao != null && !descricao.isBlank()){
            this.descricao = descricao;
        }
        else {
            System.out.println("Valor Invalido");
        }
    }

    //metodos da classe

    public void exibirAlerta(){
        JOptionPane.showMessageDialog(null,String.format("Tipo: %s \nNivel: %s \nDescrição: %s ",tipo,nivel,descricao));
    }
}
