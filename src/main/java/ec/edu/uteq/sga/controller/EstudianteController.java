package ec.edu.uteq.sga.controller;

import ec.edu.uteq.sga.dto.estudiante.*;
import ec.edu.uteq.sga.service.EstudianteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estudiantes")
@RequiredArgsConstructor
public class EstudianteController {

    private final EstudianteService estudianteService;

    @GetMapping
    public ResponseEntity<List<EstudianteResponseDTO>> listar() {
        return ResponseEntity.ok(estudianteService.listarTodos());
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<EstudianteResponseDTO>> buscar(@RequestParam String q) {
        return ResponseEntity.ok(estudianteService.buscar(q));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstudianteResponseDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(estudianteService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<EstudianteResponseDTO> crear(@Valid @RequestBody EstudianteRequestDTO dto,
                                                       Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(estudianteService.crear(dto, auth.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstudianteResponseDTO> actualizar(@PathVariable Long id,
                                                            @Valid @RequestBody EstudianteRequestDTO dto) {
        return ResponseEntity.ok(estudianteService.actualizar(id, dto));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Void> cambiarEstado(@PathVariable Long id,
                                              @RequestParam String estado) {
        estudianteService.cambiarEstado(id, estado);
        return ResponseEntity.noContent().build();
    }
}