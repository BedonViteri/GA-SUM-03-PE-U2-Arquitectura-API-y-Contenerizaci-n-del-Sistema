package ec.edu.uteq.sga.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "representantes", schema = "sga_principal")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Representante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_representante")
    private Long idRepresentante;

    @Column(unique = true, length = 20)
    private String cedula;

    @Column(nullable = false, length = 100)
    private String nombres;

    @Column(nullable = false, length = 100)
    private String apellidos;

    @Column(length = 50)
    private String parentesco;

    @Column(name = "telefono_principal", nullable = false, length = 20)
    private String telefonoPrincipal;

    @Column(name = "telefono_alt", length = 20)
    private String telefonoAlt;

    @Column(length = 100)
    private String correo;

    @Column(columnDefinition = "text")
    private String direccion;

    @Column(name = "fecha_creacion")
    private Instant fechaCreacion = Instant.now();
}