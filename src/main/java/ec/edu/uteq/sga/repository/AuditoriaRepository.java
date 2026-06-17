package ec.edu.uteq.sga.repository;

import ec.edu.uteq.sga.entity.Auditoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AuditoriaRepository extends JpaRepository<Auditoria, Long> {
    List<Auditoria> findByUsuario_IdUsuario(Long idUsuario);
    List<Auditoria> findByTablaAfectada(String tablaAfectada);
    List<Auditoria> findByAccion(String accion);
}