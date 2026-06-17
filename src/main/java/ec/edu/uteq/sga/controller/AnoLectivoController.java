package ec.edu.uteq.sga.controller;

import ec.edu.uteq.sga.dto.anolectivo.*;
import ec.edu.uteq.sga.service.AnoLectivoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/anos-lectivos")
@RequiredArgsConstructor
public class AnoLectivoController {

    private final AnoLectivoService anoLectivoService;

    @GetMapping
    public ResponseEntity<List<AnoLectivoResponseDTO>> listar() {
        return ResponseEntity.ok(anoLectivoService.listarTodos());
    }

    @GetMapping("/actual")
    public ResponseEntity<AnoLectivoResponseDTO> obtenerActual() {
        return ResponseEntity.ok(anoLectivoService.obtenerActual());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnoLectivoResponseDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(anoLectivoService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<AnoLectivoResponseDTO> crear(@Valid @RequestBody AnoLectivoRequestDTO dto,
                                                       Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(anoLectivoService.crear(dto, auth.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnoLectivoResponseDTO> actualizar(@PathVariable Long id,
                                                            @Valid @RequestBody AnoLectivoRequestDTO dto) {
        return ResponseEntity.ok(anoLectivoService.actualizar(id, dto));
    }

    @PatchMapping("/{id}/establecer-actual")
    public ResponseEntity<Void> establecerActual(@PathVariable Long id) {
        anoLectivoService.establecerActual(id);
        return ResponseEntity.noContent().build();
    }
}