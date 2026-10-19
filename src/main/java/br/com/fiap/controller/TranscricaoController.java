package br.com.fiap.controller;

import br.com.fiap.model.entity.ResultadoAnalise;
import br.com.fiap.service.AnaliseAutomaticaService;
import br.com.fiap.model.dto.TranscricaoDTO;
import br.com.fiap.model.entity.Transcricao;
import br.com.fiap.service.TranscricaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transcricoes")
public class TranscricaoController {

    @Autowired
    private TranscricaoService transcricaoService;
    @Autowired
    private AnaliseAutomaticaService analiseAutomaticaService;

    @PostMapping
    public ResponseEntity<?> cadastrar(
            @RequestBody @Valid TranscricaoDTO transcricaoDTO) {

        try {
            Transcricao transcricao =
                    transcricaoService.cadastrar(transcricaoDTO);

            if (transcricao != null) {
                return ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(transcricao);
            }

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(
                            "Meeting não encontrada ou já possui uma transcrição."
                    );

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao cadastrar Transcrição.");
        }
    }

    @GetMapping
    public ResponseEntity<List<Transcricao>> listar() {

        List<Transcricao> transcricoes =
                transcricaoService.listar();

        return ResponseEntity.ok(transcricoes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(
            @PathVariable int id) {

        Transcricao transcricao =
                transcricaoService.buscarPorId(id);

        if (transcricao != null) {
            return ResponseEntity.ok(transcricao);
        }

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("Transcrição não encontrada.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(
            @PathVariable int id,
            @RequestBody @Valid TranscricaoDTO transcricaoDTO) {

        Transcricao transcricaoExistente =
                transcricaoService.buscarPorId(id);

        if (transcricaoExistente == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Transcrição não encontrada.");
        }

        try {
            Transcricao transcricao =
                    transcricaoService.atualizar(
                            id,
                            transcricaoDTO
                    );

            if (transcricao != null) {
                return ResponseEntity.ok(transcricao);
            }

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(
                            "Meeting não encontrada ou já possui outra transcrição."
                    );

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao atualizar Transcrição.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> excluir(
            @PathVariable int id) {

        Transcricao transcricao =
                transcricaoService.buscarPorId(id);

        if (transcricao == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Transcrição não encontrada.");
        }

        if (transcricaoService.excluir(id)) {
            return ResponseEntity.ok(
                    "Transcrição removida com sucesso."
            );
        }

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(
                        "Não é possível remover a Transcrição enquanto existir um resultado de análise vinculado."
                );
    }

    @PostMapping("/{id}/analisar")
    public ResponseEntity<?> analisar(
            @PathVariable int id) {

        Transcricao transcricao =
                transcricaoService.buscarPorId(id);

        if (transcricao == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(
                            "Transcrição não encontrada."
                    );
        }

        try {

            ResultadoAnalise resultadoAnalise =
                    analiseAutomaticaService.analisar(id);

            if (resultadoAnalise != null) {
                return ResponseEntity.ok(
                        resultadoAnalise
                );
            }

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(
                            "A Transcrição já possui um resultado de análise."
                    );

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(
                            "Erro ao analisar Transcrição."
                    );
        }
    }
}