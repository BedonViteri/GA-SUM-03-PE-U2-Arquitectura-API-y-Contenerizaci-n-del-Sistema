package ec.edu.uteq.sga.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "fichas_estudiante", schema = "sga_principal")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class FichaEstudiante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ficha")
    private Long idFicha;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_estudiante", nullable = false, unique = true)
    private Estudiante estudiante;

    @Column(name = "tipo_sangre", length = 5)
    private String tipoSangre;

    @Column(name = "enfermedad_catastrofica")
    private boolean enfermedadCatastrofica = false;

    @Column(name = "detalle_enfermedad", columnDefinition = "text")
    private String detalleEnfermedad;

    @Column(name = "medicacion_permanente", columnDefinition = "text")
    private String medicacionPermanente;

    @Column(columnDefinition = "text")
    private String alergias;

    @Column(name = "contacto_emergencia", length = 100)
    private String contactoEmergencia;

    @Column(name = "telefono_emergencia", length = 20)
    private String telefonoEmergencia;

    @Column(name = "direccion_referencia", columnDefinition = "text")
    private String direccionReferencia;

    @Column(name = "fecha_actualizacion")
    private Instant fechaActualizacion = Instant.now();
}