package ec.edu.uteq.sga.dto.asignacion;

import lombok.*;
import java.time.Instant;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AsignacionResponseDTO {
    private Long idAsignacion;
    private String docente;
    private String asignatura;
    private String grado;
    private String anoLectivo;
    private boolean esTutor;
    private boolean activo;
    private Instant fechaAsignacion;
    private String asignadoPor;
}