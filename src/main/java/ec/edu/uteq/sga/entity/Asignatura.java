package ec.edu.uteq.sga.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "asignaturas", schema = "sga_principal")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Asignatura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_asignatura")
    private Long idAsignatura;

    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    @Column(unique = true, length = 20)
    private String codigo;

    @Column(columnDefinition = "text")
    private String descripcion;

    @Column(name = "horas_semana")
    private Short horasSemanales;

    @Column
    private boolean activa = true;

    @Column(name = "fecha_creacion")
    private Instant fechaCreacion = Instant.now();
}