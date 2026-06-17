package ec.edu.uteq.sga.dto.representante;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RepresentanteRequestDTO {

    @Size(max = 20)
    private String cedula;

    @NotBlank(message = "Los nombres son obligatorios")
    @Size(max = 100)
    private String nombres;

    @NotBlank(message = "Los apellidos son obligatorios")
    @Size(max = 100)
    private String apellidos;

    @Size(max = 50)
    private String parentesco;

    @NotBlank(message = "El teléfono principal es obligatorio")
    @Size(max = 20)
    private String telefonoPrincipal;

    @Size(max = 20)
    private String telefonoAlt;

    @Email
    @Size(max = 100)
    private String correo;

    private String direccion;
}