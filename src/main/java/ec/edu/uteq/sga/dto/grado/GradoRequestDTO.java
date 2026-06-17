package ec.edu.uteq.sga.dto.grado;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class GradoRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    private String nombre;

    @NotNull(message = "El orden es obligatorio")
    private Short orden;

    private Short capacidadMax = 35;

    private Long idNivel;
}