package ec.edu.uteq.sga.controller;

import ec.edu.uteq.sga.rpc.DocenteRpcClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/calificaciones")
@RequiredArgsConstructor
public class CalificacionesController {

    private final DocenteRpcClient docenteRpcClient;

    @GetMapping("/matricula/{idMatricula}/trimestre/{trimestre}")
    public ResponseEntity<?> obtenerCalificaciones(
            @PathVariable Long idMatricula,
            @PathVariable Integer trimestre) {

        Object[] calificacionesRaw = docenteRpcClient.obtenerCalificaciones(idMatricula, trimestre);
        Double promedioFormativo   = docenteRpcClient.calcularPromedioFormativo(idMatricula, trimestre);
        Double promedioFinal       = docenteRpcClient.calcularPromedioFinal(idMatricula, trimestre);

        List<Map<String, Object>> calificaciones = new ArrayList<>();
        for (Object obj : calificacionesRaw) {
            @SuppressWarnings("unchecked")
            Map<String, Object> c = (Map<String, Object>) obj;
            calificaciones.add(c);
        }

        Map<String, Object> resultado = new HashMap<>();
        resultado.put("promedioFormativo", promedioFormativo);
        resultado.put("promedioSumativo",  0.0);
        resultado.put("promedioFinal",     promedioFinal);
        resultado.put("calificaciones",    calificaciones);

        return ResponseEntity.ok(resultado);
    }
}