package ec.edu.uteq.sga.service;

import ec.edu.uteq.sga.dto.asignatura.*;
import ec.edu.uteq.sga.entity.Asignatura;
import ec.edu.uteq.sga.repository.AsignaturaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AsignaturaService {

    private final AsignaturaRepository asignaturaRepo;

    public List<AsignaturaResponseDTO> listarTodos() {
        return asignaturaRepo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<AsignaturaResponseDTO> listarActivos() {
        return asignaturaRepo.findByActivaTrue().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public AsignaturaResponseDTO obtenerPorId(Long id) {
        return toDTO(buscarPorId(id));
    }

    @Transactional
    public AsignaturaResponseDTO crear(AsignaturaRequestDTO dto) {
        if (asignaturaRepo.existsByNombre(dto.getNombre()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe una asignatura con ese nombre");

        if (dto.getCodigo() != null && asignaturaRepo.existsByCodigo(dto.getCodigo()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe una asignatura con ese código");

        Asignatura asignatura = Asignatura.builder()
                .nombre(dto.getNombre())
                .codigo(dto.getCodigo())
                .descripcion(dto.getDescripcion())
                .horasSemanales(dto.getHorasSemanales())
                .activa(true)
                .build();

        return toDTO(asignaturaRepo.save(asignatura));
    }

    @Transactional
    public AsignaturaResponseDTO actualizar(Long id, AsignaturaRequestDTO dto) {
        Asignatura asignatura = buscarPorId(id);

        asignatura.setNombre(dto.getNombre());
        asignatura.setCodigo(dto.getCodigo());
        asignatura.setDescripcion(dto.getDescripcion());
        asignatura.setHorasSemanales(dto.getHorasSemanales());

        return toDTO(asignaturaRepo.save(asignatura));
    }

    @Transactional
    public void cambiarEstado(Long id, boolean activo) {
        Asignatura asignatura = buscarPorId(id);
        asignatura.setActiva(activo);
        asignaturaRepo.save(asignatura);
    }

    private Asignatura buscarPorId(Long id) {
        return asignaturaRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Asignatura no encontrada"));
    }

    private AsignaturaResponseDTO toDTO(Asignatura a) {
        return AsignaturaResponseDTO.builder()
                .idAsignatura(a.getIdAsignatura())
                .nombre(a.getNombre())
                .codigo(a.getCodigo())
                .descripcion(a.getDescripcion())
                .horasSemanales(a.getHorasSemanales())
                .activo(a.isActiva())
                .fechaCreacion(a.getFechaCreacion())
                .build();
    }
}