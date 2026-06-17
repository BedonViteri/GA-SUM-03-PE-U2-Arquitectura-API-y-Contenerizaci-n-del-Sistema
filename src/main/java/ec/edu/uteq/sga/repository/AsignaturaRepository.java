package ec.edu.uteq.sga.repository;

import ec.edu.uteq.sga.entity.Asignatura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AsignaturaRepository extends JpaRepository<Asignatura, Long> {
    List<Asignatura> findByActivaTrue();
    boolean existsByNombre(String nombre);
    boolean existsByCodigo(String codigo);
}