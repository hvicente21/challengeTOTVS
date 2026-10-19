package br.com.fiap.service;

import br.com.fiap.model.dto.AlertaDTO;
import br.com.fiap.model.entity.Alerta;
import br.com.fiap.model.entity.ResultadoAnalise;
import br.com.fiap.model.repository.AlertaRepository;
import br.com.fiap.model.repository.ResultadoAnaliseRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
public class AlertaService {

    @Autowired
    private AlertaRepository alertaRepository;

    @Autowired
    private ResultadoAnaliseRepository resultadoAnaliseRepository;

    public Alerta cadastrar(
            @Valid AlertaDTO alertaDTO) {

        ResultadoAnalise resultadoAnalise =
                resultadoAnaliseRepository
                        .findById(alertaDTO.idResultadoAnalise())
                        .orElse(null);

        if (resultadoAnalise == null) {
            return null;
        }

        Alerta alerta =
                new Alerta(
                        alertaDTO,
                        resultadoAnalise
                );

        return alertaRepository.save(alerta);
    }

    public List<Alerta> listar() {
        return alertaRepository.findAll();
    }

    public Alerta buscarPorId(int id) {

        if (id <= 0) {
            return null;
        }

        return alertaRepository
                .findById(id)
                .orElse(null);
    }

    public Alerta atualizar(
            int id,
            @Valid AlertaDTO alertaDTO) {

        Alerta alerta =
                buscarPorId(id);

        if (alerta == null) {
            return null;
        }

        ResultadoAnalise resultadoAnalise =
                resultadoAnaliseRepository
                        .findById(alertaDTO.idResultadoAnalise())
                        .orElse(null);

        if (resultadoAnalise == null) {
            return null;
        }

        alerta.setResultadoAnalise(resultadoAnalise);
        alerta.setTipoAlerta(
                alertaDTO.tipoAlerta()
        );
        alerta.setNivelRisco(
                alertaDTO.nivelRisco()
        );
        alerta.setDescricao(
                alertaDTO.descricao()
        );

        return alertaRepository.save(alerta);
    }

    public boolean excluir(int id) {

        Alerta alerta =
                buscarPorId(id);

        if (alerta == null) {
            return false;
        }

        alertaRepository.delete(alerta);

        return true;
    }
}