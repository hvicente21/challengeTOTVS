package br.com.fiap.bean;

import javax.swing.*;

public class AlertaOportunidade extends Alerta {

    private String produtoRelacionado;
    private boolean potencialUpsell;

    public AlertaOportunidade(String tipo, String nivel, String descricao, String produtoRelacionado, boolean potencialUpsell) {
        super(tipo, nivel, descricao);
        this.produtoRelacionado = produtoRelacionado;
        this.potencialUpsell = potencialUpsell;
    }

    public String getProdutoRelacionado() {
        return produtoRelacionado;
    }

    public void setProdutoRelacionado(String produtoRelacionado) {
        this.produtoRelacionado = produtoRelacionado;
    }

    public boolean isPotencialUpsell() {
        return potencialUpsell;
    }

    public void setPotencialUpsell(boolean potencialUpsell) {
        this.potencialUpsell = potencialUpsell;
    }


    public void exibirAlerta() {
        JOptionPane.showMessageDialog(
                null,
                "🚀 ALERTA DE OPORTUNIDADE\n\n" +
                        "Descrição: " + getDescricao() + "\n" +
                        "Produto Relacionado: " + produtoRelacionado + "\n" +
                        "Potencial Upsell: " + (potencialUpsell ? "SIM" : "NÃO"),
                "Oportunidade Detectada",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}