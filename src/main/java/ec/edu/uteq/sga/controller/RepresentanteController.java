package ec.edu.uteq.sga.controller;

import ec.edu.uteq.sga.dto.representante.*;
import ec.edu.uteq.sga.service.RepresentanteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/representantes")
@RequiredArgsConstructor
public class RepresentanteController {

    private final RepresentanteService representanteService;

    @GetMapping
    public ResponseEntity<List<RepresentanteResponseDTO>> listar() {
        return ResponseEntity.ok(representanteService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RepresentanteResponseDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(representanteService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<RepresentanteResponseDTO> crear(@Valid @RequestBody RepresentanteRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(representanteService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RepresentanteResponseDTO> actualizar(@PathVariable Long id,
                                                               @Valid @RequestBody RepresentanteRequestDTO dto) {
        return ResponseEntity.ok(representanteService.actualizar(id, dto));
    }
}