package br.com.fiap.bean;

import javax.swing.*;

public class AlertaRisco extends Alerta {

    private String concorrenteDetectado;
    private boolean riscoChurn;

    public AlertaRisco(String tipo, String nivel, String descricao, String concorrenteDetectado, boolean riscoChurn) {
        super(tipo, nivel, descricao);
        this.concorrenteDetectado = concorrenteDetectado;
        this.riscoChurn = riscoChurn;
    }

    public String getConcorrenteDetectado() {
        return concorrenteDetectado;
    }

    public void setConcorrenteDetectado(String concorrenteDetectado) {
        this.concorrenteDetectado = concorrenteDetectado;
    }

    public boolean isRiscoChurn() {
        return riscoChurn;
    }

    public void setRiscoChurn(boolean riscoChurn) {
        this.riscoChurn = riscoChurn;
    }

    @Override
    public void exibirAlerta() {
        JOptionPane.showMessageDialog(
                null,
                "⚠ ALERTA DE RISCO\n\n" +
                        "Descrição: " + getDescricao() + "\n" +
                        "Concorrente Detectado: " + concorrenteDetectado + "\n" +
                        "Risco de Churn: " + (riscoChurn ? "SIM" : "NÃO"),
                "Risco Detectado",
                JOptionPane.WARNING_MESSAGE
        );
    }
}