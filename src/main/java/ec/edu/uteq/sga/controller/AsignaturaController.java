package ec.edu.uteq.sga.controller;

import ec.edu.uteq.sga.dto.asignatura.*;
import ec.edu.uteq.sga.service.AsignaturaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/asignaturas")
@RequiredArgsConstructor
public class AsignaturaController {

    private final AsignaturaService asignaturaService;

    @GetMapping
    public ResponseEntity<List<AsignaturaResponseDTO>> listar() {
        return ResponseEntity.ok(asignaturaService.listarTodos());
    }

    @GetMapping("/activas")
    public ResponseEntity<List<AsignaturaResponseDTO>> listarActivas() {
        return ResponseEntity.ok(asignaturaService.listarActivos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AsignaturaResponseDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(asignaturaService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<AsignaturaResponseDTO> crear(@Valid @RequestBody AsignaturaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(asignaturaService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AsignaturaResponseDTO> actualizar(@PathVariable Long id,
                                                            @Valid @RequestBody AsignaturaRequestDTO dto) {
        return ResponseEntity.ok(asignaturaService.actualizar(id, dto));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Void> cambiarEstado(@PathVariable Long id,
                                              @RequestParam boolean activo) {
        asignaturaService.cambiarEstado(id, activo);
        return ResponseEntity.noContent().build();
    }
}