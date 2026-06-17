package ec.edu.uteq.sga.controller;

import ec.edu.uteq.sga.dto.grado.*;
import ec.edu.uteq.sga.service.GradoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grados")
@RequiredArgsConstructor
public class GradoController {

    private final GradoService gradoService;

    @GetMapping
    public ResponseEntity<List<GradoResponseDTO>> listar() {
        return ResponseEntity.ok(gradoService.listarTodos());
    }

    @GetMapping("/activos")
    public ResponseEntity<List<GradoResponseDTO>> listarActivos() {
        return ResponseEntity.ok(gradoService.listarActivos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GradoResponseDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(gradoService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<GradoResponseDTO> crear(@Valid @RequestBody GradoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(gradoService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GradoResponseDTO> actualizar(@PathVariable Long id,
                                                       @Valid @RequestBody GradoRequestDTO dto) {
        return ResponseEntity.ok(gradoService.actualizar(id, dto));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Void> cambiarEstado(@PathVariable Long id,
                                              @RequestParam boolean activo) {
        gradoService.cambiarEstado(id, activo);
        return ResponseEntity.noContent().build();
    }
}