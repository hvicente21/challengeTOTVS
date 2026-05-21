package br.com.fiap.bean;

public class AlertaOportunidade extends Alerta{

    //atributos
    private String produtoRelacionado;
    private boolean potenciaUpsell;
    //construtores
    public AlertaOportunidade() {}

    public AlertaOportunidade(String tipo, String nivel, String descricao, String produtoRelacionado, boolean potenciaUpsell) {
        super(tipo, nivel, descricao);
        this.produtoRelacionado = produtoRelacionado;
        this.potenciaUpsell = potenciaUpsell;

    }
    //getters e setters

    public String getProdutoRelacionado() {
        return produtoRelacionado;
    }

    public void setProdutoRelacionado(String produtoRelacionado) {
        this.produtoRelacionado = produtoRelacionado;
    }

    public boolean isPotenciaUpsell() {
        return potenciaUpsell;
    }

    public void setPotenciaUpsell(boolean potenciaUpsell) {
        this.potenciaUpsell = potenciaUpsell;
    }
    //metodo da classe
    public void exibirAlerta() {
        super.exibirAlerta();
    }
}
