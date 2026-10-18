package br.com.fiap.model.repository;

import br.com.fiap.model.entity.PalavraChave;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PalavraChaveRepository
        extends JpaRepository<PalavraChave, Integer> {

    PalavraChave findByPalavra(String palavra);
}