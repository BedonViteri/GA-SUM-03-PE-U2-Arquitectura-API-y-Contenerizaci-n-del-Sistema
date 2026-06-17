package ec.edu.uteq.sga.repository;

import ec.edu.uteq.sga.entity.Horario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HorarioRepository extends JpaRepository<Horario, Long> {
    List<Horario> findByAsignacion_IdAsignacion(Long idAsignacion);
    List<Horario> findByAsignacion_Grado_IdGrado(Long idGrado);
    List<Horario> findByAsignacion_Docente_IdPersona(Long idPersona);
}