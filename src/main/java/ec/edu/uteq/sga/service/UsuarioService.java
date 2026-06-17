package ec.edu.uteq.sga.service;

import ec.edu.uteq.sga.dto.usuario.*;
import ec.edu.uteq.sga.entity.*;
import ec.edu.uteq.sga.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepo;
    private final RolRepository rolRepo;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    private static final String CHARS = "ABCDEFGHJKMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz23456789@#$!";

    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioRepo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UsuarioResponseDTO obtenerPorId(Long id) {
        return toDTO(buscarPorId(id));
    }

    @Transactional
    public UsuarioResponseDTO crear(UsuarioRequestDTO dto) {
        if (usuarioRepo.existsByCorreo(dto.getCorreo()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El correo ya está registrado");

        String username = generarUsername(dto.getNombres(), dto.getApellidos());
        String passwordPlano = generarPassword();

        Set<Rol> roles = resolverRoles(dto.getRoles());

        Usuario usuario = Usuario.builder()
                .username(username)
                .correo(dto.getCorreo())
                .passwordHash(passwordEncoder.encode(passwordPlano))
                .estado(true)
                .primerIngreso(true)
                .intentosFallidos(0)
                .roles(roles)
                .build();

        usuarioRepo.save(usuario);

        String nombreCompleto = dto.getNombres() + " " + dto.getApellidos();
        emailService.enviarCredenciales(dto.getCorreo(), nombreCompleto, username, passwordPlano);

        return toDTO(usuario);
    }

    @Transactional
    public UsuarioResponseDTO actualizar(Long id, UsuarioUpdateDTO dto) {
        Usuario usuario = buscarPorId(id);
        if (dto.getCorreo() != null) usuario.setCorreo(dto.getCorreo());
        if (dto.getRoles() != null) usuario.setRoles(resolverRoles(dto.getRoles()));
        return toDTO(usuarioRepo.save(usuario));
    }

    @Transactional
    public void cambiarEstado(Long id, String estado) {
        Usuario usuario = buscarPorId(id);
        usuario.setEstado(true);
        usuarioRepo.save(usuario);
    }

    @Transactional
    public void resetearPassword(Long id) {
        Usuario usuario = buscarPorId(id);
        String passwordPlano = generarPassword();
        usuario.setPasswordHash(passwordEncoder.encode(passwordPlano));
        usuario.setPrimerIngreso(true);
        usuario.setIntentosFallidos(0);
        usuario.setBloqueadoHasta(null);
        usuarioRepo.save(usuario);

        emailService.enviarCredenciales(
                usuario.getCorreo(),
                usuario.getUsername(),
                usuario.getUsername(),
                passwordPlano
        );
    }

    @Transactional
    public void cambiarPassword(String username, CambioPasswordDTO dto) {
        Usuario usuario = usuarioRepo.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        if (!passwordEncoder.matches(dto.getPasswordActual(), usuario.getPasswordHash()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Contraseña actual incorrecta");

        usuario.setPasswordHash(passwordEncoder.encode(dto.getPasswordNuevo()));
        usuario.setPrimerIngreso(false);
        usuarioRepo.save(usuario);
    }

    // ── Helpers ──────────────────────────────────────────

    private String generarUsername(String nombres, String apellidos) {
        String[] partesNombre = nombres.trim().toLowerCase().split("\\s+");
        String[] partesApellido = apellidos.trim().toLowerCase().split("\\s+");

        StringBuilder username = new StringBuilder();

        for (String n : partesNombre) {
            username.append(n.charAt(0));
        }

        username.append(partesApellido[0]);

        if (partesApellido.length > 1) {
            username.append(partesApellido[1].charAt(0));
        }

        String base = username.toString().replaceAll("[^a-z0-9]", "");

        String final_ = base;
        int contador = 1;
        while (usuarioRepo.existsByUsername(final_)) {
            final_ = base + contador;
            contador++;
        }

        return final_;
    }

    private String generarPassword() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            sb.append(CHARS.charAt(random.nextInt(CHARS.length())));
        }
        return sb.toString();
    }

    private Usuario buscarPorId(Long id) {
        return usuarioRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
    }

    private Set<Rol> resolverRoles(Set<Long> ids) {
        return ids.stream()
                .map(idRol -> rolRepo.findById(idRol)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rol no encontrado: " + idRol)))
                .collect(Collectors.toSet());
    }

    private UsuarioResponseDTO toDTO(Usuario u) {
        return UsuarioResponseDTO.builder()
                .idUsuario(u.getIdUsuario())
                .username(u.getUsername())
                .correo(u.getCorreo())
                .estado(u.getEstado())
                .primerIngreso(u.isPrimerIngreso())
                .intentosFallidos(u.getIntentosFallidos())
                .ultimoAcceso(u.getUltimoAcceso())
                .fechaCreacion(u.getFechaCreacion())
                .roles(u.getRoles().stream().map(Rol::getNombre).collect(Collectors.toSet()))
                .build();
    }
    @Transactional
    public void eliminar(Long id) {
        if (!usuarioRepo.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        usuarioRepo.deleteById(id);
    }
}