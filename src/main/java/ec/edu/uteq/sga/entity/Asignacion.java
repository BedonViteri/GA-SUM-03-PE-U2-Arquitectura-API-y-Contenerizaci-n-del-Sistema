package ec.edu.uteq.sga.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "asignaciones", schema = "sga_principal")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Asignacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_asignacion")
    private Long idAsignacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_docente", nullable = false)
    private Persona docente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_asignatura", nullable = false)
    private Asignatura asignatura;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_grado", nullable = false)
    private Grado grado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ano_lectivo", nullable = false)
    private AnoLectivo anoLectivo;

    @Column(name = "es_tutor")
    private boolean esTutor = false;

    @Column
    private boolean activo = true;

    @Column(name = "fecha_asignacion")
    private Instant fechaAsignacion = Instant.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asignado_por")
    private Usuario asignadoPor;
}