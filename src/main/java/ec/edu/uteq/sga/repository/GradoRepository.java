package ec.edu.uteq.sga.repository;

import ec.edu.uteq.sga.entity.Grado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GradoRepository extends JpaRepository<Grado, Long> {
    List<Grado> findByActivoTrue();
    boolean existsByNombre(String nombre);
}