package br.com.fiap.model.repository;

import br.com.fiap.model.entity.ResultadoAnalise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResultadoAnaliseRepository
        extends JpaRepository<ResultadoAnalise, Integer> {
}