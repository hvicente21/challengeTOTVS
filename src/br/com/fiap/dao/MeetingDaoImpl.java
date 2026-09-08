package br.com.fiap.dao;

import br.com.fiap.dto.Meeting;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MeetingDaoImpl implements MeetingDao {
    private Connection con;

    public MeetingDaoImpl(Connection con) {
        this.con = con;
    }

    public String inserir(Meeting meeting) {
        if (meeting == null) {
            return "Meeting inválida.";
        }
        if (con == null) {
            return "Conexão inválida.";
        }
        if (meeting.getDataMeeting() == null) {
            return "Data da Meeting inválida.";
        }
        if (meeting.getFormato() == null ||
                meeting.getFormato().trim().isEmpty()) {
            return "Formato da Meeting inválido.";
        }
        if (meeting.getStatus() == null ||
                meeting.getStatus().trim().isEmpty()) {
            return "Status da Meeting inválido.";
        }
        if (meeting.getDuracao() <= 0) {
            return "Duração da Meeting inválida.";
        }

        String sql = """
            INSERT INTO MEETING
            (DT_MEETING, FORMATO, STATUS, DURACAO, UF, NOME_SEGMENTO, NOTA_NPS)
            VALUES (?, ?, ?, NUMTODSINTERVAL(?, 'SECOND'), ?, ?, ?)
            """;

        try (PreparedStatement pstmt =
                     con.prepareStatement(sql, new String[]{"ID_MEETING"})) {
            pstmt.setTimestamp(
                    1,
                    Timestamp.valueOf(meeting.getDataMeeting())
            );
            pstmt.setString(2, meeting.getFormato());
            pstmt.setString(3, meeting.getStatus());
            pstmt.setInt(4, meeting.getDuracao());
            pstmt.setString(5, meeting.getUf());
            pstmt.setString(6, meeting.getSegmento());
            pstmt.setDouble(7, meeting.getNotaNps());

            int linhasAfetadas = pstmt.executeUpdate();

            if (linhasAfetadas > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        meeting.setIdMeeting(rs.getInt(1));
                        return "Meeting inserida com sucesso.";
                    }
                }
                return "Meeting inserida, mas o ID gerado não foi recuperado.";
            }
            return "Nenhuma Meeting foi inserida.";
        } catch (SQLException e) {
            return "Erro ao inserir Meeting: " + e.getMessage();
        }
    }
    public Meeting buscar(int id) {
        if (id <= 0) {
            return null;
        }
        if (con == null) {
            return null;
        }

        String sql = """
            SELECT ID_MEETING,
                   DT_MEETING,
                   FORMATO,
                   STATUS,
                   (EXTRACT(DAY FROM DURACAO) * 86400 +
                    EXTRACT(HOUR FROM DURACAO) * 3600 +
                    EXTRACT(MINUTE FROM DURACAO) * 60 +
                    EXTRACT(SECOND FROM DURACAO)) AS DURACAO_SEGUNDOS,
                   UF,
                   NOME_SEGMENTO,
                   NOTA_NPS
            FROM MEETING
            WHERE ID_MEETING = ?
            """;

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Meeting meeting = new Meeting();

                    meeting.setIdMeeting(rs.getInt("ID_MEETING"));
                    meeting.setDataMeeting(
                            rs.getTimestamp("DT_MEETING").toLocalDateTime());
                    meeting.setFormato(rs.getString("FORMATO"));
                    meeting.setStatus(rs.getString("STATUS"));
                    meeting.setDuracao(rs.getInt("DURACAO_SEGUNDOS"));
                    meeting.setUf(rs.getString("UF"));
                    meeting.setSegmento(rs.getString("NOME_SEGMENTO"));
                    meeting.setNotaNps(rs.getDouble("NOTA_NPS"));

                    return meeting;
                }
            }
        } catch (SQLException e) {
            System.out.println(
                    "Erro ao buscar Meeting: " + e.getMessage()
            );
        }
        return null;
    }
    public String atualizar(Meeting meeting) {
        if (meeting == null) {
            return "Meeting inválida.";
        }
        if (con == null) {
            return "Conexão inválida.";
        }
        if (meeting.getIdMeeting() <= 0) {
            return "ID da Meeting inválido.";
        }
        if (meeting.getDataMeeting() == null) {
            return "Data da Meeting inválida.";
        }
        if (meeting.getFormato() == null ||
                meeting.getFormato().trim().isEmpty()) {
            return "Formato da Meeting inválido.";
        }
        if (meeting.getStatus() == null ||
                meeting.getStatus().trim().isEmpty()) {
            return "Status da Meeting inválido.";
        }
        if (meeting.getDuracao() <= 0) {
            return "Duração da Meeting inválida.";
        }

        String sql = """
            UPDATE MEETING
            SET DT_MEETING = ?,
                FORMATO = ?,
                STATUS = ?,
                DURACAO = NUMTODSINTERVAL(?, 'SECOND'),
                UF = ?,
                NOME_SEGMENTO = ?,
                NOTA_NPS = ?
            WHERE ID_MEETING = ?
            """;

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setTimestamp(
                    1,
                    Timestamp.valueOf(meeting.getDataMeeting())
            );

            pstmt.setString(2, meeting.getFormato());
            pstmt.setString(3, meeting.getStatus());
            pstmt.setInt(4, meeting.getDuracao());
            pstmt.setString(5, meeting.getUf());
            pstmt.setString(6, meeting.getSegmento());
            pstmt.setDouble(7, meeting.getNotaNps());
            pstmt.setInt(8, meeting.getIdMeeting());

            int linhasAfetadas = pstmt.executeUpdate();

            if (linhasAfetadas > 0) {
                return "Meeting atualizada com sucesso.";
            }
            return "Meeting não encontrada.";
        } catch (SQLException e) {
            return "Erro ao atualizar Meeting: " + e.getMessage();
        }
    }
    public String remover(int id) {
        if (id <= 0) {
            return "ID da Meeting inválido.";
        }
        if (con == null) {
            return "Conexão inválida.";
        }

        String sql = """
            DELETE FROM MEETING
            WHERE ID_MEETING = ?
            """;

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            int linhasAfetadas = pstmt.executeUpdate();
            if (linhasAfetadas > 0) {
                return "Meeting removida com sucesso.";
            }
            return "Meeting não encontrada.";
        } catch (SQLException e) {
            return "Erro ao remover Meeting: " + e.getMessage();
        }
    }
    public List<Meeting> listarTodos() {
        List<Meeting> meetings = new ArrayList<>();

        if (con == null) {
            return meetings;
        }

        String sql = """
            SELECT ID_MEETING,
                   DT_MEETING,
                   FORMATO,
                   STATUS,
                   (EXTRACT(DAY FROM DURACAO) * 86400 +
                    EXTRACT(HOUR FROM DURACAO) * 3600 +
                    EXTRACT(MINUTE FROM DURACAO) * 60 +
                    EXTRACT(SECOND FROM DURACAO)) AS DURACAO_SEGUNDOS,
                   UF,
                   NOME_SEGMENTO,
                   NOTA_NPS
            FROM MEETING
            ORDER BY ID_MEETING
            """;

        try (PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Meeting meeting = new Meeting();

                meeting.setIdMeeting(rs.getInt("ID_MEETING"));
                meeting.setDataMeeting(
                        rs.getTimestamp("DT_MEETING").toLocalDateTime()
                );
                meeting.setFormato(rs.getString("FORMATO"));
                meeting.setStatus(rs.getString("STATUS"));
                meeting.setDuracao(rs.getInt("DURACAO_SEGUNDOS"));
                meeting.setUf(rs.getString("UF"));
                meeting.setSegmento(rs.getString("NOME_SEGMENTO"));
                meeting.setNotaNps(rs.getDouble("NOTA_NPS"));

                meetings.add(meeting);
            }
        } catch (SQLException e) {
            System.out.println(
                    "Erro ao listar Meetings: " + e.getMessage()
            );
        }

        return meetings;
    }
}