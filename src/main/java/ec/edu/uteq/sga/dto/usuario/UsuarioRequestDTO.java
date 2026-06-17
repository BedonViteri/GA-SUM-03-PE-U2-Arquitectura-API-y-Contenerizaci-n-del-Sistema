package ec.edu.uteq.sga.dto.usuario;

import jakarta.validation.constraints.*;
import lombok.*;
import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UsuarioRequestDTO {

    @NotBlank(message = "Los nombres son obligatorios")
    @Size(max = 100)
    private String nombres;

    @NotBlank(message = "Los apellidos son obligatorios")
    @Size(max = 100)
    private String apellidos;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Correo inválido")
    private String correo;

    @NotEmpty(message = "Debe tener al menos un rol")
    private Set<Long> roles;
}