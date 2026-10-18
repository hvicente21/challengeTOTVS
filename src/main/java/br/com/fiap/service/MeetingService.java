package br.com.fiap.service;

import br.com.fiap.model.dto.MeetingDTO;
import br.com.fiap.model.entity.Meeting;
import br.com.fiap.model.repository.MeetingRepository;
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
        if (id <= 0 || !meetingRepository.existsById(id)) {
            return false;
        }

        meetingRepository.deleteById(id);
        return true;
    }
}