package br.com.fiap.service;

import br.com.fiap.model.entity.PalavraChave;
import br.com.fiap.model.repository.PalavraChaveRepository;
import br.com.fiap.model.dto.MeetingDTO;
import br.com.fiap.model.entity.Meeting;
import br.com.fiap.model.repository.MeetingRepository;
import br.com.fiap.model.repository.TranscricaoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
public class MeetingService {

    @Autowired
    private MeetingRepository meetingRepository;
    @Autowired
    private TranscricaoRepository transcricaoRepository;
    @Autowired
    private PalavraChaveRepository palavraChaveRepository;

    public Meeting cadastrar(@Valid MeetingDTO meetingDTO) {
        Meeting meeting = new Meeting(meetingDTO);
        return meetingRepository.save(meeting);
    }

    public List<Meeting> listar() {
        return meetingRepository.findAll();
    }

    public Meeting buscarPorId(int id) {
        if (id <= 0) {
            return null;
        }

        return meetingRepository.findById(id).orElse(null);
    }

    public Meeting atualizar(int id, @Valid MeetingDTO meetingDTO) {
        Meeting meeting = buscarPorId(id);

        if (meeting == null) {
            return null;
        }

        meeting.setDataMeeting(meetingDTO.dataMeeting());
        meeting.setFormato(meetingDTO.formato());
        meeting.setStatus(meetingDTO.status());
        meeting.setDuracao(meetingDTO.duracao());
        meeting.setUf(meetingDTO.uf());
        meeting.setSegmento(meetingDTO.segmento());
        meeting.setNotaNps(meetingDTO.notaNps());

        return meetingRepository.save(meeting);
    }

    public boolean excluir(int id) {
        Meeting meeting = buscarPorId(id);

        if (meeting == null) {
            return false;
        }

        if (transcricaoRepository
                .findByMeeting_IdMeeting(id) != null) {
            return false;
        }
        meetingRepository.delete(meeting);
        return true;
    }

    public Meeting adicionarPalavraChave(
            int idMeeting,
            int idPalavraChave) {

        Meeting meeting =
                buscarPorId(idMeeting);

        if (meeting == null) {
            return null;
        }

        PalavraChave palavraChave =
                palavraChaveRepository
                        .findById(idPalavraChave)
                        .orElse(null);

        if (palavraChave == null) {
            return null;
        }

        meeting.adicionarPalavraChave(
                palavraChave
        );

        return meetingRepository.save(
                meeting
        );
    }

    public Meeting removerPalavraChave(
            int idMeeting,
            int idPalavraChave) {

        Meeting meeting =
                buscarPorId(idMeeting);

        if (meeting == null) {
            return null;
        }

        PalavraChave palavraChave =
                palavraChaveRepository
                        .findById(idPalavraChave)
                        .orElse(null);

        if (palavraChave == null) {
            return null;
        }

        boolean removida =
                meeting.getPalavrasChave()
                        .removeIf(
                                palavra ->
                                        palavra.getIdPalavraChave()
                                                == idPalavraChave
                        );

        if (!removida) {
            return null;
        }

        return meetingRepository.save(
                meeting
        );
    }
}