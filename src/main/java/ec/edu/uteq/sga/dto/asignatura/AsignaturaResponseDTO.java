package ec.edu.uteq.sga.dto.asignatura;

import lombok.*;
import java.time.Instant;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AsignaturaResponseDTO {
    private Long idAsignatura;
    private String nombre;
    private String codigo;
    private String descripcion;
    private Short horasSemanales;
    private boolean activo;
    private Instant fechaCreacion;
}