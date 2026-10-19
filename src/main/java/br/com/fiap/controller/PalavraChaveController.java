package br.com.fiap.controller;

import br.com.fiap.model.dto.PalavraChaveDTO;
import br.com.fiap.model.entity.PalavraChave;
import br.com.fiap.service.PalavraChaveService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/palavras-chave")
public class PalavraChaveController {

    @Autowired
    private PalavraChaveService palavraChaveService;

    @PostMapping
    public ResponseEntity<?> cadastrar(
            @RequestBody @Valid PalavraChaveDTO palavraChaveDTO) {

        PalavraChave palavraChave =
                palavraChaveService.cadastrar(
                        palavraChaveDTO
                );

        if (palavraChave != null) {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(palavraChave);
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        "Palavra-chave já cadastrada."
                );
    }

    @GetMapping
    public ResponseEntity<List<PalavraChave>> listar() {

        List<PalavraChave> palavrasChave =
                palavraChaveService.listar();

        return ResponseEntity.ok(
                palavrasChave
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(
            @PathVariable int id) {

        PalavraChave palavraChave =
                palavraChaveService.buscarPorId(id);

        if (palavraChave != null) {
            return ResponseEntity.ok(
                    palavraChave
            );
        }

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        "Palavra-chave não encontrada."
                );
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(
            @PathVariable int id,
            @RequestBody @Valid PalavraChaveDTO palavraChaveDTO) {

        PalavraChave palavraExistente =
                palavraChaveService.buscarPorId(id);

        if (palavraExistente == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(
                            "Palavra-chave não encontrada."
                    );
        }

        PalavraChave palavraChave =
                palavraChaveService.atualizar(
                        id,
                        palavraChaveDTO
                );

        if (palavraChave != null) {
            return ResponseEntity.ok(
                    palavraChave
            );
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        "Já existe outra palavra-chave com esse nome."
                );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> excluir(
            @PathVariable int id) {

        PalavraChave palavraChave =
                palavraChaveService.buscarPorId(id);

        if (palavraChave == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(
                            "Palavra-chave não encontrada."
                    );
        }

        if (palavraChaveService.excluir(id)) {
            return ResponseEntity.ok(
                    "Palavra-chave removida com sucesso."
            );
        }

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(
                        "Não é possível remover a palavra-chave enquanto estiver vinculada a uma Meeting."
                );
    }
}