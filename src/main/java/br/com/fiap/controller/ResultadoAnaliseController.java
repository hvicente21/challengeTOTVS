package br.com.fiap.controller;

import br.com.fiap.model.dto.ResultadoAnaliseDTO;
import br.com.fiap.model.entity.ResultadoAnalise;
import br.com.fiap.service.ResultadoAnaliseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/resultados")
public class ResultadoAnaliseController {

    @Autowired
    private ResultadoAnaliseService resultadoAnaliseService;

    @PostMapping
    public ResponseEntity<?> cadastrar(
            @RequestBody @Valid ResultadoAnaliseDTO resultadoAnaliseDTO) {

        try {
            ResultadoAnalise resultadoAnalise =
                    resultadoAnaliseService.cadastrar(
                            resultadoAnaliseDTO
                    );

            if (resultadoAnalise != null) {
                return ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(resultadoAnalise);
            }

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(
                            "Transcrição não encontrada ou já possui um resultado de análise."
                    );

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(
                            "Erro ao cadastrar Resultado da Análise."
                    );
        }
    }

    @GetMapping
    public ResponseEntity<List<ResultadoAnalise>> listar() {

        List<ResultadoAnalise> resultados =
                resultadoAnaliseService.listar();

        return ResponseEntity.ok(resultados);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(
            @PathVariable int id) {

        ResultadoAnalise resultadoAnalise =
                resultadoAnaliseService.buscarPorId(id);

        if (resultadoAnalise != null) {
            return ResponseEntity.ok(resultadoAnalise);
        }

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        "Resultado da análise não encontrado."
                );
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(
            @PathVariable int id,
            @RequestBody @Valid ResultadoAnaliseDTO resultadoAnaliseDTO) {

        ResultadoAnalise resultadoExistente =
                resultadoAnaliseService.buscarPorId(id);

        if (resultadoExistente == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(
                            "Resultado da análise não encontrado."
                    );
        }

        try {
            ResultadoAnalise resultadoAnalise =
                    resultadoAnaliseService.atualizar(
                            id,
                            resultadoAnaliseDTO
                    );

            if (resultadoAnalise != null) {
                return ResponseEntity.ok(
                        resultadoAnalise
                );
            }

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(
                            "Transcrição não encontrada ou já possui outro resultado de análise."
                    );

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(
                            "Erro ao atualizar Resultado da Análise."
                    );
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> excluir(
            @PathVariable int id) {

        ResultadoAnalise resultadoAnalise =
                resultadoAnaliseService.buscarPorId(id);

        if (resultadoAnalise == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(
                            "Resultado da análise não encontrado."
                    );
        }

        if (resultadoAnaliseService.excluir(id)) {
            return ResponseEntity.ok(
                    "Resultado da análise removido com sucesso."
            );
        }

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(
                        "Não é possível remover o Resultado da Análise enquanto existirem alertas vinculados."
                );
    }
}