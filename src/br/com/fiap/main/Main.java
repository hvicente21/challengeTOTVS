package br.com.fiap.main;

import br.com.fiap.bean.*;
import javax.swing.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Main {

    public static void main(String[] args) {

        UIManager.put("OptionPane.yesButtonText", "Sim");
        UIManager.put("OptionPane.noButtonText", "Não");

        // 1. Pergunta: ID da Reunião
        int idMeeting = 0;
        while (true) {
            try {
                String input = JOptionPane.showInputDialog(null, "Digite o ID da meeting:", "Entrada de Dados", JOptionPane.QUESTION_MESSAGE);
                if (input == null) System.exit(0);
                idMeeting = Integer.parseInt(input);
                break;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Erro: Digite apenas números inteiros para o ID!", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            }
        }

        // 2. Pergunta: Data da Reunião (BLOQUEANDO DATAS FUTURAS)
        LocalDate dataMeeting = null;
        DateTimeFormatter formatadorData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        while (true) {
            try {
                String inputData = JOptionPane.showInputDialog(null, "Digite a data da reunião (DD/MM/AAAA):", "Entrada de Dados", JOptionPane.QUESTION_MESSAGE);
                if (inputData == null) System.exit(0);

                dataMeeting = LocalDate.parse(inputData, formatadorData);

                // Validação de segurança corporativa: Reunião não pode acontecer no futuro
                if (dataMeeting.isAfter(LocalDate.now())) {
                    JOptionPane.showMessageDialog(null, "Erro: A data da reunião não pode ser posterior à data de hoje (" + LocalDate.now().format(formatadorData) + ")!", "Data Inválida", JOptionPane.ERROR_MESSAGE);
                } else {
                    break; // Data válida e igual ou anterior a hoje
                }
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(null, "Data inválida ou formato incorreto! Use o padrão: DD/MM/AAAA", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            }
        }

        // 3. Pergunta: Formato
        String formato = "";
        while (true) {
            formato = JOptionPane.showInputDialog(null, "Digite o formato da meeting (Online/Presencial):", "Entrada de Dados", JOptionPane.QUESTION_MESSAGE);
            if (formato == null) System.exit(0);
            formato = formato.trim().toLowerCase();
            if (formato.equals("online") || formato.equals("presencial")) {
                formato = formato.substring(0, 1).toUpperCase() + formato.substring(1);
                break;
            }
            JOptionPane.showMessageDialog(null, "Opção inválida! Digite apenas Online ou Presencial.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
        }

        // 4. Pergunta: Status
        String status = "";
        while (true) {
            status = JOptionPane.showInputDialog(null, "Digite o status da meeting (Finalizada/Em andamento/Cancelada):", "Entrada de Dados", JOptionPane.QUESTION_MESSAGE);
            if (status == null) System.exit(0);
            status = status.trim().toLowerCase();
            if (status.equals("finalizada") || status.equals("em andamento") || status.equals("cancelada")) {
                status = status.substring(0, 1).toUpperCase() + status.substring(1);
                break;
            }
            JOptionPane.showMessageDialog(null, "Status inválido! Use: Finalizada, Em andamento ou Cancelada.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
        }

        // 5. Pergunta: Duração
        int duracao = 0;
        while (true) {
            try {
                String input = JOptionPane.showInputDialog(null, "Digite a duração da meeting em minutos:", "Entrada de Dados", JOptionPane.QUESTION_MESSAGE);
                if (input == null) System.exit(0);
                duracao = Integer.parseInt(input);
                if (duracao > 0) break;
                JOptionPane.showMessageDialog(null, "A duração deve ser maior que zero.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Erro: Digite apenas números para a duração!", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            }
        }

        // 6. Pergunta: UF
        String uf = JOptionPane.showInputDialog(null, "Digite a UF do cliente:", "Entrada de Dados", JOptionPane.QUESTION_MESSAGE);
        if (uf == null) System.exit(0);

        // 7. Pergunta: Segmento
        String segmento = JOptionPane.showInputDialog(null, "Digite o segmento do cliente:", "Entrada de Dados", JOptionPane.QUESTION_MESSAGE);
        if (segmento == null) System.exit(0);

        // 8. Pergunta: Transcrição
        String transcricao = JOptionPane.showInputDialog(null, "Digite a transcrição da reunião:", "Entrada de Dados", JOptionPane.QUESTION_MESSAGE);
        if (transcricao == null) System.exit(0);

        // Instanciação sem a pergunta do NPS (Calculado automaticamente mais abaixo)
        Meeting meeting = new Meeting(idMeeting, dataMeeting, formato, status, duracao, transcricao, uf, segmento, 0.0);

        AnalisadorTexto analisador = new AnalisadorTexto();
        ResultadoAnalise resultado = analisador.analisar(meeting);

        meeting.exibirResumoMeeting();

        JOptionPane.showMessageDialog(
                null,
                resultado.gerarRelatorio(),
                "Resultado da Análise",
                JOptionPane.INFORMATION_MESSAGE
        );

        if (!resultado.getAlertas().isEmpty()) {
            int resposta = JOptionPane.showConfirmDialog(
                    null,
                    "Deseja visualizar os alertas detectados?",
                    "Alertas",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (resposta == JOptionPane.YES_OPTION) {
                for (Alerta alerta : resultado.getAlertas()) {
                    alerta.exibirAlerta();
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Nenhum alerta identificado nessa meeting.", "Alertas", JOptionPane.INFORMATION_MESSAGE);
        }

        JOptionPane.showMessageDialog(null, "Análise finalizada com sucesso.", "Fim do Programa", JOptionPane.INFORMATION_MESSAGE);
    }
}