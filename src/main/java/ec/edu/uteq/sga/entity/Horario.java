package ec.edu.uteq.sga.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalTime;

@Entity
@Table(name = "horarios", schema = "sga_principal")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Horario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_horario")
    private Long idHorario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_asignacion", nullable = false)
    private Asignacion asignacion;

    @Column(name = "dia_semana", nullable = false)
    private Short diaSemana; // 1=Lunes, 2=Martes, ... 5=Viernes

    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;

    @Column(name = "hora_fin", nullable = false)
    private LocalTime horaFin;

    @Column(length = 50)
    private String aula;
}