package ec.edu.uteq.sga.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles", schema = "sga_principal")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    private Long idRol;

    @Column(nullable = false, unique = true, length = 50, columnDefinition = "varchar")
    private String nombre;

    @Column
    private String descripcion;

    @Column
    private boolean activo = true;


}