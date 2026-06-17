package ec.edu.uteq.sga.repository;

import ec.edu.uteq.sga.entity.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long> {
    Optional<Persona> findByUsuario_IdUsuario(Long idUsuario);
    Optional<Persona> findByCedula(String cedula);
    // verificar que ya tienes este método o agrégalo
    Optional<Persona> findById(Long id);
    boolean existsByCedula(String cedula);
}