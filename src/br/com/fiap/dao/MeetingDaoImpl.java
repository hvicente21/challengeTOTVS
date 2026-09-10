package br.com.fiap.dao;

import br.com.fiap.dto.Meeting;
import br.com.fiap.dto.PalavraChave;

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
                     con.prepareStatement(
                             sql,
                             new String[]{"ID_MEETING"}
                     )) {

            pstmt.setTimestamp(
                    1,
                    Timestamp.valueOf(
                            meeting.getDataMeeting()
                    )
            );

            pstmt.setString(
                    2,
                    meeting.getFormato()
            );

            pstmt.setString(
                    3,
                    meeting.getStatus()
            );

            pstmt.setInt(
                    4,
                    meeting.getDuracao()
            );

            pstmt.setString(
                    5,
                    meeting.getUf()
            );

            pstmt.setString(
                    6,
                    meeting.getSegmento()
            );

            pstmt.setDouble(
                    7,
                    meeting.getNotaNps()
            );

            int linhasAfetadas =
                    pstmt.executeUpdate();

            if (linhasAfetadas > 0) {

                try (ResultSet rs =
                             pstmt.getGeneratedKeys()) {

                    if (rs.next()) {

                        meeting.setIdMeeting(
                                rs.getInt(1)
                        );


                        /*
                         * Depois que a Meeting possui ID,
                         * podemos salvar suas palavras-chave
                         * e criar os vínculos.
                         */

                        try {

                            salvarPalavrasChave(
                                    meeting
                            );

                        } catch (SQLException e) {

                            return "Meeting inserida, mas houve erro ao salvar as palavras-chave: "
                                    + e.getMessage();
                        }


                        return "Meeting inserida com sucesso.";
                    }
                }

                return "Meeting inserida, mas o ID gerado não foi recuperado.";
            }

            return "Nenhuma Meeting foi inserida.";

        } catch (SQLException e) {

            return "Erro ao inserir Meeting: "
                    + e.getMessage();
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

        try (PreparedStatement pstmt =
                     con.prepareStatement(sql)) {

            pstmt.setInt(
                    1,
                    id
            );

            try (ResultSet rs =
                         pstmt.executeQuery()) {

                if (rs.next()) {

                    Meeting meeting =
                            new Meeting();

                    meeting.setIdMeeting(
                            rs.getInt(
                                    "ID_MEETING"
                            )
                    );

                    meeting.setDataMeeting(
                            rs.getTimestamp(
                                    "DT_MEETING"
                            ).toLocalDateTime()
                    );

                    meeting.setFormato(
                            rs.getString(
                                    "FORMATO"
                            )
                    );

                    meeting.setStatus(
                            rs.getString(
                                    "STATUS"
                            )
                    );

                    meeting.setDuracao(
                            rs.getInt(
                                    "DURACAO_SEGUNDOS"
                            )
                    );

                    meeting.setUf(
                            rs.getString(
                                    "UF"
                            )
                    );

                    meeting.setSegmento(
                            rs.getString(
                                    "NOME_SEGMENTO"
                            )
                    );

                    meeting.setNotaNps(
                            rs.getDouble(
                                    "NOTA_NPS"
                            )
                    );

                    return meeting;
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Erro ao buscar Meeting: "
                            + e.getMessage()
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

        try (PreparedStatement pstmt =
                     con.prepareStatement(sql)) {

            pstmt.setTimestamp(
                    1,
                    Timestamp.valueOf(
                            meeting.getDataMeeting()
                    )
            );

            pstmt.setString(
                    2,
                    meeting.getFormato()
            );

            pstmt.setString(
                    3,
                    meeting.getStatus()
            );

            pstmt.setInt(
                    4,
                    meeting.getDuracao()
            );

            pstmt.setString(
                    5,
                    meeting.getUf()
            );

            pstmt.setString(
                    6,
                    meeting.getSegmento()
            );

            pstmt.setDouble(
                    7,
                    meeting.getNotaNps()
            );

            pstmt.setInt(
                    8,
                    meeting.getIdMeeting()
            );

            int linhasAfetadas =
                    pstmt.executeUpdate();

            if (linhasAfetadas > 0) {
                return "Meeting atualizada com sucesso.";
            }

            return "Meeting não encontrada.";

        } catch (SQLException e) {

            return "Erro ao atualizar Meeting: "
                    + e.getMessage();
        }
    }


    public String remover(int id) {

        if (id <= 0) {
            return "ID da Meeting inválido.";
        }

        if (con == null) {
            return "Conexão inválida.";
        }

        try {

            /*
             * Se existir uma transcrição vinculada,
             * ela deve ser removida antes da Meeting.
             */

            String sqlVerificarTranscricao = """
            SELECT COUNT(*)
            FROM TRANSCRICAO
            WHERE ID_MEETING = ?
            """;

            try (PreparedStatement pstmt =
                         con.prepareStatement(sqlVerificarTranscricao)) {

                pstmt.setInt(1, id);

                try (ResultSet rs = pstmt.executeQuery()) {

                    if (rs.next() && rs.getInt(1) > 0) {

                        return "Não é possível remover a Meeting enquanto existir uma transcrição vinculada.";
                    }
                }
            }


            /*
             * Remove os vínculos internos com
             * as palavras-chave.
             */

            String sqlVinculos = """
            DELETE FROM MEETING_PALAVRA_CHAVE
            WHERE ID_MEETING = ?
            """;

            try (PreparedStatement pstmt =
                         con.prepareStatement(sqlVinculos)) {

                pstmt.setInt(1, id);
                pstmt.executeUpdate();
            }


            /*
             * Agora a Meeting pode ser removida.
             */

            String sqlMeeting = """
            DELETE FROM MEETING
            WHERE ID_MEETING = ?
            """;

            try (PreparedStatement pstmt =
                         con.prepareStatement(sqlMeeting)) {

                pstmt.setInt(1, id);

                int linhasAfetadas =
                        pstmt.executeUpdate();

                if (linhasAfetadas > 0) {

                    return "Meeting removida com sucesso.";
                }

                return "Meeting não encontrada.";
            }

        } catch (SQLException e) {

            return "Erro ao remover Meeting: "
                    + e.getMessage();
        }
    }

    public List<Meeting> listarTodos() {

        List<Meeting> meetings =
                new ArrayList<>();

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

        try (PreparedStatement pstmt =
                     con.prepareStatement(sql);

             ResultSet rs =
                     pstmt.executeQuery()) {

            while (rs.next()) {

                Meeting meeting =
                        new Meeting();

                meeting.setIdMeeting(
                        rs.getInt(
                                "ID_MEETING"
                        )
                );

                meeting.setDataMeeting(
                        rs.getTimestamp(
                                "DT_MEETING"
                        ).toLocalDateTime()
                );

                meeting.setFormato(
                        rs.getString(
                                "FORMATO"
                        )
                );

                meeting.setStatus(
                        rs.getString(
                                "STATUS"
                        )
                );

                meeting.setDuracao(
                        rs.getInt(
                                "DURACAO_SEGUNDOS"
                        )
                );

                meeting.setUf(
                        rs.getString(
                                "UF"
                        )
                );

                meeting.setSegmento(
                        rs.getString(
                                "NOME_SEGMENTO"
                        )
                );

                meeting.setNotaNps(
                        rs.getDouble(
                                "NOTA_NPS"
                        )
                );

                meetings.add(
                        meeting
                );
            }

        } catch (SQLException e) {

            System.out.println(
                    "Erro ao listar Meetings: "
                            + e.getMessage()
            );
        }

        return meetings;
    }


    /*
     * =========================================================
     * PALAVRAS-CHAVE DA MEETING
     * =========================================================
     */


    private void salvarPalavrasChave(
            Meeting meeting
    ) throws SQLException {

        if (meeting.getPalavrasChave() == null ||
                meeting.getPalavrasChave().isEmpty()) {

            return;
        }

        for (PalavraChave palavraChave :
                meeting.getPalavrasChave()) {

            if (palavraChave == null ||
                    palavraChave.getPalavra() == null ||
                    palavraChave.getPalavra()
                            .trim()
                            .isEmpty()) {

                continue;
            }

            int idPalavraChave =
                    buscarIdPalavraChave(
                            palavraChave.getPalavra()
                    );


            /*
             * Se a palavra ainda não existir no banco,
             * ela será inserida.
             */

            if (idPalavraChave <= 0) {

                idPalavraChave =
                        inserirPalavraChave(
                                palavraChave.getPalavra()
                        );
            }


            /*
             * Mantemos também o ID atualizado
             * no objeto Java.
             */

            palavraChave.setIdPalavraChave(
                    idPalavraChave
            );


            /*
             * Depois relacionamos a palavra
             * com a Meeting.
             */

            vincularPalavraChave(
                    meeting.getIdMeeting(),
                    idPalavraChave
            );
        }
    }


    private int buscarIdPalavraChave(
            String palavra
    ) throws SQLException {

        String sql = """
            SELECT ID_PALAVRA_CHAVE
            FROM PALAVRA_CHAVE
            WHERE LOWER(TRIM(PALAVRA)) =
                  LOWER(TRIM(?))
            """;

        try (PreparedStatement pstmt =
                     con.prepareStatement(sql)) {

            pstmt.setString(
                    1,
                    palavra
            );

            try (ResultSet rs =
                         pstmt.executeQuery()) {

                if (rs.next()) {

                    return rs.getInt(
                            "ID_PALAVRA_CHAVE"
                    );
                }
            }
        }

        return 0;
    }


    private int inserirPalavraChave(
            String palavra
    ) throws SQLException {

        String sql = """
            INSERT INTO PALAVRA_CHAVE
            (PALAVRA)
            VALUES (?)
            """;

        try (PreparedStatement pstmt =
                     con.prepareStatement(
                             sql,
                             new String[]{
                                     "ID_PALAVRA_CHAVE"
                             }
                     )) {

            pstmt.setString(
                    1,
                    palavra.trim()
            );

            int linhasAfetadas =
                    pstmt.executeUpdate();

            if (linhasAfetadas > 0) {

                try (ResultSet rs =
                             pstmt.getGeneratedKeys()) {

                    if (rs.next()) {

                        return rs.getInt(1);
                    }
                }
            }
        }

        return 0;
    }


    private void vincularPalavraChave(
            int idMeeting,
            int idPalavraChave
    ) throws SQLException {

        if (idMeeting <= 0 ||
                idPalavraChave <= 0) {

            return;
        }


        /*
         * Primeiro verifica se o vínculo
         * já existe.
         */

        String sqlVerificar = """
            SELECT COUNT(*)
            FROM MEETING_PALAVRA_CHAVE
            WHERE ID_MEETING = ?
              AND ID_PALAVRA_CHAVE = ?
            """;

        try (PreparedStatement pstmt =
                     con.prepareStatement(
                             sqlVerificar
                     )) {

            pstmt.setInt(
                    1,
                    idMeeting
            );

            pstmt.setInt(
                    2,
                    idPalavraChave
            );

            try (ResultSet rs =
                         pstmt.executeQuery()) {

                if (rs.next() &&
                        rs.getInt(1) > 0) {

                    return;
                }
            }
        }


        /*
         * Se ainda não existir,
         * cria o vínculo.
         */

        String sqlInserir = """
            INSERT INTO MEETING_PALAVRA_CHAVE
            (ID_MEETING, ID_PALAVRA_CHAVE)
            VALUES (?, ?)
            """;

        try (PreparedStatement pstmt =
                     con.prepareStatement(
                             sqlInserir
                     )) {

            pstmt.setInt(
                    1,
                    idMeeting
            );

            pstmt.setInt(
                    2,
                    idPalavraChave
            );

            pstmt.executeUpdate();
        }
    }
}