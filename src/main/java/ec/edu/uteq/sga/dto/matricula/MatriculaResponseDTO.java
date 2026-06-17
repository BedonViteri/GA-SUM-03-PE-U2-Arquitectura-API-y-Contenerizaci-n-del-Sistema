package ec.edu.uteq.sga.dto.matricula;

import lombok.*;
import java.time.Instant;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MatriculaResponseDTO {
    private Long idMatricula;
    private String estudiante;
    private String cedulaEstudiante;
    private String grado;
    private String anoLectivo;
    private Short numeroOrden;
    private LocalDate fechaRegistro;
    private String estado;
    private String observaciones;
    private String registradoPor;
    private Instant fechaCreacion;
}