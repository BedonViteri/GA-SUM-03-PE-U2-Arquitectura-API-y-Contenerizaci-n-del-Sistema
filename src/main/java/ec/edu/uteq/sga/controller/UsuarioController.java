package ec.edu.uteq.sga.controller;

import ec.edu.uteq.sga.dto.usuario.*;
import ec.edu.uteq.sga.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listar() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> crear(@Valid @RequestBody UsuarioRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> actualizar(@PathVariable Long id,
                                                         @Valid @RequestBody UsuarioUpdateDTO dto) {
        return ResponseEntity.ok(usuarioService.actualizar(id, dto));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Void> cambiarEstado(@PathVariable Long id,
                                              @RequestParam String estado) {
        usuarioService.cambiarEstado(id, estado);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/reset-password")
    public ResponseEntity<Void> resetearPassword(@PathVariable Long id) {
        usuarioService.resetearPassword(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/cambiar-password")
    public ResponseEntity<Void> cambiarPassword(Authentication auth,
                                                @Valid @RequestBody CambioPasswordDTO dto) {
        usuarioService.cambiarPassword(auth.getName(), dto);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}