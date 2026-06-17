package ec.edu.uteq.sga.dto.usuario;

import jakarta.validation.constraints.*;
import lombok.*;
import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UsuarioUpdateDTO {

    @Email(message = "Correo inválido")
    private String correo;

    private Set<Long> roles;
}