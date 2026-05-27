package br.com.fiap.main;

import br.com.fiap.bean.*;

import javax.swing.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Main {

    public static void main(String[] args) {

        try {

            JOptionPane.showMessageDialog(null, "=== SISTEMA INTELIGÊNCIA CONVERSACIONAL TOTVS ===");

            // DADOS DA REUNIÃO
            int id = Integer.parseInt(JOptionPane.showInputDialog("Digite o ID da reunião:"));

            String formato = JOptionPane.showInputDialog("Digite o formato da reunião:");

            String status = JOptionPane.showInputDialog("Digite o status da reunião:");

            String segmento = JOptionPane.showInputDialog("Digite o segmento da reunião:");

            int duracao = Integer.parseInt(JOptionPane.showInputDialog("Digite a duração da reunião:"));

            double notaNps = Double.parseDouble(JOptionPane.showInputDialog("Digite a nota NPS:"));

            String dataTexto = JOptionPane.showInputDialog("Digite a data da reunião (dd/MM/yyyy):");
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate dataMeeting = LocalDate.parse(dataTexto, dtf);

            String uf = JOptionPane.showInputDialog("Digite o UF do cliente:");

            // TRANSCRIÇÃO
            String transcricao = JOptionPane.showInputDialog("Digite a transcrição da reunião:");

            JOptionPane.showMessageDialog(null, "Transcrição recebida com sucesso!");

            // CRIAÇÃO DO MEETING
            Meeting meeting = new Meeting(id, dataMeeting, formato, status, duracao, transcricao, uf, segmento, notaNps);

            // PROCESSAMENTO DO TEXTO
            ProcessadorTexto processador = new ProcessadorTexto();
            String textoLimpo = processador.limparTexto(meeting.getTranscricao());

            // ANÁLISE DO TEXTO
            AnalisadorTexto analisador = new AnalisadorTexto();
            ResultadoAnalise resultado = analisador.analisar(meeting);

            // ALERTAS
            if (analisador.detectarRisco(textoLimpo)) {
                AlertaRisco alertaRisco = new AlertaRisco();
                resultado.adicionarAlerta(alertaRisco);
                alertaRisco.exibirAlerta();
            }

            if (analisador.detectarOportunidade(textoLimpo)) {
                AlertaOportunidade alertaOportunidade = new AlertaOportunidade();
                resultado.adicionarAlerta(alertaOportunidade);
                alertaOportunidade.exibirAlerta();
            }

            // RELATÓRIO FINAL
            meeting.exibirResumoMeeting();
            JOptionPane.showMessageDialog(null, resultado.gerarRelatorio());

            JOptionPane.showMessageDialog(null, "Análise concluída com sucesso!");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage());
        }
    }
}
