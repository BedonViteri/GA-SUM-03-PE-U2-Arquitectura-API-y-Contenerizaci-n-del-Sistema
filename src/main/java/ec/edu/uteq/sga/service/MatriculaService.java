package ec.edu.uteq.sga.service;

import ec.edu.uteq.sga.dto.matricula.*;
import ec.edu.uteq.sga.entity.*;
import ec.edu.uteq.sga.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatriculaService {

    private final MatriculaRepository matriculaRepo;
    private final EstudianteRepository estudianteRepo;
    private final GradoRepository gradoRepo;
    private final AnoLectivoRepository anoLectivoRepo;
    private final UsuarioRepository usuarioRepo;

    @Transactional(readOnly = true)
    public List<MatriculaResponseDTO> listarTodos() {
        return matriculaRepo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MatriculaResponseDTO> listarPorAnoLectivo(Long idAnoLectivo) {
        return matriculaRepo.findByAnoLectivo_IdAnoLectivo(idAnoLectivo)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MatriculaResponseDTO> listarPorEstudiante(Long idEstudiante) {
        return matriculaRepo.findByEstudiante_IdEstudiante(idEstudiante)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MatriculaResponseDTO obtenerPorId(Long id) {
        return toDTO(buscarPorId(id));
    }

    @Transactional
    public MatriculaResponseDTO crear(MatriculaRequestDTO dto, String username) {
        // Validar que no esté matriculado ya en ese año
        if (matriculaRepo.existsByEstudiante_IdEstudianteAndAnoLectivo_IdAnoLectivo(
                dto.getIdEstudiante(), dto.getIdAnoLectivo()))
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "El estudiante ya tiene matrícula en ese año lectivo");

        Estudiante estudiante = estudianteRepo.findById(dto.getIdEstudiante())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estudiante no encontrado"));

        Grado grado = gradoRepo.findById(dto.getIdGrado())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Grado no encontrado"));

        AnoLectivo anoLectivo = anoLectivoRepo.findById(dto.getIdAnoLectivo())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Año lectivo no encontrado"));

        Usuario registrador = usuarioRepo.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        // Calcular número de orden
        Short numeroOrden = matriculaRepo
                .findTopByAnoLectivo_IdAnoLectivoOrderByNumeroOrdenDesc(dto.getIdAnoLectivo())
                .map(m -> (short)(m.getNumeroOrden() + 1))
                .orElse((short) 1);

        Matricula matricula = Matricula.builder()
                .estudiante(estudiante)
                .grado(grado)
                .anoLectivo(anoLectivo)
                .numeroOrden(numeroOrden)
                .fechaRegistro(LocalDate.now())
                .estado("ACTIVO")
                .observaciones(dto.getObservaciones())
                .registradoPor(registrador)
                .build();

        return toDTO(matriculaRepo.save(matricula));
    }

    @Transactional
    public void cambiarEstado(Long id, String estado) {
        Matricula matricula = buscarPorId(id);
        matricula.setEstado(estado);
        matriculaRepo.save(matricula);
    }

    private Matricula buscarPorId(Long id) {
        return matriculaRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Matrícula no encontrada"));
    }

    private MatriculaResponseDTO toDTO(Matricula m) {
        return MatriculaResponseDTO.builder()
                .idMatricula(m.getIdMatricula())
                .estudiante(m.getEstudiante().getNombres() + " " + m.getEstudiante().getApellidos())
                .cedulaEstudiante(m.getEstudiante().getCedula())
                .grado(m.getGrado().getNombre())
                .anoLectivo(m.getAnoLectivo().getNombre())
                .numeroOrden(m.getNumeroOrden())
                .fechaRegistro(m.getFechaRegistro())
                .estado(m.getEstado())
                .observaciones(m.getObservaciones())
                .registradoPor(m.getRegistradoPor() != null ? m.getRegistradoPor().getUsername() : null)
                .fechaCreacion(m.getFechaCreacion())
                .build();
    }
}