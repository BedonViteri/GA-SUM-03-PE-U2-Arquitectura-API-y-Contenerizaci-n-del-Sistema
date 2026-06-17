package ec.edu.uteq.sga.service;

import ec.edu.uteq.sga.dto.estudiante.*;
import ec.edu.uteq.sga.entity.Estudiante;
import ec.edu.uteq.sga.entity.Representante;
import ec.edu.uteq.sga.entity.Usuario;
import ec.edu.uteq.sga.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EstudianteService {

    private final EstudianteRepository estudianteRepo;
    private final RepresentanteRepository representanteRepo;
    private final UsuarioRepository usuarioRepo;

    @Transactional(readOnly = true)
    public List<EstudianteResponseDTO> listarTodos() {
        return estudianteRepo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EstudianteResponseDTO> buscar(String query) {
        return estudianteRepo
                .findByNombresContainingIgnoreCaseOrApellidosContainingIgnoreCase(query, query)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EstudianteResponseDTO obtenerPorId(Long id) {
        return toDTO(buscarPorId(id));
    }

    @Transactional
    public EstudianteResponseDTO crear(EstudianteRequestDTO dto, String username) {
        if (dto.getCedula() != null && estudianteRepo.existsByCedula(dto.getCedula()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un estudiante con esa cédula");

        Representante representante = null;
        if (dto.getIdRepresentante() != null)
            representante = representanteRepo.findById(dto.getIdRepresentante())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Representante no encontrado"));

        Usuario creador = usuarioRepo.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        Estudiante estudiante = Estudiante.builder()
                .cedula(dto.getCedula())
                .nombres(dto.getNombres())
                .apellidos(dto.getApellidos())
                .fechaNacimiento(dto.getFechaNacimiento())
                .genero(dto.getGenero())
                .direccion(dto.getDireccion())
                .telefono(dto.getTelefono())
                .correo(dto.getCorreo())
                .discapacidad(dto.isDiscapacidad())
                .tipoDiscapacidad(dto.getTipoDiscapacidad())
                .porcentajeDisc(dto.getPorcentajeDisc())
                .estado("ACTIVO")
                .representante(representante)
                .creadoPor(creador)
                .build();

        return toDTO(estudianteRepo.save(estudiante));
    }

    @Transactional
    public EstudianteResponseDTO actualizar(Long id, EstudianteRequestDTO dto) {
        Estudiante estudiante = buscarPorId(id);

        estudiante.setNombres(dto.getNombres());
        estudiante.setApellidos(dto.getApellidos());
        estudiante.setFechaNacimiento(dto.getFechaNacimiento());
        estudiante.setGenero(dto.getGenero());
        estudiante.setDireccion(dto.getDireccion());
        estudiante.setTelefono(dto.getTelefono());
        estudiante.setCorreo(dto.getCorreo());
        estudiante.setDiscapacidad(dto.isDiscapacidad());
        estudiante.setTipoDiscapacidad(dto.getTipoDiscapacidad());
        estudiante.setPorcentajeDisc(dto.getPorcentajeDisc());

        if (dto.getIdRepresentante() != null) {
            Representante rep = representanteRepo.findById(dto.getIdRepresentante())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Representante no encontrado"));
            estudiante.setRepresentante(rep);
        }

        return toDTO(estudianteRepo.save(estudiante));
    }

    @Transactional
    public void cambiarEstado(Long id, String estado) {
        Estudiante estudiante = buscarPorId(id);
        estudiante.setEstado(estado);
        estudianteRepo.save(estudiante);
    }

    private Estudiante buscarPorId(Long id) {
        return estudianteRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Estudiante no encontrado"));
    }

    private EstudianteResponseDTO toDTO(Estudiante e) {
        return EstudianteResponseDTO.builder()
                .idEstudiante(e.getIdEstudiante())
                .cedula(e.getCedula())
                .codigoEstudiante(e.getCodigoEstudiante())
                .nombres(e.getNombres())
                .apellidos(e.getApellidos())
                .fechaNacimiento(e.getFechaNacimiento())
                .genero(e.getGenero())
                .direccion(e.getDireccion())
                .telefono(e.getTelefono())
                .correo(e.getCorreo())
                .discapacidad(e.isDiscapacidad())
                .tipoDiscapacidad(e.getTipoDiscapacidad())
                .porcentajeDisc(e.getPorcentajeDisc())
                .estado(e.getEstado())
                .representante(e.getRepresentante() != null ?
                        e.getRepresentante().getNombres() + " " + e.getRepresentante().getApellidos() : null)
                .fechaCreacion(e.getFechaCreacion())
                .build();
    }
}