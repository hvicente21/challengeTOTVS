package br.com.fiap.dao;

import br.com.fiap.dto.Meeting;
import br.com.fiap.dto.Transcricao;

import java.io.StringReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TranscricaoDaoImpl implements TranscricaoDao {
    private Connection con;

    public TranscricaoDaoImpl(Connection con) {
        this.con = con;
    }

    public String inserir(Transcricao transcricao) {

        if (transcricao == null) {
            return "Transcrição inválida.";
        }

        if (con == null) {
            return "Conexão inválida.";
        }

        if (transcricao.getMeeting() == null ||
                transcricao.getMeeting().getIdMeeting() <= 0) {
            return "Meeting da transcrição inválida.";
        }

        if (transcricao.getTexto() == null ||
                transcricao.getTexto().trim().isEmpty()) {
            return "Texto da transcrição inválido.";
        }

        String sql = """
            INSERT INTO TRANSCRICAO
            (ID_MEETING, TEXTO)
            VALUES (?, ?)
            """;

        try (PreparedStatement pstmt =
                     con.prepareStatement(sql, new String[]{"ID_TRANSCRICAO"})) {

            pstmt.setInt(
                    1,
                    transcricao.getMeeting().getIdMeeting()
            );

            pstmt.setCharacterStream(
                    2,
                    new StringReader(transcricao.getTexto())
            );

            int linhasAfetadas = pstmt.executeUpdate();

            if (linhasAfetadas > 0) {

                try (ResultSet rs = pstmt.getGeneratedKeys()) {

                    if (rs.next()) {
                        transcricao.setIdTranscricao(rs.getInt(1));
                        return "Transcrição inserida com sucesso.";
                    }
                }

                return "Transcrição inserida, mas o ID gerado não foi recuperado.";
            }

            return "Nenhuma transcrição foi inserida.";

        } catch (SQLException e) {
            return "Erro ao inserir transcrição: " + e.getMessage();
        }
    }
    public Transcricao buscar(int id) {

        if (id <= 0) {
            return null;
        }

        if (con == null) {
            return null;
        }

        String sql = """
            SELECT ID_TRANSCRICAO,
                   ID_MEETING,
                   TEXTO
            FROM TRANSCRICAO
            WHERE ID_TRANSCRICAO = ?
            """;

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {

                if (rs.next()) {

                    Transcricao transcricao = new Transcricao();

                    transcricao.setIdTranscricao(
                            rs.getInt("ID_TRANSCRICAO")
                    );

                    Meeting meeting = new Meeting();
                    meeting.setIdMeeting(
                            rs.getInt("ID_MEETING")
                    );

                    transcricao.setMeeting(meeting);

                    transcricao.setTexto(
                            rs.getString("TEXTO")
                    );

                    return transcricao;
                }
            }

        } catch (SQLException e) {
            System.out.println(
                    "Erro ao buscar transcrição: " + e.getMessage()
            );
        }

        return null;
    }
    public String atualizar(Transcricao transcricao) {

        if (transcricao == null) {
            return "Transcrição inválida.";
        }

        if (con == null) {
            return "Conexão inválida.";
        }

        if (transcricao.getIdTranscricao() <= 0) {
            return "ID da transcrição inválido.";
        }

        if (transcricao.getMeeting() == null ||
                transcricao.getMeeting().getIdMeeting() <= 0) {
            return "Meeting da transcrição inválida.";
        }

        if (transcricao.getTexto() == null ||
                transcricao.getTexto().trim().isEmpty()) {
            return "Texto da transcrição inválido.";
        }

        String sql = """
            UPDATE TRANSCRICAO
            SET ID_MEETING = ?,
                TEXTO = ?
            WHERE ID_TRANSCRICAO = ?
            """;

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(
                    1,
                    transcricao.getMeeting().getIdMeeting()
            );

            pstmt.setCharacterStream(
                    2,
                    new StringReader(transcricao.getTexto())
            );

            pstmt.setInt(
                    3,
                    transcricao.getIdTranscricao()
            );

            int linhasAfetadas = pstmt.executeUpdate();

            if (linhasAfetadas > 0) {
                return "Transcrição atualizada com sucesso.";
            }

            return "Transcrição não encontrada.";

        } catch (SQLException e) {
            return "Erro ao atualizar transcrição: " + e.getMessage();
        }
    }
    public String remover(int id) {

        if (id <= 0) {
            return "ID da transcrição inválido.";
        }

        if (con == null) {
            return "Conexão inválida.";
        }

        String sql = """
            DELETE FROM TRANSCRICAO
            WHERE ID_TRANSCRICAO = ?
            """;

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            int linhasAfetadas = pstmt.executeUpdate();

            if (linhasAfetadas > 0) {
                return "Transcrição removida com sucesso.";
            }

            return "Transcrição não encontrada.";

        } catch (SQLException e) {
            return "Erro ao remover transcrição: " + e.getMessage();
        }
    }
    public List<Transcricao> listarTodos() {

        List<Transcricao> transcricoes = new ArrayList<>();

        if (con == null) {
            return transcricoes;
        }

        String sql = """
            SELECT ID_TRANSCRICAO,
                   ID_MEETING,
                   TEXTO
            FROM TRANSCRICAO
            ORDER BY ID_TRANSCRICAO
            """;

        try (PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {

                Transcricao transcricao = new Transcricao();

                transcricao.setIdTranscricao(
                        rs.getInt("ID_TRANSCRICAO")
                );

                Meeting meeting = new Meeting();

                meeting.setIdMeeting(
                        rs.getInt("ID_MEETING")
                );

                transcricao.setMeeting(meeting);

                transcricao.setTexto(
                        rs.getString("TEXTO")
                );

                transcricoes.add(transcricao);
            }

        } catch (SQLException e) {
            System.out.println(
                    "Erro ao listar transcrições: " + e.getMessage()
            );
        }

        return transcricoes;
    }
}