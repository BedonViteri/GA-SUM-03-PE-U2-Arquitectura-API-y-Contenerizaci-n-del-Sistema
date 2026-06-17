package ec.edu.uteq.sga.dto.asignatura;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AsignaturaRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    private String nombre;

    @Size(max = 20)
    private String codigo;

    private String descripcion;

    private Short horasSemanales;
}