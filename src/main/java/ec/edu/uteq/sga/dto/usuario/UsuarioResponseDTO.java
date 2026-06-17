package ec.edu.uteq.sga.dto.usuario;

import lombok.*;
import java.time.Instant;
import java.util.Set;
import ec.edu.uteq.sga.entity.EstadoUsuario; // ← agregar esto

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UsuarioResponseDTO {
    private Long idUsuario;
    private String username;
    private String correo;
    private boolean estado;
    private boolean primerIngreso;
    private int intentosFallidos;
    private Instant ultimoAcceso;
    private Instant fechaCreacion;
    private Set<String> roles;
}