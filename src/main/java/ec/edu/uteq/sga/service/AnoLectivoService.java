package ec.edu.uteq.sga.service;

import ec.edu.uteq.sga.dto.anolectivo.*;
import ec.edu.uteq.sga.entity.AnoLectivo;
import ec.edu.uteq.sga.repository.AnoLectivoRepository;
import ec.edu.uteq.sga.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnoLectivoService {

    private final AnoLectivoRepository anoLectivoRepo;
    private final UsuarioRepository usuarioRepo;
    @Transactional(readOnly = true)
    public List<AnoLectivoResponseDTO> listarTodos()  {
        return anoLectivoRepo.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public AnoLectivoResponseDTO obtenerPorId(Long id) {
        return toDTO(buscarPorId(id));
    }

    @Transactional
    public AnoLectivoResponseDTO crear(AnoLectivoRequestDTO dto, String username) {
        if (anoLectivoRepo.existsByNombre(dto.getNombre()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un año lectivo con ese nombre");

        if (dto.getFechaFin().isBefore(dto.getFechaInicio()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La fecha fin no puede ser anterior a la fecha inicio");

        var creador = usuarioRepo.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        AnoLectivo ano = AnoLectivo.builder()
                .nombre(dto.getNombre())
                .fechaInicio(dto.getFechaInicio())
                .fechaFin(dto.getFechaFin())
                .esActual(false)
                .creadoPor(creador)
                .build();

        return toDTO(anoLectivoRepo.save(ano));
    }

    @Transactional
    public AnoLectivoResponseDTO actualizar(Long id, AnoLectivoRequestDTO dto) {
        AnoLectivo ano = buscarPorId(id);

        if (dto.getFechaFin().isBefore(dto.getFechaInicio()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La fecha fin no puede ser anterior a la fecha inicio");

        ano.setNombre(dto.getNombre());
        ano.setFechaInicio(dto.getFechaInicio());
        ano.setFechaFin(dto.getFechaFin());

        return toDTO(anoLectivoRepo.save(ano));
    }

    @Transactional
    public void establecerActual(Long id) {
        // Desactivar todos
        anoLectivoRepo.findAll().forEach(a -> {
            a.setEsActual(false);
            anoLectivoRepo.save(a);
        });

        // Activar el seleccionado
        AnoLectivo ano = buscarPorId(id);
        ano.setEsActual(true);
        anoLectivoRepo.save(ano);
    }
    @Transactional(readOnly = true)
    public AnoLectivoResponseDTO obtenerActual() {
        return anoLectivoRepo.findByEsActualTrue()
                .map(this::toDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No hay año lectivo activo"));
    }

    // Helpers
    private AnoLectivo buscarPorId(Long id) {
        return anoLectivoRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Año lectivo no encontrado"));
    }

    private AnoLectivoResponseDTO toDTO(AnoLectivo a) {
        return AnoLectivoResponseDTO.builder()
                .idAnoLectivo(a.getIdAnoLectivo())
                .nombre(a.getNombre())
                .fechaInicio(a.getFechaInicio())
                .fechaFin(a.getFechaFin())
                .esActual(a.isEsActual())
                .fechaCreacion(a.getFechaCreacion())
                .creadoPor(a.getCreadoPor() != null ? a.getCreadoPor().getUsername() : null)
                .build();
    }


}