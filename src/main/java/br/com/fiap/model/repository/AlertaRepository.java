package br.com.fiap.model.repository;

import br.com.fiap.model.entity.Alerta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertaRepository
        extends JpaRepository<Alerta, Integer> {
}