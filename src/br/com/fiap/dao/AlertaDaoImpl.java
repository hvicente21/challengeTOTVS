package br.com.fiap.dao;

import br.com.fiap.dto.Alerta;
import br.com.fiap.dto.ResultadoAnalise;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlertaDaoImpl implements AlertaDao {

    private Connection con;

    public AlertaDaoImpl(Connection con) {
        this.con = con;
    }
    public String inserir(Alerta alerta) {

        if (alerta == null) {
            return "Alerta inválido.";
        }

        if (con == null) {
            return "Conexão inválida.";
        }

        if (alerta.getResultadoAnalise() == null ||
                alerta.getResultadoAnalise().getIdResultadoAnalise() <= 0) {
            return "Resultado da análise do alerta inválido.";
        }

        if (alerta.getTipoAlerta() == null ||
                alerta.getTipoAlerta().trim().isEmpty()) {
            return "Tipo do alerta inválido.";
        }

        if (alerta.getDescricao() == null ||
                alerta.getDescricao().trim().isEmpty()) {
            return "Descrição do alerta inválida.";
        }

        if (alerta.getDescricao().length() > 500) {
            return "Descrição do alerta deve possuir no máximo 500 caracteres.";
        }

        String sql = """
            INSERT INTO ALERTA
            (ID_RESULTADO_ANALISE, TIPO_ALERTA, DESCRICAO, NIVEL_RISCO)
            VALUES (?, ?, ?, ?)
            """;

        try (PreparedStatement pstmt =
                     con.prepareStatement(
                             sql,
                             new String[]{"ID_ALERTA"}
                     )) {

            pstmt.setInt(
                    1,
                    alerta.getResultadoAnalise().getIdResultadoAnalise()
            );

            pstmt.setString(
                    2,
                    alerta.getTipoAlerta()
            );

            pstmt.setString(
                    3,
                    alerta.getDescricao()
            );

            if (alerta.getNivelRisco() != null) {
                pstmt.setString(
                        4,
                        alerta.getNivelRisco()
                );
            } else {
                pstmt.setNull(
                        4,
                        Types.VARCHAR
                );
            }

            int linhasAfetadas = pstmt.executeUpdate();

            if (linhasAfetadas > 0) {

                try (ResultSet rs = pstmt.getGeneratedKeys()) {

                    if (rs.next()) {
                        alerta.setIdAlerta(
                                rs.getInt(1)
                        );

                        return "Alerta inserido com sucesso.";
                    }
                }

                return "Alerta inserido, mas o ID gerado não foi recuperado.";
            }

            return "Nenhum alerta foi inserido.";

        } catch (SQLException e) {
            return "Erro ao inserir alerta: "
                    + e.getMessage();
        }
    }
    public Alerta buscar(int id) {

        if (id <= 0) {
            return null;
        }

        if (con == null) {
            return null;
        }

        String sql = """
            SELECT ID_ALERTA,
                   ID_RESULTADO_ANALISE,
                   TIPO_ALERTA,
                   DESCRICAO,
                   NIVEL_RISCO
            FROM ALERTA
            WHERE ID_ALERTA = ?
            """;

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {

                if (rs.next()) {

                    Alerta alerta = new Alerta();

                    alerta.setIdAlerta(
                            rs.getInt("ID_ALERTA")
                    );

                    ResultadoAnalise resultadoAnalise =
                            new ResultadoAnalise();

                    resultadoAnalise.setIdResultadoAnalise(
                            rs.getInt("ID_RESULTADO_ANALISE")
                    );

                    alerta.setResultadoAnalise(resultadoAnalise);

                    alerta.setTipoAlerta(
                            rs.getString("TIPO_ALERTA")
                    );

                    alerta.setDescricao(
                            rs.getString("DESCRICAO")
                    );

                    alerta.setNivelRisco(
                            rs.getString("NIVEL_RISCO")
                    );

                    return alerta;
                }
            }

        } catch (SQLException e) {
            System.out.println(
                    "Erro ao buscar alerta: "
                            + e.getMessage()
            );
        }

        return null;
    }
    public String atualizar(Alerta alerta) {

        if (alerta == null) {
            return "Alerta inválido.";
        }

        if (con == null) {
            return "Conexão inválida.";
        }

        if (alerta.getIdAlerta() <= 0) {
            return "ID do alerta inválido.";
        }

        if (alerta.getResultadoAnalise() == null ||
                alerta.getResultadoAnalise().getIdResultadoAnalise() <= 0) {
            return "Resultado da análise do alerta inválido.";
        }

        if (alerta.getTipoAlerta() == null ||
                alerta.getTipoAlerta().trim().isEmpty()) {
            return "Tipo do alerta inválido.";
        }

        if (alerta.getDescricao() == null ||
                alerta.getDescricao().trim().isEmpty()) {
            return "Descrição do alerta inválida.";
        }

        if (alerta.getDescricao().length() > 500) {
            return "Descrição do alerta deve possuir no máximo 500 caracteres.";
        }

        String sql = """
            UPDATE ALERTA
            SET ID_RESULTADO_ANALISE = ?,
                TIPO_ALERTA = ?,
                DESCRICAO = ?,
                NIVEL_RISCO = ?
            WHERE ID_ALERTA = ?
            """;

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(
                    1,
                    alerta.getResultadoAnalise().getIdResultadoAnalise()
            );

            pstmt.setString(
                    2,
                    alerta.getTipoAlerta()
            );

            pstmt.setString(
                    3,
                    alerta.getDescricao()
            );

            if (alerta.getNivelRisco() != null) {
                pstmt.setString(
                        4,
                        alerta.getNivelRisco()
                );
            } else {
                pstmt.setNull(
                        4,
                        Types.VARCHAR
                );
            }

            pstmt.setInt(
                    5,
                    alerta.getIdAlerta()
            );

            int linhasAfetadas = pstmt.executeUpdate();

            if (linhasAfetadas > 0) {
                return "Alerta atualizado com sucesso.";
            }

            return "Alerta não encontrado.";

        } catch (SQLException e) {
            return "Erro ao atualizar alerta: "
                    + e.getMessage();
        }
    }
    public String remover(int id) {

        if (id <= 0) {
            return "ID do alerta inválido.";
        }

        if (con == null) {
            return "Conexão inválida.";
        }

        String sql = """
            DELETE FROM ALERTA
            WHERE ID_ALERTA = ?
            """;

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            int linhasAfetadas = pstmt.executeUpdate();

            if (linhasAfetadas > 0) {
                return "Alerta removido com sucesso.";
            }

            return "Alerta não encontrado.";

        } catch (SQLException e) {
            return "Erro ao remover alerta: "
                    + e.getMessage();
        }
    }
    public List<Alerta> listarTodos() {

        List<Alerta> alertas = new ArrayList<>();

        if (con == null) {
            return alertas;
        }

        String sql = """
            SELECT ID_ALERTA,
                   ID_RESULTADO_ANALISE,
                   TIPO_ALERTA,
                   DESCRICAO,
                   NIVEL_RISCO
            FROM ALERTA
            ORDER BY ID_ALERTA
            """;

        try (PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {

                Alerta alerta = new Alerta();

                alerta.setIdAlerta(
                        rs.getInt("ID_ALERTA")
                );

                ResultadoAnalise resultadoAnalise =
                        new ResultadoAnalise();

                resultadoAnalise.setIdResultadoAnalise(
                        rs.getInt("ID_RESULTADO_ANALISE")
                );

                alerta.setResultadoAnalise(resultadoAnalise);

                alerta.setTipoAlerta(
                        rs.getString("TIPO_ALERTA")
                );

                alerta.setDescricao(
                        rs.getString("DESCRICAO")
                );

                alerta.setNivelRisco(
                        rs.getString("NIVEL_RISCO")
                );

                alertas.add(alerta);
            }

        } catch (SQLException e) {
            System.out.println(
                    "Erro ao listar alertas: "
                            + e.getMessage()
            );
        }

        return alertas;
    }
}
