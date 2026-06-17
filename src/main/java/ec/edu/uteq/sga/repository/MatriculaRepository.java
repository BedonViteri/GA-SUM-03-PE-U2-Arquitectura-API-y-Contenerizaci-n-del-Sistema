package ec.edu.uteq.sga.repository;

import ec.edu.uteq.sga.entity.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MatriculaRepository extends JpaRepository<Matricula, Long> {
    List<Matricula> findByAnoLectivo_IdAnoLectivo(Long idAnoLectivo);
    List<Matricula> findByEstudiante_IdEstudiante(Long idEstudiante);
    boolean existsByEstudiante_IdEstudianteAndAnoLectivo_IdAnoLectivo(Long idEstudiante, Long idAnoLectivo);
    Optional<Matricula> findTopByAnoLectivo_IdAnoLectivoOrderByNumeroOrdenDesc(Long idAnoLectivo);
}