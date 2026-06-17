package ec.edu.uteq.sga.dto;

import lombok.*;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AuthResponse {

    private String token;
    private String username;
    private String correo;
    private List<String> roles;
    private boolean primerIngreso;
}