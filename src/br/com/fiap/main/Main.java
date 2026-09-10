package br.com.fiap.main;

import br.com.fiap.dao.AlertaDao;
import br.com.fiap.dao.AlertaDaoImpl;
import br.com.fiap.dao.ConnectionFactory;
import br.com.fiap.dao.MeetingDao;
import br.com.fiap.dao.MeetingDaoImpl;
import br.com.fiap.dao.ResultadoAnaliseDao;
import br.com.fiap.dao.ResultadoAnaliseDaoImpl;
import br.com.fiap.dao.TranscricaoDao;
import br.com.fiap.dao.TranscricaoDaoImpl;

import br.com.fiap.dto.Alerta;
import br.com.fiap.dto.Meeting;
import br.com.fiap.dto.ResultadoAnalise;
import br.com.fiap.dto.Transcricao;

import br.com.fiap.service.AnalisadorTexto;

import javax.swing.*;
import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        do {

            String opcaoDigitada =
                    JOptionPane.showInputDialog(
                            """
                            INTELIGÊNCIA CONVERSACIONAL - TOTVS

                            O que você deseja fazer?

                            1 - Analisar nova reunião
                            2 - Consultar reuniões
                            3 - Consultar resultados das análises
                            """
                    );

            if (opcaoDigitada == null) {
                break;
            }

            try {

                int opcao =
                        Integer.parseInt(opcaoDigitada);

                switch (opcao) {

                    case 1:
                        analisarReuniao();
                        break;

                    case 2:
                        consultarReunioes();
                        break;

                    case 3:
                        consultarResultados();
                        break;

                    default:
                        JOptionPane.showMessageDialog(
                                null,
                                "Opção inválida."
                        );
                }

            } catch (NumberFormatException e) {

                JOptionPane.showMessageDialog(
                        null,
                        "Digite uma opção numérica válida."
                );
            }

        } while (
                JOptionPane.showConfirmDialog(
                        null,
                        "Deseja continuar?",
                        "Atenção",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                ) == JOptionPane.YES_OPTION
        );
    }


    public static void analisarReuniao() {

        Meeting meeting =
                new Meeting();


        /*
         * =========================================================
         * DADOS DA MEETING
         * =========================================================
         */

        try {

            meeting.setDataMeeting(
                    LocalDateTime.now()
            );

            meeting.setFormato(
                    JOptionPane.showInputDialog(
                            "Digite o formato da reunião:"
                    )
            );

            meeting.setStatus(
                    JOptionPane.showInputDialog(
                            "Digite o status da reunião:"
                    )
            );

            meeting.setDuracao(
                    Integer.parseInt(
                            JOptionPane.showInputDialog(
                                    "Digite a duração da reunião em segundos:"
                            )
                    )
            );

            meeting.setUf(
                    JOptionPane.showInputDialog(
                            "Digite a UF:"
                    )
            );

            meeting.setSegmento(
                    JOptionPane.showInputDialog(
                            "Digite o segmento do cliente:"
                    )
            );

            meeting.setNotaNps(
                    Double.parseDouble(
                            JOptionPane.showInputDialog(
                                    "Digite a nota NPS:"
                            )
                    )
            );

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    null,
                    "Duração e NPS devem ser valores numéricos."
            );

            return;

        } catch (IllegalArgumentException e) {

            JOptionPane.showMessageDialog(
                    null,
                    "Dados inválidos: "
                            + e.getMessage()
            );

            return;
        }


        /*
         * EXIBE OS DADOS DA MEETING
         */

        JOptionPane.showMessageDialog(
                null,
                """
                Meeting criada com sucesso!

                Formato: %s
                Status: %s
                Duração: %d segundos
                UF: %s
                Segmento: %s
                NPS: %.1f
                """.formatted(
                        meeting.getFormato(),
                        meeting.getStatus(),
                        meeting.getDuracao(),
                        meeting.getUf(),
                        meeting.getSegmento(),
                        meeting.getNotaNps()
                )
        );


        /*
         * =========================================================
         * LEITURA DO ARQUIVO TXT
         * =========================================================
         */

        String caminhoArquivo =
                JOptionPane.showInputDialog(
                        "Digite o caminho completo do arquivo TXT da transcrição:"
                );

        Transcricao transcricao =
                new Transcricao();

        transcricao.setMeeting(
                meeting
        );

        try {

            transcricao.lerTranscricao(
                    caminhoArquivo
            );

            meeting.setTranscricao(
                    transcricao
            );

            JOptionPane.showMessageDialog(
                    null,
                    """
                    Transcrição carregada com sucesso!

                    Quantidade de caracteres: %d
                    """.formatted(
                            transcricao.getTexto().length()
                    )
            );

        } catch (IOException | IllegalArgumentException e) {

            JOptionPane.showMessageDialog(
                    null,
                    "Erro ao carregar transcrição: "
                            + e.getMessage()
            );

            return;
        }


        /*
         * =========================================================
         * ANALISE DA TRANSCRICAO
         * =========================================================
         */

        AnalisadorTexto analisadorTexto =
                new AnalisadorTexto();

        ResultadoAnalise resultadoAnalise;

        try {

            resultadoAnalise =
                    analisadorTexto.analisar(
                            transcricao
                    );

        } catch (IllegalArgumentException e) {

            JOptionPane.showMessageDialog(
                    null,
                    "Erro ao analisar transcrição: "
                            + e.getMessage()
            );

            return;
        }


        /*
         * =========================================================
         * EXIBICAO DO RESULTADO
         * =========================================================
         */

        String mensagemAnalise = """
                ANÁLISE CONCLUÍDA

                Sentimento: %s
                Classificação: %s
                Risco de Churn: %.1f%%

                Resumo:
                %s

                Quantidade de alertas: %d
                Quantidade de palavras-chave: %d
                """.formatted(
                resultadoAnalise.getSentimento(),
                resultadoAnalise.getClassificacao(),
                resultadoAnalise.getRiscoChurn(),
                resultadoAnalise.getResumo(),
                resultadoAnalise.getAlertas().size(),
                meeting.getPalavrasChave().size()
        );

        JTextArea areaTexto =
                new JTextArea(
                        mensagemAnalise
                );

        areaTexto.setEditable(false);
        areaTexto.setLineWrap(true);
        areaTexto.setWrapStyleWord(true);
        areaTexto.setRows(18);
        areaTexto.setColumns(65);
        areaTexto.setCaretPosition(0);

        JScrollPane scrollPane =
                new JScrollPane(
                        areaTexto
                );

        JOptionPane.showMessageDialog(
                null,
                scrollPane,
                "Resultado da Análise",
                JOptionPane.INFORMATION_MESSAGE
        );


        /*
         * =========================================================
         * PERSISTENCIA NO BANCO
         * =========================================================
         */

        Connection con =
                ConnectionFactory.abrirConexao();

        if (con == null) {

            JOptionPane.showMessageDialog(
                    null,
                    "Não foi possível conectar ao banco."
            );

            return;
        }


        MeetingDao meetingDao =
                new MeetingDaoImpl(con);

        TranscricaoDao transcricaoDao =
                new TranscricaoDaoImpl(con);

        ResultadoAnaliseDao resultadoAnaliseDao =
                new ResultadoAnaliseDaoImpl(con);

        AlertaDao alertaDao =
                new AlertaDaoImpl(con);


        /*
         * 1 - SALVAR MEETING
         */

        String mensagemMeeting =
                meetingDao.inserir(
                        meeting
                );

        if (!mensagemMeeting.equals(
                "Meeting inserida com sucesso."
        )) {

            JOptionPane.showMessageDialog(
                    null,
                    mensagemMeeting
            );

            ConnectionFactory.fecharConexao(
                    con
            );

            return;
        }


        /*
         * 2 - SALVAR TRANSCRICAO
         */

        String mensagemTranscricao =
                transcricaoDao.inserir(
                        transcricao
                );

        if (!mensagemTranscricao.equals(
                "Transcrição inserida com sucesso."
        )) {

            JOptionPane.showMessageDialog(
                    null,
                    mensagemTranscricao
            );

            ConnectionFactory.fecharConexao(
                    con
            );

            return;
        }


        /*
         * 3 - SALVAR RESULTADO DA ANALISE
         */

        String mensagemResultado =
                resultadoAnaliseDao.inserir(
                        resultadoAnalise
                );

        if (!mensagemResultado.equals(
                "Resultado da análise inserido com sucesso."
        )) {

            JOptionPane.showMessageDialog(
                    null,
                    mensagemResultado
            );

            ConnectionFactory.fecharConexao(
                    con
            );

            return;
        }


        /*
         * 4 - SALVAR ALERTAS
         */

        for (Alerta alerta :
                resultadoAnalise.getAlertas()) {

            alerta.setResultadoAnalise(
                    resultadoAnalise
            );

            String mensagemAlerta =
                    alertaDao.inserir(
                            alerta
                    );

            if (!mensagemAlerta.equals(
                    "Alerta inserido com sucesso."
            )) {

                JOptionPane.showMessageDialog(
                        null,
                        mensagemAlerta
                );

                ConnectionFactory.fecharConexao(
                        con
                );

                return;
            }
        }


        /*
         * =========================================================
         * CONFIRMACAO FINAL
         * =========================================================
         */

        JOptionPane.showMessageDialog(
                null,
                """
                Análise salva no banco com sucesso!

                ID Meeting: %d
                ID Transcrição: %d
                ID Resultado: %d

                Palavras-chave: %d
                Alertas salvos: %d
                """.formatted(
                        meeting.getIdMeeting(),
                        transcricao.getIdTranscricao(),
                        resultadoAnalise.getIdResultadoAnalise(),
                        meeting.getPalavrasChave().size(),
                        resultadoAnalise.getAlertas().size()
                )
        );

        ConnectionFactory.fecharConexao(
                con
        );
    }


    public static void consultarReunioes() {

        Connection con =
                ConnectionFactory.abrirConexao();

        if (con == null) {

            JOptionPane.showMessageDialog(
                    null,
                    "Não foi possível conectar ao banco."
            );

            return;
        }


        MeetingDao meetingDao =
                new MeetingDaoImpl(con);

        List<Meeting> meetings =
                meetingDao.listarTodos();


        if (meetings.isEmpty()) {

            JOptionPane.showMessageDialog(
                    null,
                    "Nenhuma reunião cadastrada."
            );

            ConnectionFactory.fecharConexao(
                    con
            );

            return;
        }


        String mensagem = "";

        for (Meeting meeting :
                meetings) {

            mensagem = mensagem
                    + "ID: "
                    + meeting.getIdMeeting()

                    + "\nData: "
                    + meeting.getDataMeeting()

                    + "\nFormato: "
                    + meeting.getFormato()

                    + "\nStatus: "
                    + meeting.getStatus()

                    + "\nDuração: "
                    + meeting.getDuracao()
                    + " segundos"

                    + "\nUF: "
                    + meeting.getUf()

                    + "\nSegmento: "
                    + meeting.getSegmento()

                    + "\nNPS: "
                    + meeting.getNotaNps()

                    + "\nClassificação NPS: "
                    + meeting.classificarNps()

                    + "\n\n";
        }


        JTextArea areaTexto =
                new JTextArea(
                        mensagem
                );

        areaTexto.setEditable(false);
        areaTexto.setLineWrap(true);
        areaTexto.setWrapStyleWord(true);
        areaTexto.setRows(20);
        areaTexto.setColumns(60);
        areaTexto.setCaretPosition(0);

        JScrollPane scrollPane =
                new JScrollPane(
                        areaTexto
                );

        JOptionPane.showMessageDialog(
                null,
                scrollPane,
                "Reuniões cadastradas",
                JOptionPane.INFORMATION_MESSAGE
        );

        ConnectionFactory.fecharConexao(
                con
        );
    }


    public static void consultarResultados() {

        Connection con =
                ConnectionFactory.abrirConexao();

        if (con == null) {

            JOptionPane.showMessageDialog(
                    null,
                    "Não foi possível conectar ao banco."
            );

            return;
        }


        ResultadoAnaliseDao resultadoAnaliseDao =
                new ResultadoAnaliseDaoImpl(
                        con
                );

        AlertaDao alertaDao =
                new AlertaDaoImpl(
                        con
                );


        List<Alerta> alertas =
                alertaDao.listarTodos();

        List<ResultadoAnalise> resultados =
                resultadoAnaliseDao.listarTodos();


        if (resultados.isEmpty()) {

            JOptionPane.showMessageDialog(
                    null,
                    "Nenhum resultado de análise cadastrado."
            );

            ConnectionFactory.fecharConexao(
                    con
            );

            return;
        }


        String mensagem = "";

        for (ResultadoAnalise resultado :
                resultados) {

            mensagem = mensagem
                    + "ID Resultado: "
                    + resultado.getIdResultadoAnalise()

                    + "\nID Transcrição: "
                    + resultado
                    .getTranscricao()
                    .getIdTranscricao()

                    + "\nSentimento: "
                    + resultado.getSentimento()

                    + "\nClassificação: "
                    + resultado.getClassificacao()

                    + "\nRisco de Churn: "
                    + resultado.getRiscoChurn()
                    + "%"

                    + "\nResumo: "
                    + resultado.getResumo()

                    + "\n\n";

            mensagem = mensagem
                    + "Alertas:";

            boolean possuiAlerta =
                    false;


            for (Alerta alerta :
                    alertas) {

                if (alerta
                        .getResultadoAnalise()
                        .getIdResultadoAnalise()
                        ==
                        resultado
                                .getIdResultadoAnalise()) {

                    mensagem = mensagem
                            + "\n- "
                            + alerta.exibirAlerta();

                    possuiAlerta = true;
                }
            }


            if (!possuiAlerta) {

                mensagem = mensagem
                        + "\nNenhum alerta identificado.";
            }


            mensagem = mensagem
                    + "\n\n"
                    + "----------------------------------------"
                    + "\n\n";
        }


        JTextArea areaTexto =
                new JTextArea(
                        mensagem
                );

        areaTexto.setEditable(false);
        areaTexto.setLineWrap(true);
        areaTexto.setWrapStyleWord(true);
        areaTexto.setRows(20);
        areaTexto.setColumns(65);
        areaTexto.setCaretPosition(0);

        JScrollPane scrollPane =
                new JScrollPane(
                        areaTexto
                );

        JOptionPane.showMessageDialog(
                null,
                scrollPane,
                "Resultados das Análises",
                JOptionPane.INFORMATION_MESSAGE
        );

        ConnectionFactory.fecharConexao(
                con
        );
    }
}