package br.com.fiap.model.repository;

import br.com.fiap.model.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingRepository
        extends JpaRepository<Meeting, Integer> {

    boolean existsByPalavrasChave_IdPalavraChave(
            int idPalavraChave
    );
}