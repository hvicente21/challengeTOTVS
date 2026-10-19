package br.com.fiap.controller;

import br.com.fiap.model.dto.AlertaDTO;
import br.com.fiap.model.entity.Alerta;
import br.com.fiap.service.AlertaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alertas")
public class AlertaController {

    @Autowired
    private AlertaService alertaService;

    @PostMapping
    public ResponseEntity<?> cadastrar(
            @RequestBody @Valid AlertaDTO alertaDTO) {

        try {
            Alerta alerta =
                    alertaService.cadastrar(alertaDTO);

            if (alerta != null) {
                return ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(alerta);
            }

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(
                            "Resultado da análise não encontrado."
                    );

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(
                            "Erro ao cadastrar Alerta."
                    );
        }
    }

    @GetMapping
    public ResponseEntity<List<Alerta>> listar() {

        List<Alerta> alertas =
                alertaService.listar();

        return ResponseEntity.ok(alertas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(
            @PathVariable int id) {

        Alerta alerta =
                alertaService.buscarPorId(id);

        if (alerta != null) {
            return ResponseEntity.ok(alerta);
        }

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        "Alerta não encontrado."
                );
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(
            @PathVariable int id,
            @RequestBody @Valid AlertaDTO alertaDTO) {

        Alerta alertaExistente =
                alertaService.buscarPorId(id);

        if (alertaExistente == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(
                            "Alerta não encontrado."
                    );
        }

        try {
            Alerta alerta =
                    alertaService.atualizar(
                            id,
                            alertaDTO
                    );

            if (alerta != null) {
                return ResponseEntity.ok(alerta);
            }

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(
                            "Resultado da análise não encontrado."
                    );

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(
                            "Erro ao atualizar Alerta."
                    );
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> excluir(
            @PathVariable int id) {

        if (alertaService.excluir(id)) {
            return ResponseEntity.ok(
                    "Alerta removido com sucesso."
            );
        }

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        "Alerta não encontrado."
                );
    }
}