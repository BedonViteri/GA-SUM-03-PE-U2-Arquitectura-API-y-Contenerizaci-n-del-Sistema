package ec.edu.uteq.sga.dto.estudiante;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class EstudianteRequestDTO {

    @Size(max = 20)
    private String cedula;

    @NotBlank(message = "Los nombres son obligatorios")
    @Size(max = 100)
    private String nombres;

    @NotBlank(message = "Los apellidos son obligatorios")
    @Size(max = 100)
    private String apellidos;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    private LocalDate fechaNacimiento;

    @Size(max = 1)
    private String genero;

    private String direccion;

    @Size(max = 20)
    private String telefono;

    @Email
    @Size(max = 100)
    private String correo;

    private boolean discapacidad = false;

    private String tipoDiscapacidad;

    private Short porcentajeDisc;

    private Long idRepresentante;
}