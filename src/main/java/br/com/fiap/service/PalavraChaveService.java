package br.com.fiap.service;

import br.com.fiap.model.dto.PalavraChaveDTO;
import br.com.fiap.model.entity.PalavraChave;
import br.com.fiap.model.repository.MeetingRepository;
import br.com.fiap.model.repository.PalavraChaveRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
public class PalavraChaveService {

    @Autowired
    private PalavraChaveRepository palavraChaveRepository;

    @Autowired
    private MeetingRepository meetingRepository;

    public PalavraChave cadastrar(
            @Valid PalavraChaveDTO palavraChaveDTO) {

        String palavra =
                palavraChaveDTO.palavra().trim();

        PalavraChave palavraExistente =
                palavraChaveRepository
                        .findByPalavraIgnoreCase(palavra);

        if (palavraExistente != null) {
            return null;
        }

        PalavraChave palavraChave =
                new PalavraChave(palavraChaveDTO);

        return palavraChaveRepository.save(
                palavraChave
        );
    }

    public List<PalavraChave> listar() {
        return palavraChaveRepository.findAll();
    }

    public PalavraChave buscarPorId(int id) {

        if (id <= 0) {
            return null;
        }

        return palavraChaveRepository
                .findById(id)
                .orElse(null);
    }

    public PalavraChave atualizar(
            int id,
            @Valid PalavraChaveDTO palavraChaveDTO) {

        PalavraChave palavraChave =
                buscarPorId(id);

        if (palavraChave == null) {
            return null;
        }

        String palavra =
                palavraChaveDTO.palavra().trim();

        PalavraChave palavraExistente =
                palavraChaveRepository
                        .findByPalavraIgnoreCase(palavra);

        if (palavraExistente != null &&
                palavraExistente.getIdPalavraChave() != id) {

            return null;
        }

        palavraChave.setPalavra(palavra);

        return palavraChaveRepository.save(
                palavraChave
        );
    }

    public boolean excluir(int id) {

        PalavraChave palavraChave =
                buscarPorId(id);

        if (palavraChave == null) {
            return false;
        }

        if (meetingRepository
                .existsByPalavrasChave_IdPalavraChave(id)) {

            return false;
        }

        palavraChaveRepository.delete(
                palavraChave
        );

        return true;
    }
}