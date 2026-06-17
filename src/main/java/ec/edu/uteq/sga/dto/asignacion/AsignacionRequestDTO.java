package ec.edu.uteq.sga.dto.asignacion;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AsignacionRequestDTO {

    @NotNull(message = "El docente es obligatorio")
    private Long idDocente;

    @NotNull(message = "La asignatura es obligatoria")
    private Long idAsignatura;

    @NotNull(message = "El grado es obligatorio")
    private Long idGrado;

    @NotNull(message = "El año lectivo es obligatorio")
    private Long idAnoLectivo;

    private boolean esTutor = false;
}