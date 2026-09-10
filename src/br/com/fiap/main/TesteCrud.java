package br.com.fiap.main;

import br.com.fiap.dao.AlertaDao;
import br.com.fiap.dao.AlertaDaoImpl;
import br.com.fiap.dao.ConnectionFactory;
import br.com.fiap.dao.MeetingDao;
import br.com.fiap.dao.MeetingDaoImpl;
import br.com.fiap.dao.PalavraChaveDao;
import br.com.fiap.dao.PalavraChaveDaoImpl;
import br.com.fiap.dao.ResultadoAnaliseDao;
import br.com.fiap.dao.ResultadoAnaliseDaoImpl;
import br.com.fiap.dao.TranscricaoDao;
import br.com.fiap.dao.TranscricaoDaoImpl;

import br.com.fiap.dto.Alerta;
import br.com.fiap.dto.Meeting;
import br.com.fiap.dto.PalavraChave;
import br.com.fiap.dto.ResultadoAnalise;
import br.com.fiap.dto.Transcricao;

import java.sql.Connection;
import java.time.LocalDateTime;

public class TesteCrud {

    public static void main(String[] args) {

        Connection con = ConnectionFactory.abrirConexao();

        if (con == null) {
            System.out.println("Não foi possível conectar ao banco.");
            return;
        }

        System.out.println("Conexão realizada com sucesso!");


        /*
         * =========================================================
         * MEETING
         * =========================================================
         */

        MeetingDao meetingDao =
                new MeetingDaoImpl(con);

        Meeting meeting =
                new Meeting();

        meeting.setDataMeeting(LocalDateTime.now());
        meeting.setFormato("Presencial");
        meeting.setStatus("Concluída");
        meeting.setDuracao(3600);
        meeting.setUf("SP");
        meeting.setSegmento("Tecnologia");
        meeting.setNotaNps(8);


        /*
         * INSERT MEETING
         */

        System.out.println(
                "\n--- INSERINDO MEETING ---"
        );

        System.out.println(
                meetingDao.inserir(meeting)
        );

        System.out.println(
                "ID gerado: "
                        + meeting.getIdMeeting()
        );


        /*
         * BUSCAR MEETING
         */

        Meeting meetingBuscada =
                meetingDao.buscar(
                        meeting.getIdMeeting()
                );

        if (meetingBuscada != null) {

            System.out.println(
                    "\n--- MEETING BUSCADA ---"
            );

            System.out.println(
                    "ID: "
                            + meetingBuscada.getIdMeeting()
            );

            System.out.println(
                    "Data: "
                            + meetingBuscada.getDataMeeting()
            );

            System.out.println(
                    "Formato: "
                            + meetingBuscada.getFormato()
            );

            System.out.println(
                    "Status: "
                            + meetingBuscada.getStatus()
            );

            System.out.println(
                    "Duração: "
                            + meetingBuscada.getDuracao()
            );

            System.out.println(
                    "UF: "
                            + meetingBuscada.getUf()
            );

            System.out.println(
                    "Segmento: "
                            + meetingBuscada.getSegmento()
            );

            System.out.println(
                    "NPS: "
                            + meetingBuscada.getNotaNps()
            );
        }


        /*
         * UPDATE MEETING
         */

        meeting.setStatus("Em andamento");
        meeting.setSegmento("Varejo");
        meeting.setNotaNps(9);

        System.out.println(
                meetingDao.atualizar(meeting)
        );

        Meeting meetingAtualizada =
                meetingDao.buscar(
                        meeting.getIdMeeting()
                );

        if (meetingAtualizada != null) {

            System.out.println(
                    "\n--- MEETING ATUALIZADA ---"
            );

            System.out.println(
                    "Status: "
                            + meetingAtualizada.getStatus()
            );

            System.out.println(
                    "Segmento: "
                            + meetingAtualizada.getSegmento()
            );

            System.out.println(
                    "NPS: "
                            + meetingAtualizada.getNotaNps()
            );
        }


        /*
         * LISTAR MEETINGS
         */

        System.out.println(
                "\n--- TODAS AS MEETINGS ---"
        );

        for (Meeting m : meetingDao.listarTodos()) {

            System.out.println(
                    "ID: " + m.getIdMeeting()
                            + " | Status: " + m.getStatus()
                            + " | Segmento: " + m.getSegmento()
                            + " | NPS: " + m.getNotaNps()
            );
        }


        /*
         * =========================================================
         * TRANSCRICAO
         * =========================================================
         */

        TranscricaoDao transcricaoDao =
                new TranscricaoDaoImpl(con);

        Transcricao transcricao =
                new Transcricao();

        transcricao.setMeeting(meeting);

        transcricao.setTexto(
                "Cliente demonstrou interesse em novos módulos da TOTVS."
        );


        /*
         * INSERT TRANSCRICAO
         */

        System.out.println(
                "\n--- INSERINDO TRANSCRICAO ---"
        );

        System.out.println(
                transcricaoDao.inserir(transcricao)
        );

        System.out.println(
                "ID da transcrição gerado: "
                        + transcricao.getIdTranscricao()
        );


        /*
         * BUSCAR TRANSCRICAO
         */

        Transcricao transcricaoBuscada =
                transcricaoDao.buscar(
                        transcricao.getIdTranscricao()
                );

        if (transcricaoBuscada != null) {

            System.out.println(
                    "\n--- TRANSCRICAO BUSCADA ---"
            );

            System.out.println(
                    "ID: "
                            + transcricaoBuscada.getIdTranscricao()
            );

            System.out.println(
                    "ID Meeting: "
                            + transcricaoBuscada
                            .getMeeting()
                            .getIdMeeting()
            );

            System.out.println(
                    "Texto: "
                            + transcricaoBuscada.getTexto()
            );
        }


        /*
         * UPDATE TRANSCRICAO
         */

        transcricao.setTexto(
                "Cliente demonstrou interesse em novos módulos e integração com soluções TOTVS."
        );

        System.out.println(
                transcricaoDao.atualizar(transcricao)
        );

        Transcricao transcricaoAtualizada =
                transcricaoDao.buscar(
                        transcricao.getIdTranscricao()
                );

        if (transcricaoAtualizada != null) {

            System.out.println(
                    "\n--- TRANSCRICAO ATUALIZADA ---"
            );

            System.out.println(
                    "ID: "
                            + transcricaoAtualizada.getIdTranscricao()
            );

            System.out.println(
                    "ID Meeting: "
                            + transcricaoAtualizada
                            .getMeeting()
                            .getIdMeeting()
            );

            System.out.println(
                    "Texto: "
                            + transcricaoAtualizada.getTexto()
            );
        }


        /*
         * LISTAR TRANSCRICOES
         */

        System.out.println(
                "\n--- TODAS AS TRANSCRICOES ---"
        );

        for (Transcricao t : transcricaoDao.listarTodos()) {

            System.out.println(
                    "ID: " + t.getIdTranscricao()
                            + " | ID Meeting: "
                            + t.getMeeting().getIdMeeting()
                            + " | Texto: "
                            + t.getTexto()
            );
        }


        /*
         * =========================================================
         * PALAVRA-CHAVE
         * =========================================================
         */

        PalavraChaveDao palavraChaveDao =
                new PalavraChaveDaoImpl(con);

        PalavraChave palavraChave =
                new PalavraChave();

        /*
         * Usamos o ID da Meeting para que cada execução
         * tenha uma palavra diferente e não dê conflito
         * com UNIQUE.
         */

        palavraChave.setPalavra(
                "Protheus Teste "
                        + meeting.getIdMeeting()
        );


        /*
         * INSERT PALAVRA-CHAVE
         */

        System.out.println(
                "\n--- INSERINDO PALAVRA-CHAVE ---"
        );

        System.out.println(
                palavraChaveDao.inserir(palavraChave)
        );

        System.out.println(
                "ID da palavra-chave gerado: "
                        + palavraChave.getIdPalavraChave()
        );


        /*
         * BUSCAR PALAVRA-CHAVE
         */

        PalavraChave palavraChaveBuscada =
                palavraChaveDao.buscar(
                        palavraChave.getIdPalavraChave()
                );

        if (palavraChaveBuscada != null) {

            System.out.println(
                    "\n--- PALAVRA-CHAVE BUSCADA ---"
            );

            System.out.println(
                    "ID: "
                            + palavraChaveBuscada.getIdPalavraChave()
            );

            System.out.println(
                    "Palavra: "
                            + palavraChaveBuscada.getPalavra()
            );
        }


        /*
         * UPDATE PALAVRA-CHAVE
         */

        palavraChave.setPalavra(
                "TOTVS Protheus Teste "
                        + meeting.getIdMeeting()
        );

        System.out.println(
                palavraChaveDao.atualizar(
                        palavraChave
                )
        );

        PalavraChave palavraChaveAtualizada =
                palavraChaveDao.buscar(
                        palavraChave.getIdPalavraChave()
                );

        if (palavraChaveAtualizada != null) {

            System.out.println(
                    "\n--- PALAVRA-CHAVE ATUALIZADA ---"
            );

            System.out.println(
                    "ID: "
                            + palavraChaveAtualizada.getIdPalavraChave()
            );

            System.out.println(
                    "Palavra: "
                            + palavraChaveAtualizada.getPalavra()
            );
        }


        /*
         * LISTAR PALAVRAS-CHAVE
         */

        System.out.println(
                "\n--- TODAS AS PALAVRAS-CHAVE ---"
        );

        for (PalavraChave p :
                palavraChaveDao.listarTodos()) {

            System.out.println(
                    "ID: "
                            + p.getIdPalavraChave()
                            + " | Palavra: "
                            + p.getPalavra()
            );
        }


        /*
         * =========================================================
         * RESULTADO ANALISE
         * =========================================================
         */

        ResultadoAnaliseDao resultadoAnaliseDao =
                new ResultadoAnaliseDaoImpl(con);

        ResultadoAnalise resultadoAnalise =
                new ResultadoAnalise();

        resultadoAnalise.setTranscricao(transcricao);

        resultadoAnalise.setResumo(
                "Cliente demonstrou interesse em soluções TOTVS."
        );

        resultadoAnalise.setSentimento(
                "Positivo"
        );

        resultadoAnalise.setClassificacao(
                "Oportunidade"
        );

        resultadoAnalise.setRiscoChurn(
                20
        );


        /*
         * INSERT RESULTADO ANALISE
         */

        System.out.println(
                "\n--- INSERINDO RESULTADO ANALISE ---"
        );

        System.out.println(
                resultadoAnaliseDao.inserir(
                        resultadoAnalise
                )
        );

        System.out.println(
                "ID do resultado gerado: "
                        + resultadoAnalise
                        .getIdResultadoAnalise()
        );


        /*
         * BUSCAR RESULTADO ANALISE
         */

        ResultadoAnalise resultadoBuscado =
                resultadoAnaliseDao.buscar(
                        resultadoAnalise
                                .getIdResultadoAnalise()
                );

        if (resultadoBuscado != null) {

            System.out.println(
                    "\n--- RESULTADO ANALISE BUSCADO ---"
            );

            System.out.println(
                    "ID: "
                            + resultadoBuscado
                            .getIdResultadoAnalise()
            );

            System.out.println(
                    "ID Transcrição: "
                            + resultadoBuscado
                            .getTranscricao()
                            .getIdTranscricao()
            );

            System.out.println(
                    "Resumo: "
                            + resultadoBuscado.getResumo()
            );

            System.out.println(
                    "Sentimento: "
                            + resultadoBuscado.getSentimento()
            );

            System.out.println(
                    "Classificação: "
                            + resultadoBuscado.getClassificacao()
            );

            System.out.println(
                    "Risco Churn: "
                            + resultadoBuscado.getRiscoChurn()
            );
        }


        /*
         * UPDATE RESULTADO ANALISE
         */

        resultadoAnalise.setResumo(
                "Cliente demonstrou forte interesse em expansão das soluções TOTVS."
        );

        resultadoAnalise.setSentimento(
                "Misto"
        );

        resultadoAnalise.setClassificacao(
                "Risco"
        );

        resultadoAnalise.setRiscoChurn(
                65
        );

        System.out.println(
                resultadoAnaliseDao.atualizar(
                        resultadoAnalise
                )
        );

        ResultadoAnalise resultadoAtualizado =
                resultadoAnaliseDao.buscar(
                        resultadoAnalise
                                .getIdResultadoAnalise()
                );

        if (resultadoAtualizado != null) {

            System.out.println(
                    "\n--- RESULTADO ANALISE ATUALIZADO ---"
            );

            System.out.println(
                    "Resumo: "
                            + resultadoAtualizado.getResumo()
            );

            System.out.println(
                    "Sentimento: "
                            + resultadoAtualizado.getSentimento()
            );

            System.out.println(
                    "Classificação: "
                            + resultadoAtualizado.getClassificacao()
            );

            System.out.println(
                    "Risco Churn: "
                            + resultadoAtualizado.getRiscoChurn()
            );
        }


        /*
         * LISTAR RESULTADOS
         */

        System.out.println(
                "\n--- TODOS OS RESULTADOS DE ANALISE ---"
        );

        for (ResultadoAnalise r :
                resultadoAnaliseDao.listarTodos()) {

            System.out.println(
                    "ID: "
                            + r.getIdResultadoAnalise()
                            + " | ID Transcrição: "
                            + r.getTranscricao()
                            .getIdTranscricao()
                            + " | Sentimento: "
                            + r.getSentimento()
                            + " | Classificação: "
                            + r.getClassificacao()
                            + " | Risco Churn: "
                            + r.getRiscoChurn()
            );
        }


        /*
         * =========================================================
         * ALERTA
         * =========================================================
         */

        AlertaDao alertaDao =
                new AlertaDaoImpl(con);

        Alerta alerta =
                new Alerta();

        alerta.setResultadoAnalise(
                resultadoAnalise
        );

        alerta.setTipoAlerta(
                "Risco"
        );

        alerta.setNivelRisco(
                "Alto"
        );

        alerta.setDescricao(
                "Cliente apresentou sinais de risco comercial."
        );


        /*
         * INSERT ALERTA
         */

        System.out.println(
                "\n--- INSERINDO ALERTA ---"
        );

        System.out.println(
                alertaDao.inserir(alerta)
        );

        System.out.println(
                "ID do alerta gerado: "
                        + alerta.getIdAlerta()
        );


        /*
         * BUSCAR ALERTA
         */

        Alerta alertaBuscado =
                alertaDao.buscar(
                        alerta.getIdAlerta()
                );

        if (alertaBuscado != null) {

            System.out.println(
                    "\n--- ALERTA BUSCADO ---"
            );

            System.out.println(
                    "ID: "
                            + alertaBuscado.getIdAlerta()
            );

            System.out.println(
                    "ID Resultado: "
                            + alertaBuscado
                            .getResultadoAnalise()
                            .getIdResultadoAnalise()
            );

            System.out.println(
                    "Tipo: "
                            + alertaBuscado.getTipoAlerta()
            );

            System.out.println(
                    "Nível: "
                            + alertaBuscado.getNivelRisco()
            );

            System.out.println(
                    "Descrição: "
                            + alertaBuscado.getDescricao()
            );
        }


        /*
         * UPDATE ALERTA
         */

        alerta.setNivelRisco(
                "Crítico"
        );

        alerta.setDescricao(
                "Cliente apresentou risco comercial elevado."
        );

        System.out.println(
                alertaDao.atualizar(alerta)
        );

        Alerta alertaAtualizado =
                alertaDao.buscar(
                        alerta.getIdAlerta()
                );

        if (alertaAtualizado != null) {

            System.out.println(
                    "\n--- ALERTA ATUALIZADO ---"
            );

            System.out.println(
                    "Tipo: "
                            + alertaAtualizado.getTipoAlerta()
            );

            System.out.println(
                    "Nível: "
                            + alertaAtualizado.getNivelRisco()
            );

            System.out.println(
                    "Descrição: "
                            + alertaAtualizado.getDescricao()
            );
        }


        /*
         * LISTAR ALERTAS
         */

        System.out.println(
                "\n--- TODOS OS ALERTAS ---"
        );

        for (Alerta a :
                alertaDao.listarTodos()) {

            System.out.println(
                    "ID: "
                            + a.getIdAlerta()
                            + " | ID Resultado: "
                            + a.getResultadoAnalise()
                            .getIdResultadoAnalise()
                            + " | Tipo: "
                            + a.getTipoAlerta()
                            + " | Nível: "
                            + a.getNivelRisco()
                            + " | Descrição: "
                            + a.getDescricao()
            );
        }


        /*
         * =========================================================
         * REMOCAO DOS DADOS CRIADOS NESTE TESTE
         * =========================================================
         */


        /*
         * REMOVE ALERTA
         */

        System.out.println(
                "\n--- REMOVENDO ALERTA ---"
        );

        System.out.println(
                alertaDao.remover(
                        alerta.getIdAlerta()
                )
        );

        Alerta alertaRemovido =
                alertaDao.buscar(
                        alerta.getIdAlerta()
                );

        if (alertaRemovido == null) {

            System.out.println(
                    "Confirmação: Alerta não existe mais no banco."
            );

        } else {

            System.out.println(
                    "Erro: Alerta ainda foi encontrado."
            );
        }


        /*
         * REMOVE RESULTADO ANALISE
         */

        System.out.println(
                "\n--- REMOVENDO RESULTADO ANALISE ---"
        );

        System.out.println(
                resultadoAnaliseDao.remover(
                        resultadoAnalise
                                .getIdResultadoAnalise()
                )
        );

        ResultadoAnalise resultadoRemovido =
                resultadoAnaliseDao.buscar(
                        resultadoAnalise
                                .getIdResultadoAnalise()
                );

        if (resultadoRemovido == null) {

            System.out.println(
                    "Confirmação: Resultado da análise não existe mais no banco."
            );

        } else {

            System.out.println(
                    "Erro: Resultado da análise ainda foi encontrado."
            );
        }


        /*
         * REMOVE PALAVRA-CHAVE
         */

        System.out.println(
                "\n--- REMOVENDO PALAVRA-CHAVE ---"
        );

        System.out.println(
                palavraChaveDao.remover(
                        palavraChave.getIdPalavraChave()
                )
        );

        PalavraChave palavraChaveRemovida =
                palavraChaveDao.buscar(
                        palavraChave.getIdPalavraChave()
                );

        if (palavraChaveRemovida == null) {

            System.out.println(
                    "Confirmação: Palavra-chave não existe mais no banco."
            );

        } else {

            System.out.println(
                    "Erro: Palavra-chave ainda foi encontrada."
            );
        }


        /*
         * REMOVE TRANSCRICAO
         */

        System.out.println(
                "\n--- REMOVENDO TRANSCRICAO ---"
        );

        System.out.println(
                transcricaoDao.remover(
                        transcricao.getIdTranscricao()
                )
        );

        Transcricao transcricaoRemovida =
                transcricaoDao.buscar(
                        transcricao.getIdTranscricao()
                );

        if (transcricaoRemovida == null) {

            System.out.println(
                    "Confirmação: Transcrição não existe mais no banco."
            );

        } else {

            System.out.println(
                    "Erro: Transcrição ainda foi encontrada."
            );
        }


        /*
         * REMOVE MEETING
         */

        System.out.println(
                "\n--- REMOVENDO MEETING ---"
        );

        System.out.println(
                meetingDao.remover(
                        meeting.getIdMeeting()
                )
        );

        Meeting meetingRemovida =
                meetingDao.buscar(
                        meeting.getIdMeeting()
                );

        if (meetingRemovida == null) {

            System.out.println(
                    "Confirmação: Meeting não existe mais no banco."
            );

        } else {

            System.out.println(
                    "Erro: Meeting ainda foi encontrada."
            );
        }


        /*
         * =========================================================
         * ENCERRAMENTO
         * =========================================================
         */

        ConnectionFactory.fecharConexao(con);
    }
}