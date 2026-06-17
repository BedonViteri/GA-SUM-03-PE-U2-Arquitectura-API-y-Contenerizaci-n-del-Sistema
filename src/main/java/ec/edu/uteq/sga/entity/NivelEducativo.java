package ec.edu.uteq.sga.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "niveles_educativos", schema = "sga_principal")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class NivelEducativo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_nivel")
    private Long idNivel;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(name = "tipo_escala", nullable = false, length = 20)
    private String tipoEscala; // CUALITATIVA o CUANTITATIVA

    @Column(name = "grado_inicio")
    private Short gradoInicio;

    @Column(name = "grado_fin")
    private Short gradoFin;
}