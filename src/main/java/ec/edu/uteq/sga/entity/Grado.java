package ec.edu.uteq.sga.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "grados", schema = "sga_principal")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Grado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_grado")
    private Long idGrado;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false)
    private Short orden;

    @Column(name = "capacidad_max")
    private Short capacidadMax = 35;

    @Column
    private boolean activo = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nivel")
    private NivelEducativo nivelEducativo;
}