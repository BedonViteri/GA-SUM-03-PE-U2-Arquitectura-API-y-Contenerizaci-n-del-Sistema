package ec.edu.uteq.sga.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "periodos_evaluacion", schema = "sga_principal")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PeriodoEvaluacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_periodo")
    private Long idPeriodo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ano_lectivo", nullable = false)
    private AnoLectivo anoLectivo;

    @Column(nullable = false, length = 20)
    private String tipo; // PRIMER_TRIMESTRE, SEGUNDO_TRIMESTRE, TERCER_TRIMESTRE

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    @Column
    private boolean activo = true;
}