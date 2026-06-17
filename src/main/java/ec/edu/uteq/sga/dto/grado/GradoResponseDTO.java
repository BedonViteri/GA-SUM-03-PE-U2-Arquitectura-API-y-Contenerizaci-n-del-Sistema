package ec.edu.uteq.sga.dto.grado;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class GradoResponseDTO {
    private Long idGrado;
    private String nombre;
    private Short orden;
    private Short capacidadMax;
    private boolean activo;
    private String nivelEducativo;
}