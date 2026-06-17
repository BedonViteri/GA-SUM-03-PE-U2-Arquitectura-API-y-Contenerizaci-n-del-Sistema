package ec.edu.uteq.sga.dto.estudiante;

import lombok.*;
import java.time.Instant;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class EstudianteResponseDTO {
    private Long idEstudiante;
    private String cedula;
    private String codigoEstudiante;
    private String nombres;
    private String apellidos;
    private LocalDate fechaNacimiento;
    private String genero;
    private String direccion;
    private String telefono;
    private String correo;
    private boolean discapacidad;
    private String tipoDiscapacidad;
    private Short porcentajeDisc;
    private String estado;
    private String representante;
    private Instant fechaCreacion;
}