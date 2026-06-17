package ec.edu.uteq.sga.controller;

import ec.edu.uteq.sga.dto.AuthResponse;
import ec.edu.uteq.sga.dto.LoginRequest;
import ec.edu.uteq.sga.dto.usuario.CambioPasswordDTO;
import ec.edu.uteq.sga.entity.Usuario;
import ec.edu.uteq.sga.repository.UsuarioRepository;
import ec.edu.uteq.sga.security.JwtUtil;
import ec.edu.uteq.sga.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService; // ← agregar esto

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        Usuario usuario = usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow();

        List<String> roles = usuario.getRoles().stream()
                .map(r -> r.getNombre())
                .collect(Collectors.toList());

        String token = jwtUtil.generarToken(usuario.getUsername(), roles);

        return ResponseEntity.ok(AuthResponse.builder()
                .token(token)
                .username(usuario.getUsername())
                .correo(usuario.getCorreo())
                .roles(roles)
                .primerIngreso(usuario.isPrimerIngreso())
                .build());
    }

    @PatchMapping("/cambiar-password")
    public ResponseEntity<Void> cambiarPassword(
            Authentication auth,
            @Valid @RequestBody CambioPasswordDTO dto) {
        usuarioService.cambiarPassword(auth.getName(), dto);
        return ResponseEntity.noContent().build();
    }
}