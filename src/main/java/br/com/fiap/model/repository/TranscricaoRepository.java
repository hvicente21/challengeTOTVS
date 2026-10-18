package br.com.fiap.model.repository;

import br.com.fiap.model.entity.Transcricao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TranscricaoRepository
        extends JpaRepository<Transcricao, Integer> {

    Transcricao findByMeeting_IdMeeting(int idMeeting);
}