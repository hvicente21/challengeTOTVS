package br.com.fiap.service;

import br.com.fiap.model.dto.TranscricaoDTO;
import br.com.fiap.model.entity.Meeting;
import br.com.fiap.model.entity.Transcricao;
import br.com.fiap.model.repository.MeetingRepository;
import br.com.fiap.model.repository.TranscricaoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
public class TranscricaoService {

    @Autowired
    private TranscricaoRepository transcricaoRepository;

    @Autowired
    private MeetingRepository meetingRepository;

    public Transcricao cadastrar(@Valid TranscricaoDTO transcricaoDTO) {

        Meeting meeting =
                meetingRepository
                        .findById(transcricaoDTO.idMeeting())
                        .orElse(null);

        if (meeting == null) {
            return null;
        }

        if (meeting.getTranscricao() != null) {
            return null;
        }

        Transcricao transcricao =
                new Transcricao(transcricaoDTO, meeting);

        return transcricaoRepository.save(transcricao);
    }

    public List<Transcricao> listar() {
        return transcricaoRepository.findAll();
    }

    public Transcricao buscarPorId(int id) {

        if (id <= 0) {
            return null;
        }

        return transcricaoRepository
                .findById(id)
                .orElse(null);
    }

    public Transcricao atualizar(
            int id,
            @Valid TranscricaoDTO transcricaoDTO) {

        Transcricao transcricao =
                buscarPorId(id);

        if (transcricao == null) {
            return null;
        }

        Meeting meeting =
                meetingRepository
                        .findById(transcricaoDTO.idMeeting())
                        .orElse(null);

        if (meeting == null) {
            return null;
        }

        if (meeting.getTranscricao() != null &&
                meeting.getTranscricao().getIdTranscricao() != id) {

            return null;
        }

        transcricao.setMeeting(meeting);
        transcricao.setTexto(transcricaoDTO.texto());

        return transcricaoRepository.save(transcricao);
    }

    public boolean excluir(int id) {

        Transcricao transcricao =
                buscarPorId(id);

        if (transcricao == null) {
            return false;
        }

        if (transcricao.getResultadoAnalise() != null) {
            return false;
        }

        transcricaoRepository.delete(transcricao);
        return true;
    }
}