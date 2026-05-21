package br.com.fiap.bean;

import javax.swing.*;

public class AlertaRisco  extends Alerta{
    //atributos
    private String concorrenteDetectado;
    private boolean riscoChurn;
    private String mensagemRisco;

    // construtores
    public AlertaRisco() {}

    public AlertaRisco(String tipo, String nivel, String descricao, String concorrenteDetectado, boolean riscoChurn) {
        super(tipo, nivel, descricao);
        setConcorrenteDetectado(concorrenteDetectado);
        setRiscoChurn(riscoChurn);
    }

    // getters e setters
    public String getConcorrenteDetectado() {
        return concorrenteDetectado;
    }

    public void setConcorrenteDetectado(String concorrenteDetectado) {
        if (concorrenteDetectado != null && !concorrenteDetectado.isBlank()){
            this.concorrenteDetectado = concorrenteDetectado;
        }else {
            System.out.println("Valor Invalido");
        }

    }

    public boolean isRiscoChurn() {
        return riscoChurn;
    }

    public void setRiscoChurn(boolean riscoChurn) {

        this.riscoChurn = riscoChurn;
        if (riscoChurn){
            mensagemRisco ="Existe risco de cancelamento";
        }else{
            mensagemRisco = "Não existe risco de cancelamento";
        }
    }
    //metodos da classe


    public void exibirAlerta() {
        super.exibirAlerta();

        JOptionPane.showMessageDialog(null,
                String.format("Concorrente: %s \nRisco de churn: %s", concorrenteDetectado,mensagemRisco));
    }
}
