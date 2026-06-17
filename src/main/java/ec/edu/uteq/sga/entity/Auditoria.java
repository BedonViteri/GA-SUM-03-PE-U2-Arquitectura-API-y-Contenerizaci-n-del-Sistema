package ec.edu.uteq.sga.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "auditoria", schema = "sga_principal")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_auditoria")
    private Long idAuditoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @Column(length = 50)
    private String username;

    @Column(nullable = false, length = 50)
    private String accion;

    @Column(name = "tabla_afectada", length = 50)
    private String tablaAfectada;

    @Column(name = "registro_id")
    private Long registroId;

    @Column(columnDefinition = "text")
    private String descripcion;

    @Column(name = "ip_address", length = 50)
    private String ipAddress;

    @Column(name = "user_agent", columnDefinition = "text")
    private String userAgent;

    @Column(nullable = false)
    private Instant fecha = Instant.now();
}