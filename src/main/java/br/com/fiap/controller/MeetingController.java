package br.com.fiap.controller;

import br.com.fiap.model.dto.MeetingDTO;
import br.com.fiap.model.entity.Meeting;
import br.com.fiap.service.MeetingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/meetings")
public class MeetingController {

    @Autowired
    private MeetingService meetingService;

    @PostMapping
    public ResponseEntity<?> cadastrar(
            @RequestBody @Valid MeetingDTO meetingDTO) {

        try {
            Meeting meeting =
                    meetingService.cadastrar(meetingDTO);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(meeting);

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao cadastrar Meeting.");
        }
    }

    @GetMapping
    public ResponseEntity<List<Meeting>> listar() {

        List<Meeting> meetings =
                meetingService.listar();

        return ResponseEntity.ok(meetings);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(
            @PathVariable int id) {

        Meeting meeting =
                meetingService.buscarPorId(id);

        if (meeting != null) {
            return ResponseEntity.ok(meeting);
        }

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("Meeting não encontrada.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(
            @PathVariable int id,
            @RequestBody @Valid MeetingDTO meetingDTO) {

        try {
            Meeting meeting =
                    meetingService.atualizar(
                            id,
                            meetingDTO
                    );

            if (meeting != null) {
                return ResponseEntity.ok(meeting);
            }

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Meeting não encontrada.");

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao atualizar Meeting.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> excluir(
            @PathVariable int id) {

        Meeting meeting =
                meetingService.buscarPorId(id);

        if (meeting == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Meeting não encontrada.");
        }

        if (meetingService.excluir(id)) {
            return ResponseEntity.ok(
                    "Meeting removida com sucesso."
            );
        }

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(
                        "Não é possível remover a Meeting enquanto existir uma transcrição vinculada."
                );
    }
}