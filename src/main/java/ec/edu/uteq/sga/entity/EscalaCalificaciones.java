package ec.edu.uteq.sga.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "escala_calificaciones", schema = "sga_principal")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class EscalaCalificaciones {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_escala")
    private Long idEscala;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ano_lectivo", nullable = false)
    private AnoLectivo anoLectivo;

    @Column(name = "nota_minima", nullable = false)
    private BigDecimal notaMinima;

    @Column(name = "nota_maxima", nullable = false)
    private BigDecimal notaMaxima;

    @Column(nullable = false, length = 10)
    private String descripcion;

    @Column(nullable = false)
    private boolean aprobado = true;
}