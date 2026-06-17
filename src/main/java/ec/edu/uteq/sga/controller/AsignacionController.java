package ec.edu.uteq.sga.controller;

import ec.edu.uteq.sga.dto.asignacion.*;
import ec.edu.uteq.sga.service.AsignacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/asignaciones")
@RequiredArgsConstructor
public class AsignacionController {

    private final AsignacionService asignacionService;

    @GetMapping
    public ResponseEntity<List<AsignacionResponseDTO>> listar() {
        return ResponseEntity.ok(asignacionService.listarTodos());
    }

    @GetMapping("/ano-lectivo/{idAnoLectivo}")
    public ResponseEntity<List<AsignacionResponseDTO>> listarPorAnoLectivo(@PathVariable Long idAnoLectivo) {
        return ResponseEntity.ok(asignacionService.listarPorAnoLectivo(idAnoLectivo));
    }

    @GetMapping("/docente/{idDocente}")
    public ResponseEntity<List<AsignacionResponseDTO>> listarPorDocente(@PathVariable Long idDocente) {
        return ResponseEntity.ok(asignacionService.listarPorDocente(idDocente));
    }

    @PostMapping
    public ResponseEntity<AsignacionResponseDTO> crear(@Valid @RequestBody AsignacionRequestDTO dto,
                                                       Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(asignacionService.crear(dto, auth.getName()));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Void> cambiarEstado(@PathVariable Long id,
                                              @RequestParam boolean activo) {
        asignacionService.cambiarEstado(id, activo);
        return ResponseEntity.noContent().build();
    }
}