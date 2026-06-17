package ec.edu.uteq.sga.rpc;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * DocenteRpcClient
 * ----------------
 * Este es el CLIENTE RPC.
 * Desde aquí el sga-principal llama métodos del sga-docente
 * como si fueran locales — eso es exactamente lo que hace RPC.
 *
 * El servidor está en: http://localhost:9090/RPC2
 */
@Component
public class DocenteRpcClient {

    private static final String URL_SERVIDOR = "http://localhost:9090/RPC2";

    private XmlRpcClient crearCliente() throws Exception {
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setServerURL(new URL(URL_SERVIDOR));
        config.setEnabledForExtensions(true);
        config.setConnectionTimeout(5000);  // 5 segundos máximo
        config.setReplyTimeout(10000);      // 10 segundos para respuesta

        XmlRpcClient cliente = new XmlRpcClient();
        cliente.setConfig(config);
        return cliente;
    }

    /**
     * Llama al procedimiento remoto: DocenteService.obtenerCalificaciones
     * Retorna la lista de calificaciones de un estudiante en un trimestre.
     */
    @SuppressWarnings("unchecked")
    public Object[] obtenerCalificaciones(Long idMatricula, Integer trimestre) {
        try {
            XmlRpcClient cliente = crearCliente();
            Object[] params = new Object[]{idMatricula, trimestre};
            // "DocenteService.obtenerCalificaciones" → nombre registrado en el servidor
            Object resultado = cliente.execute("DocenteService.obtenerCalificaciones", params);
            return (Object[]) resultado;
        } catch (Exception e) {
            System.err.println("Error RPC obtenerCalificaciones: " + e.getMessage());
            return new Object[0];
        }
    }

    /**
     * Llama al procedimiento remoto: DocenteService.calcularPromedioFormativo
     */
    public Double calcularPromedioFormativo(Long idMatricula, Integer trimestre) {
        try {
            XmlRpcClient cliente = crearCliente();
            Object[] params = new Object[]{idMatricula, trimestre};
            Object resultado = cliente.execute("DocenteService.calcularPromedioFormativo", params);
            return ((Number) resultado).doubleValue();
        } catch (Exception e) {
            System.err.println("Error RPC calcularPromedioFormativo: " + e.getMessage());
            return 0.0;
        }
    }

    /**
     * Llama al procedimiento remoto: DocenteService.calcularPromedioFinal
     */
    public Double calcularPromedioFinal(Long idMatricula, Integer trimestre) {
        try {
            XmlRpcClient cliente = crearCliente();
            Object[] params = new Object[]{idMatricula, trimestre};
            Object resultado = cliente.execute("DocenteService.calcularPromedioFinal", params);
            return ((Number) resultado).doubleValue();
        } catch (Exception e) {
            System.err.println("Error RPC calcularPromedioFinal: " + e.getMessage());
            return 0.0;
        }
    }

    /**
     * Llama al procedimiento remoto: DocenteService.registrarCalificacion
     * Retorna un mapa con: exitoso (boolean), mensaje (String), idCalificacion (long)
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> registrarCalificacion(Long idMatricula, Long idActividad,
                                                      Double nota, Integer trimestre) {
        try {
            XmlRpcClient cliente = crearCliente();
            Object[] params = new Object[]{idMatricula, idActividad, nota, trimestre};
            Object resultado = cliente.execute("DocenteService.registrarCalificacion", params);
            return (Map<String, Object>) resultado;
        } catch (Exception e) {
            System.err.println("Error RPC registrarCalificacion: " + e.getMessage());
            return Map.of("exitoso", false, "mensaje", e.getMessage(), "idCalificacion", 0L);
        }
    }

    /**
     * Llama al procedimiento remoto: DocenteService.convertirACualitativa
     */
    public String convertirACualitativa(Double nota) {
        try {
            XmlRpcClient cliente = crearCliente();
            Object[] params = new Object[]{nota};
            Object resultado = cliente.execute("DocenteService.convertirACualitativa", params);
            return resultado.toString();
        } catch (Exception e) {
            System.err.println("Error RPC convertirACualitativa: " + e.getMessage());
            return "—";
        }
    }
}
