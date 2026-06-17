package ec.edu.uteq.sga.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "usuarios", schema = "sga_principal")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 100)
    private String correo;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Builder.Default
    @Column(nullable = false)
    private Boolean estado = true;

    @Builder.Default
    @Column(name = "primer_ingreso")
    private boolean primerIngreso = true;

    @Builder.Default
    @Column(name = "intentos_fallidos")
    private int intentosFallidos = 0;

    @Column(name = "bloqueado_hasta")
    private Instant bloqueadoHasta;

    @Column(name = "ultimo_acceso")
    private Instant ultimoAcceso;

    @Builder.Default
    @Column(name = "fecha_creacion")
    private Instant fechaCreacion = Instant.now();

    @Builder.Default
    @Column(name = "fecha_actualizacion")
    private Instant fechaActualizacion = Instant.now();

    @Builder.Default
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "usuario_roles",
            schema = "sga_principal",
            joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_rol")
    )
    private Set<Rol> roles = new HashSet<>();
}