package ec.edu.uteq.sga.repository;

import ec.edu.uteq.sga.entity.Asignacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AsignacionRepository extends JpaRepository<Asignacion, Long> {
    List<Asignacion> findByAnoLectivo_IdAnoLectivo(Long idAnoLectivo);
    List<Asignacion> findByDocente_IdPersona(Long idDocente);
    boolean existsByDocente_IdPersonaAndAsignatura_IdAsignaturaAndGrado_IdGradoAndAnoLectivo_IdAnoLectivo(
            Long idDocente, Long idAsignatura, Long idGrado, Long idAnoLectivo);
}