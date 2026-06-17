package ec.edu.uteq.sga.repository;

import ec.edu.uteq.sga.entity.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {
    Optional<Estudiante> findByCedula(String cedula);
    boolean existsByCedula(String cedula);
    List<Estudiante> findByEstado(String estado);
    List<Estudiante> findByNombresContainingIgnoreCaseOrApellidosContainingIgnoreCase(String nombres, String apellidos);
}