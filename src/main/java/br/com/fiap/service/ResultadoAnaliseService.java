package br.com.fiap.service;

import br.com.fiap.model.dto.ResultadoAnaliseDTO;
import br.com.fiap.model.entity.ResultadoAnalise;
import br.com.fiap.model.entity.Transcricao;
import br.com.fiap.model.repository.AlertaRepository;
import br.com.fiap.model.repository.ResultadoAnaliseRepository;
import br.com.fiap.model.repository.TranscricaoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
public class ResultadoAnaliseService {

    @Autowired
    private ResultadoAnaliseRepository resultadoAnaliseRepository;

    @Autowired
    private TranscricaoRepository transcricaoRepository;

    @Autowired
    private AlertaRepository alertaRepository;

    public ResultadoAnalise cadastrar(
            @Valid ResultadoAnaliseDTO resultadoAnaliseDTO) {

        Transcricao transcricao =
                transcricaoRepository
                        .findById(resultadoAnaliseDTO.idTranscricao())
                        .orElse(null);

        if (transcricao == null) {
            return null;
        }

        ResultadoAnalise resultadoExistente =
                resultadoAnaliseRepository
                        .findByTranscricao_IdTranscricao(
                                resultadoAnaliseDTO.idTranscricao()
                        );

        if (resultadoExistente != null) {
            return null;
        }

        ResultadoAnalise resultadoAnalise =
                new ResultadoAnalise(
                        resultadoAnaliseDTO,
                        transcricao
                );

        return resultadoAnaliseRepository.save(resultadoAnalise);
    }

    public List<ResultadoAnalise> listar() {
        return resultadoAnaliseRepository.findAll();
    }

    public ResultadoAnalise buscarPorId(int id) {

        if (id <= 0) {
            return null;
        }

        return resultadoAnaliseRepository
                .findById(id)
                .orElse(null);
    }

    public ResultadoAnalise atualizar(
            int id,
            @Valid ResultadoAnaliseDTO resultadoAnaliseDTO) {

        ResultadoAnalise resultadoAnalise =
                buscarPorId(id);

        if (resultadoAnalise == null) {
            return null;
        }

        Transcricao transcricao =
                transcricaoRepository
                        .findById(resultadoAnaliseDTO.idTranscricao())
                        .orElse(null);

        if (transcricao == null) {
            return null;
        }

        ResultadoAnalise resultadoExistente =
                resultadoAnaliseRepository
                        .findByTranscricao_IdTranscricao(
                                resultadoAnaliseDTO.idTranscricao()
                        );

        if (resultadoExistente != null &&
                resultadoExistente.getIdResultadoAnalise() != id) {

            return null;
        }

        resultadoAnalise.setTranscricao(transcricao);
        resultadoAnalise.setResumo(
                resultadoAnaliseDTO.resumo()
        );
        resultadoAnalise.setSentimento(
                resultadoAnaliseDTO.sentimento()
        );
        resultadoAnalise.setRiscoChurn(
                resultadoAnaliseDTO.riscoChurn()
        );
        resultadoAnalise.setClassificacao(
                resultadoAnaliseDTO.classificacao()
        );

        return resultadoAnaliseRepository.save(
                resultadoAnalise
        );
    }

    public boolean excluir(int id) {

        ResultadoAnalise resultadoAnalise =
                buscarPorId(id);

        if (resultadoAnalise == null) {
            return false;
        }

        if (alertaRepository
                .existsByResultadoAnalise_IdResultadoAnalise(id)) {

            return false;
        }

        resultadoAnaliseRepository.delete(
                resultadoAnalise
        );

        return true;
    }
}