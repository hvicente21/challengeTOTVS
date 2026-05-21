package br.com.fiap.bean;

import javax.swing.*;

public class AlertaOportunidade extends Alerta{

    //atributos
    private String produtoRelacionado;
    private boolean potencialUpsell;
    private String mensagemUpsell;
    //construtores
    public AlertaOportunidade() {}

    public AlertaOportunidade(String tipo, String nivel, String descricao, String produtoRelacionado, boolean potenciaUpsell) {
        super(tipo, nivel, descricao);
        this.produtoRelacionado = produtoRelacionado;
        this.potencialUpsell = potenciaUpsell;
    }
    //getters e setters

    public String getProdutoRelacionado() {
        return produtoRelacionado;
    }

    public void setProdutoRelacionado(String produtoRelacionado) {
        if (produtoRelacionado != null && !produtoRelacionado.isBlank()){
            this.produtoRelacionado = produtoRelacionado;
        }else {
            System.out.println("Valor Invalido");
        }
    }

    public boolean isPotenciaUpsell() {
        return potencialUpsell;
    }

    public void setPotenciaUpsell(boolean potenciaUpsell) {
        this.potencialUpsell = potenciaUpsell;
        if (potenciaUpsell){
            mensagemUpsell ="Existe oportunidade de upsell";
        }else{
            mensagemUpsell = "Não existe oportunidade de upsell";
        }
    }
    //metodo da classe
    public void exibirAlerta() {
        super.exibirAlerta();

        JOptionPane.showMessageDialog(null,
                String.format("Produto Relacionado: %s \nOportunidade de Upsell: %s", produtoRelacionado, mensagemUpsell));
    }
}
