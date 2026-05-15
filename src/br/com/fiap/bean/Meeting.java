package br.com.fiap.bean;

import javax.swing.*;
import java.time.LocalDate;


public class Meeting {
    //atributos
    private int idMeeting;
    private LocalDate dataMeeting;
    private String formato;
    private String status;
    private int duracao;
    private String transcricao;
    private String uf;
    private String segmento;
    private double notaNps;
    //contrutores
    public Meeting() {}

    public Meeting(int idMeeting, LocalDate dataMeeting, String formato, String status, int duracao, String transcricao, String uf, String segmento, double notaNps) {
        this.idMeeting = idMeeting;
        this.dataMeeting = dataMeeting;
        this.formato = formato;
        setStatus(status);
        setDuracao(duracao);
        setTranscricao(transcricao);
        this.uf = uf;
        this.segmento = segmento;
        setNotaNps(notaNps);
    }
    //getters e setters


    public int getIdMeeting() {
        return idMeeting;
    }

    public void setIdMeeting(int idMeeting) {
        this.idMeeting = idMeeting;
    }

    public LocalDate getDataMeeting() {
        return dataMeeting;
    }

    public void setDataMeeting(LocalDate dataMeeting) {
        this.dataMeeting = dataMeeting;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if ("Finalizada".equalsIgnoreCase(status) || "Em andamento".equalsIgnoreCase(status) || "Cancelada".equalsIgnoreCase(status)){
            this.status = status;
        }
        else {
            System.out.println("Valor Invalido");
        }
    }

    public int getDuracao() {
        return duracao;
    }

    public void setDuracao(int duracao) {
        if(duracao > 0){
            this.duracao = duracao;
        }
        else {
            System.out.println("Valor Invalido");
        }
    }

    public String getTranscricao() {
        return transcricao;
    }

    public void setTranscricao(String transcricao) {
        if(transcricao != null && !transcricao.isBlank()){
            this.transcricao = transcricao;
        }
        else{
            System.out.println("Valor Invalido");
        }
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getSegmento() {
        return segmento;
    }

    public void setSegmento(String segmento) {
        this.segmento = segmento;
    }

    public double getNotaNps() {
        return notaNps;
    }

    public void setNotaNps(double notaNps) {
        if (notaNps >= 0 && notaNps <= 10){
            this.notaNps = notaNps;
        }
        else {
            System.out.println("Valor Invalido");
        }
    }
    //metodos da classe

    public void exibirResumoMeeting(){
        JOptionPane.showMessageDialog(null,String.format("Id Meeting: %d \nData: %s \nFormato: %s \nStatus: %s \nDuração: %d \nTranscrição: %s \nUF: %s \nSegmento: %s \nNota: %.1f",idMeeting,dataMeeting,formato,status,duracao,transcricao,uf,segmento,notaNps));
    }
}
