package ec.edu.uteq.sga.service;

import ec.edu.uteq.sga.dto.grado.*;
import ec.edu.uteq.sga.entity.Grado;
import ec.edu.uteq.sga.entity.NivelEducativo;
import ec.edu.uteq.sga.repository.GradoRepository;
import ec.edu.uteq.sga.repository.NivelEducativoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GradoService {

    private final GradoRepository gradoRepo;
    private final NivelEducativoRepository nivelRepo;

    public List<GradoResponseDTO> listarTodos() {
        return gradoRepo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<GradoResponseDTO> listarActivos() {
        return gradoRepo.findByActivoTrue().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public GradoResponseDTO obtenerPorId(Long id) {
        return toDTO(buscarPorId(id));
    }

    @Transactional
    public GradoResponseDTO crear(GradoRequestDTO dto) {
        if (gradoRepo.existsByNombre(dto.getNombre()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe ese grado");

        NivelEducativo nivel = null;
        if (dto.getIdNivel() != null)
            nivel = nivelRepo.findById(dto.getIdNivel())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nivel educativo no encontrado"));

        Grado grado = Grado.builder()
                .nombre(dto.getNombre())
                .orden(dto.getOrden())
                .capacidadMax(dto.getCapacidadMax())
                .activo(true)
                .nivelEducativo(nivel)
                .build();

        return toDTO(gradoRepo.save(grado));
    }

    @Transactional
    public GradoResponseDTO actualizar(Long id, GradoRequestDTO dto) {
        Grado grado = buscarPorId(id);
        grado.setNombre(dto.getNombre());
        grado.setOrden(dto.getOrden());
        grado.setCapacidadMax(dto.getCapacidadMax());
        if (dto.getIdNivel() != null) {
            NivelEducativo nivel = nivelRepo.findById(dto.getIdNivel())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nivel educativo no encontrado"));
            grado.setNivelEducativo(nivel);
        }
        return toDTO(gradoRepo.save(grado));
    }

    @Transactional
    public void cambiarEstado(Long id, boolean activo) {
        Grado grado = buscarPorId(id);
        grado.setActivo(activo);
        gradoRepo.save(grado);
    }

    private Grado buscarPorId(Long id) {
        return gradoRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Grado no encontrado"));
    }

    private GradoResponseDTO toDTO(Grado g) {
        return GradoResponseDTO.builder()
                .idGrado(g.getIdGrado())
                .nombre(g.getNombre())
                .orden(g.getOrden())
                .capacidadMax(g.getCapacidadMax())
                .activo(g.isActivo())
                .nivelEducativo(g.getNivelEducativo() != null ? g.getNivelEducativo().getNombre() : null)
                .build();
    }
}
