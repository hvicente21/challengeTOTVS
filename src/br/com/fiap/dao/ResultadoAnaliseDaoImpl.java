package br.com.fiap.dao;

import br.com.fiap.dto.ResultadoAnalise;
import br.com.fiap.dto.Transcricao;

import java.io.StringReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResultadoAnaliseDaoImpl implements ResultadoAnaliseDao {

    private Connection con;

    public ResultadoAnaliseDaoImpl(Connection con) {
        this.con = con;
    }
    public String inserir(ResultadoAnalise resultadoAnalise) {

        if (resultadoAnalise == null) {
            return "Resultado da análise inválido.";
        }

        if (con == null) {
            return "Conexão inválida.";
        }

        if (resultadoAnalise.getTranscricao() == null ||
                resultadoAnalise.getTranscricao().getIdTranscricao() <= 0) {
            return "Transcrição do resultado inválida.";
        }

        if (resultadoAnalise.getResumo() == null ||
                resultadoAnalise.getResumo().trim().isEmpty()) {
            return "Resumo do resultado inválido.";
        }

        if (resultadoAnalise.getSentimento() == null) {
            return "Sentimento do resultado inválido.";
        }

        if (resultadoAnalise.getClassificacao() == null) {
            return "Classificação do resultado inválida.";
        }

        if (resultadoAnalise.getRiscoChurn() < 0 ||
                resultadoAnalise.getRiscoChurn() > 100) {
            return "Risco de churn inválido.";
        }

        String sql = """
        INSERT INTO RESULTADO_ANALISE
        (ID_TRANSCRICAO, RESUMO, SENTIMENTO, CLASSIFICACAO, RISCO_CHURN)
        VALUES (?, ?, ?, ?, ?)
        """;

        try (PreparedStatement pstmt =
                     con.prepareStatement(
                             sql,
                             new String[]{"ID_RESULTADO_ANALISE"}
                     )) {

            pstmt.setInt(
                    1,
                    resultadoAnalise.getTranscricao().getIdTranscricao()
            );

            pstmt.setCharacterStream(
                    2,
                    new StringReader(resultadoAnalise.getResumo())
            );

            pstmt.setString(
                    3,
                    resultadoAnalise.getSentimento()
            );

            pstmt.setString(
                    4,
                    resultadoAnalise.getClassificacao()
            );

            pstmt.setDouble(
                    5,
                    resultadoAnalise.getRiscoChurn()
            );

            int linhasAfetadas = pstmt.executeUpdate();

            if (linhasAfetadas > 0) {

                try (ResultSet rs = pstmt.getGeneratedKeys()) {

                    if (rs.next()) {
                        resultadoAnalise.setIdResultadoAnalise(
                                rs.getInt(1)
                        );

                        return "Resultado da análise inserido com sucesso.";
                    }
                }

                return "Resultado inserido, mas o ID gerado não foi recuperado.";
            }

            return "Nenhum resultado da análise foi inserido.";

        } catch (SQLException e) {
            return "Erro ao inserir resultado da análise: "
                    + e.getMessage();
        }
    }
    public ResultadoAnalise buscar(int id) {

        if (id <= 0) {
            return null;
        }

        if (con == null) {
            return null;
        }

        String sql = """
            SELECT ID_RESULTADO_ANALISE,
                   ID_TRANSCRICAO,
                   RESUMO,
                   SENTIMENTO,
                   CLASSIFICACAO,
                   RISCO_CHURN
            FROM RESULTADO_ANALISE
            WHERE ID_RESULTADO_ANALISE = ?
            """;

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {

                if (rs.next()) {

                    ResultadoAnalise resultadoAnalise =
                            new ResultadoAnalise();

                    resultadoAnalise.setIdResultadoAnalise(
                            rs.getInt("ID_RESULTADO_ANALISE")
                    );

                    Transcricao transcricao = new Transcricao();

                    transcricao.setIdTranscricao(
                            rs.getInt("ID_TRANSCRICAO")
                    );

                    resultadoAnalise.setTranscricao(transcricao);

                    resultadoAnalise.setResumo(
                            rs.getString("RESUMO")
                    );

                    resultadoAnalise.setSentimento(
                            rs.getString("SENTIMENTO")
                    );

                    resultadoAnalise.setClassificacao(
                            rs.getString("CLASSIFICACAO")
                    );

                    resultadoAnalise.setRiscoChurn(
                            rs.getDouble("RISCO_CHURN")
                    );

                    return resultadoAnalise;
                }
            }

        } catch (SQLException e) {
            System.out.println(
                    "Erro ao buscar resultado da análise: "
                            + e.getMessage()
            );
        }

        return null;
    }
    public String atualizar(ResultadoAnalise resultadoAnalise) {

        if (resultadoAnalise == null) {
            return "Resultado da análise inválido.";
        }

        if (con == null) {
            return "Conexão inválida.";
        }

        if (resultadoAnalise.getIdResultadoAnalise() <= 0) {
            return "ID do resultado da análise inválido.";
        }

        if (resultadoAnalise.getTranscricao() == null ||
                resultadoAnalise.getTranscricao().getIdTranscricao() <= 0) {
            return "Transcrição do resultado inválida.";
        }

        if (resultadoAnalise.getResumo() == null ||
                resultadoAnalise.getResumo().trim().isEmpty()) {
            return "Resumo do resultado inválido.";
        }

        if (resultadoAnalise.getSentimento() == null) {
            return "Sentimento do resultado inválido.";
        }

        if (resultadoAnalise.getClassificacao() == null) {
            return "Classificação do resultado inválida.";
        }

        if (resultadoAnalise.getRiscoChurn() < 0 ||
                resultadoAnalise.getRiscoChurn() > 100) {
            return "Risco de churn inválido.";
        }

        String sql = """
        UPDATE RESULTADO_ANALISE
        SET ID_TRANSCRICAO = ?,
            RESUMO = ?,
            SENTIMENTO = ?,
            CLASSIFICACAO = ?,
            RISCO_CHURN = ?
        WHERE ID_RESULTADO_ANALISE = ?
        """;

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(
                    1,
                    resultadoAnalise.getTranscricao().getIdTranscricao()
            );

            pstmt.setCharacterStream(
                    2,
                    new StringReader(resultadoAnalise.getResumo())
            );

            pstmt.setString(
                    3,
                    resultadoAnalise.getSentimento()
            );

            pstmt.setString(
                    4,
                    resultadoAnalise.getClassificacao()
            );

            pstmt.setDouble(
                    5,
                    resultadoAnalise.getRiscoChurn()
            );

            pstmt.setInt(
                    6,
                    resultadoAnalise.getIdResultadoAnalise()
            );

            int linhasAfetadas = pstmt.executeUpdate();

            if (linhasAfetadas > 0) {
                return "Resultado da análise atualizado com sucesso.";
            }

            return "Resultado da análise não encontrado.";

        } catch (SQLException e) {
            return "Erro ao atualizar resultado da análise: "
                    + e.getMessage();
        }
    }
    public String remover(int id) {

        if (id <= 0) {
            return "ID do resultado da análise inválido.";
        }

        if (con == null) {
            return "Conexão inválida.";
        }

        String sql = """
            DELETE FROM RESULTADO_ANALISE
            WHERE ID_RESULTADO_ANALISE = ?
            """;

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            int linhasAfetadas = pstmt.executeUpdate();

            if (linhasAfetadas > 0) {
                return "Resultado da análise removido com sucesso.";
            }

            return "Resultado da análise não encontrado.";

        } catch (SQLException e) {
            return "Erro ao remover resultado da análise: "
                    + e.getMessage();
        }
    }
    public List<ResultadoAnalise> listarTodos() {

        List<ResultadoAnalise> resultados = new ArrayList<>();

        if (con == null) {
            return resultados;
        }

        String sql = """
            SELECT ID_RESULTADO_ANALISE,
                   ID_TRANSCRICAO,
                   RESUMO,
                   SENTIMENTO,
                   CLASSIFICACAO,
                   RISCO_CHURN
            FROM RESULTADO_ANALISE
            ORDER BY ID_RESULTADO_ANALISE
            """;

        try (PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {

                ResultadoAnalise resultadoAnalise =
                        new ResultadoAnalise();

                resultadoAnalise.setIdResultadoAnalise(
                        rs.getInt("ID_RESULTADO_ANALISE")
                );

                Transcricao transcricao = new Transcricao();

                transcricao.setIdTranscricao(
                        rs.getInt("ID_TRANSCRICAO")
                );

                resultadoAnalise.setTranscricao(transcricao);

                resultadoAnalise.setResumo(
                        rs.getString("RESUMO")
                );

                resultadoAnalise.setSentimento(
                        rs.getString("SENTIMENTO")
                );

                resultadoAnalise.setClassificacao(
                        rs.getString("CLASSIFICACAO")
                );

                resultadoAnalise.setRiscoChurn(
                        rs.getDouble("RISCO_CHURN")
                );

                resultados.add(resultadoAnalise);
            }

        } catch (SQLException e) {
            System.out.println(
                    "Erro ao listar resultados da análise: "
                            + e.getMessage()
            );
        }

        return resultados;
    }
}