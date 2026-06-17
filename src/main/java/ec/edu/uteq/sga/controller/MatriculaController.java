package ec.edu.uteq.sga.controller;

import ec.edu.uteq.sga.dto.matricula.*;
import ec.edu.uteq.sga.service.MatriculaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matriculas")
@RequiredArgsConstructor
public class MatriculaController {

    private final MatriculaService matriculaService;

    @GetMapping
    public ResponseEntity<List<MatriculaResponseDTO>> listar() {
        return ResponseEntity.ok(matriculaService.listarTodos());
    }

    @GetMapping("/ano-lectivo/{idAnoLectivo}")
    public ResponseEntity<List<MatriculaResponseDTO>> listarPorAnoLectivo(@PathVariable Long idAnoLectivo) {
        return ResponseEntity.ok(matriculaService.listarPorAnoLectivo(idAnoLectivo));
    }

    @GetMapping("/estudiante/{idEstudiante}")
    public ResponseEntity<List<MatriculaResponseDTO>> listarPorEstudiante(@PathVariable Long idEstudiante) {
        return ResponseEntity.ok(matriculaService.listarPorEstudiante(idEstudiante));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatriculaResponseDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(matriculaService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<MatriculaResponseDTO> crear(@Valid @RequestBody MatriculaRequestDTO dto,
                                                      Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(matriculaService.crear(dto, auth.getName()));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Void> cambiarEstado(@PathVariable Long id,
                                              @RequestParam String estado) {
        matriculaService.cambiarEstado(id, estado);
        return ResponseEntity.noContent().build();
    }
}