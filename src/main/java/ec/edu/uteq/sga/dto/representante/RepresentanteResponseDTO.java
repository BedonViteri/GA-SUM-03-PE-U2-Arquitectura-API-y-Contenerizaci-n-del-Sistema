package ec.edu.uteq.sga.dto.representante;

import lombok.*;
import java.time.Instant;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RepresentanteResponseDTO {
    private Long idRepresentante;
    private String cedula;
    private String nombres;
    private String apellidos;
    private String parentesco;
    private String telefonoPrincipal;
    private String telefonoAlt;
    private String correo;
    private String direccion;
    private Instant fechaCreacion;
}