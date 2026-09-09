package br.com.fiap.dao;

import br.com.fiap.dto.PalavraChave;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PalavraChaveDaoImpl implements PalavraChaveDao {

    private Connection con;

    public PalavraChaveDaoImpl(Connection con) {
        this.con = con;
    }
    public String inserir(PalavraChave palavraChave) {

        if (palavraChave == null) {
            return "Palavra-chave inválida.";
        }

        if (con == null) {
            return "Conexão inválida.";
        }

        if (palavraChave.getPalavra() == null ||
                palavraChave.getPalavra().trim().isEmpty()) {
            return "Palavra-chave inválida.";
        }

        if (palavraChave.getPalavra().trim().length() > 100) {
            return "Palavra-chave deve possuir no máximo 100 caracteres.";
        }

        String sqlVerificar = """
            SELECT COUNT(*)
            FROM PALAVRA_CHAVE
            WHERE LOWER(TRIM(PALAVRA)) = LOWER(TRIM(?))
            """;

        try (PreparedStatement pstmt = con.prepareStatement(sqlVerificar)) {

            pstmt.setString(
                    1,
                    palavraChave.getPalavra()
            );

            try (ResultSet rs = pstmt.executeQuery()) {

                if (rs.next() && rs.getInt(1) > 0) {
                    return "Palavra-chave já cadastrada.";
                }
            }

        } catch (SQLException e) {
            return "Erro ao verificar palavra-chave: "
                    + e.getMessage();
        }

        String sql = """
            INSERT INTO PALAVRA_CHAVE
            (PALAVRA)
            VALUES (?)
            """;

        try (PreparedStatement pstmt =
                     con.prepareStatement(
                             sql,
                             new String[]{"ID_PALAVRA_CHAVE"}
                     )) {

            pstmt.setString(
                    1,
                    palavraChave.getPalavra().trim()
            );

            int linhasAfetadas = pstmt.executeUpdate();

            if (linhasAfetadas > 0) {

                try (ResultSet rs = pstmt.getGeneratedKeys()) {

                    if (rs.next()) {

                        palavraChave.setIdPalavraChave(
                                rs.getInt(1)
                        );

                        return "Palavra-chave inserida com sucesso.";
                    }
                }

                return "Palavra-chave inserida, mas o ID gerado não foi recuperado.";
            }

            return "Nenhuma palavra-chave foi inserida.";

        } catch (SQLException e) {
            return "Erro ao inserir palavra-chave: "
                    + e.getMessage();
        }
    }
    public PalavraChave buscar(int id) {

        if (id <= 0) {
            return null;
        }

        if (con == null) {
            return null;
        }

        String sql = """
            SELECT ID_PALAVRA_CHAVE,
                   PALAVRA
            FROM PALAVRA_CHAVE
            WHERE ID_PALAVRA_CHAVE = ?
            """;

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {

                if (rs.next()) {

                    PalavraChave palavraChave =
                            new PalavraChave();

                    palavraChave.setIdPalavraChave(
                            rs.getInt("ID_PALAVRA_CHAVE")
                    );

                    palavraChave.setPalavra(
                            rs.getString("PALAVRA")
                    );

                    return palavraChave;
                }
            }

        } catch (SQLException e) {
            System.out.println(
                    "Erro ao buscar palavra-chave: "
                            + e.getMessage()
            );
        }

        return null;
    }
    public String atualizar(PalavraChave palavraChave) {

        if (palavraChave == null) {
            return "Palavra-chave inválida.";
        }

        if (con == null) {
            return "Conexão inválida.";
        }

        if (palavraChave.getIdPalavraChave() <= 0) {
            return "ID da palavra-chave inválido.";
        }

        if (palavraChave.getPalavra() == null ||
                palavraChave.getPalavra().trim().isEmpty()) {
            return "Palavra-chave inválida.";
        }

        if (palavraChave.getPalavra().trim().length() > 100) {
            return "Palavra-chave deve possuir no máximo 100 caracteres.";
        }

        String sqlVerificar = """
            SELECT COUNT(*)
            FROM PALAVRA_CHAVE
            WHERE LOWER(TRIM(PALAVRA)) = LOWER(TRIM(?))
              AND ID_PALAVRA_CHAVE <> ?
            """;

        try (PreparedStatement pstmt = con.prepareStatement(sqlVerificar)) {

            pstmt.setString(
                    1,
                    palavraChave.getPalavra()
            );

            pstmt.setInt(
                    2,
                    palavraChave.getIdPalavraChave()
            );

            try (ResultSet rs = pstmt.executeQuery()) {

                if (rs.next() && rs.getInt(1) > 0) {
                    return "Palavra-chave já cadastrada.";
                }
            }

        } catch (SQLException e) {
            return "Erro ao verificar palavra-chave: "
                    + e.getMessage();
        }

        String sql = """
            UPDATE PALAVRA_CHAVE
            SET PALAVRA = ?
            WHERE ID_PALAVRA_CHAVE = ?
            """;

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(
                    1,
                    palavraChave.getPalavra().trim()
            );

            pstmt.setInt(
                    2,
                    palavraChave.getIdPalavraChave()
            );

            int linhasAfetadas = pstmt.executeUpdate();

            if (linhasAfetadas > 0) {
                return "Palavra-chave atualizada com sucesso.";
            }

            return "Palavra-chave não encontrada.";

        } catch (SQLException e) {
            return "Erro ao atualizar palavra-chave: "
                    + e.getMessage();
        }
    }
    public String remover(int id) {

        if (id <= 0) {
            return "ID da palavra-chave inválido.";
        }

        if (con == null) {
            return "Conexão inválida.";
        }

        String sql = """
            DELETE FROM PALAVRA_CHAVE
            WHERE ID_PALAVRA_CHAVE = ?
            """;

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            int linhasAfetadas = pstmt.executeUpdate();

            if (linhasAfetadas > 0) {
                return "Palavra-chave removida com sucesso.";
            }

            return "Palavra-chave não encontrada.";

        } catch (SQLException e) {
            return "Erro ao remover palavra-chave: "
                    + e.getMessage();
        }
    }
    public List<PalavraChave> listarTodos() {

        List<PalavraChave> palavrasChave = new ArrayList<>();

        if (con == null) {
            return palavrasChave;
        }

        String sql = """
            SELECT ID_PALAVRA_CHAVE,
                   PALAVRA
            FROM PALAVRA_CHAVE
            ORDER BY ID_PALAVRA_CHAVE
            """;

        try (PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {

                PalavraChave palavraChave =
                        new PalavraChave();

                palavraChave.setIdPalavraChave(
                        rs.getInt("ID_PALAVRA_CHAVE")
                );

                palavraChave.setPalavra(
                        rs.getString("PALAVRA")
                );

                palavrasChave.add(palavraChave);
            }

        } catch (SQLException e) {
            System.out.println(
                    "Erro ao listar palavras-chave: "
                            + e.getMessage()
            );
        }

        return palavrasChave;
    }
}