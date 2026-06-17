package ec.edu.uteq.sga.dto.anolectivo;

import lombok.*;
import java.time.Instant;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AnoLectivoResponseDTO {
    private Long idAnoLectivo;
    private String nombre;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private boolean esActual;
    private Instant fechaCreacion;
    private String creadoPor;
}