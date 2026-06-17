package ec.edu.uteq.sga.service;

import ec.edu.uteq.sga.dto.representante.*;
import ec.edu.uteq.sga.entity.Representante;
import ec.edu.uteq.sga.repository.RepresentanteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RepresentanteService {

    private final RepresentanteRepository representanteRepo;

    public List<RepresentanteResponseDTO> listarTodos() {
        return representanteRepo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public RepresentanteResponseDTO obtenerPorId(Long id) {
        return toDTO(buscarPorId(id));
    }

    @Transactional
    public RepresentanteResponseDTO crear(RepresentanteRequestDTO dto) {
        if (dto.getCedula() != null && representanteRepo.existsByCedula(dto.getCedula()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un representante con esa cédula");

        Representante rep = Representante.builder()
                .cedula(dto.getCedula())
                .nombres(dto.getNombres())
                .apellidos(dto.getApellidos())
                .parentesco(dto.getParentesco())
                .telefonoPrincipal(dto.getTelefonoPrincipal())
                .telefonoAlt(dto.getTelefonoAlt())
                .correo(dto.getCorreo())
                .direccion(dto.getDireccion())
                .build();

        return toDTO(representanteRepo.save(rep));
    }

    @Transactional
    public RepresentanteResponseDTO actualizar(Long id, RepresentanteRequestDTO dto) {
        Representante rep = buscarPorId(id);

        rep.setNombres(dto.getNombres());
        rep.setApellidos(dto.getApellidos());
        rep.setParentesco(dto.getParentesco());
        rep.setTelefonoPrincipal(dto.getTelefonoPrincipal());
        rep.setTelefonoAlt(dto.getTelefonoAlt());
        rep.setCorreo(dto.getCorreo());
        rep.setDireccion(dto.getDireccion());

        return toDTO(representanteRepo.save(rep));
    }

    private Representante buscarPorId(Long id) {
        return representanteRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Representante no encontrado"));
    }

    public RepresentanteResponseDTO toDTO(Representante r) {
        return RepresentanteResponseDTO.builder()
                .idRepresentante(r.getIdRepresentante())
                .cedula(r.getCedula())
                .nombres(r.getNombres())
                .apellidos(r.getApellidos())
                .parentesco(r.getParentesco())
                .telefonoPrincipal(r.getTelefonoPrincipal())
                .telefonoAlt(r.getTelefonoAlt())
                .correo(r.getCorreo())
                .direccion(r.getDireccion())
                .fechaCreacion(r.getFechaCreacion())
                .build();
    }
}