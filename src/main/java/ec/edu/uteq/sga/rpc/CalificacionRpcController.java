package ec.edu.uteq.sga.rpc;

import ec.edu.uteq.sga.rpc.DocenteRpcClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * CalificacionRpcController
 * -------------------------
 * Este controller recibe peticiones HTTP del frontend React
 * y las convierte en llamadas RPC al sga-docente.
 *
 * Flujo completo:
 * React → HTTP → CalificacionRpcController → RPC → sga-docente → BD
 */
@RestController
@RequestMapping("/api/rpc/calificaciones")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CalificacionRpcController {

    private final DocenteRpcClient docenteRpcClient;

    /**
     * GET /api/rpc/calificaciones/{idMatricula}/{trimestre}
     * Obtiene todas las calificaciones de un estudiante en un trimestre
     */
    @GetMapping("/{idMatricula}/{trimestre}")
    public ResponseEntity<Object[]> obtenerCalificaciones(
            @PathVariable Long idMatricula,
            @PathVariable Integer trimestre) {

        Object[] calificaciones = docenteRpcClient.obtenerCalificaciones(idMatricula, trimestre);
        return ResponseEntity.ok(calificaciones);
    }

    /**
     * GET /api/rpc/calificaciones/promedio-formativo/{idMatricula}/{trimestre}
     * Calcula el promedio formativo (70%)
     */
    @GetMapping("/promedio-formativo/{idMatricula}/{trimestre}")
    public ResponseEntity<Double> promedioFormativo(
            @PathVariable Long idMatricula,
            @PathVariable Integer trimestre) {

        Double promedio = docenteRpcClient.calcularPromedioFormativo(idMatricula, trimestre);
        return ResponseEntity.ok(promedio);
    }

    /**
     * GET /api/rpc/calificaciones/promedio-final/{idMatricula}/{trimestre}
     * Calcula el promedio final
     */
    @GetMapping("/promedio-final/{idMatricula}/{trimestre}")
    public ResponseEntity<Double> promedioFinal(
            @PathVariable Long idMatricula,
            @PathVariable Integer trimestre) {

        Double promedio = docenteRpcClient.calcularPromedioFinal(idMatricula, trimestre);
        return ResponseEntity.ok(promedio);
    }

    /**
     * POST /api/rpc/calificaciones/registrar
     * Registra una nueva calificación via RPC
     * Body: { "idMatricula": 1, "idActividad": 2, "nota": 9.5, "trimestre": 1 }
     */
    @PostMapping("/registrar")
    public ResponseEntity<Map<String, Object>> registrarCalificacion(
            @RequestBody Map<String, Object> body) {

        Long idMatricula  = Long.valueOf(body.get("idMatricula").toString());
        Long idActividad  = Long.valueOf(body.get("idActividad").toString());
        Double nota       = Double.valueOf(body.get("nota").toString());
        Integer trimestre = Integer.valueOf(body.get("trimestre").toString());

        Map<String, Object> resultado = docenteRpcClient
                .registrarCalificacion(idMatricula, idActividad, nota, trimestre);

        return ResponseEntity.ok(resultado);
    }

    /**
     * GET /api/rpc/calificaciones/cualitativa/{nota}
     * Convierte nota numérica a equivalencia cualitativa (A+, B-, etc.)
     */
    @GetMapping("/cualitativa/{nota}")
    public ResponseEntity<String> convertirACualitativa(@PathVariable Double nota) {
        String cualitativa = docenteRpcClient.convertirACualitativa(nota);
        return ResponseEntity.ok(cualitativa);
    }
}
