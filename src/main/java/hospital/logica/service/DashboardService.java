package hospital.logica.service;

import hospital.datos.RecetaDAO;
import hospital.model.entidades.*;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

public class DashboardService {
    private final RecetaDAO recetaDAO;

    public DashboardService() {
        this.recetaDAO = new RecetaDAO();
    }
    // ============================
    // 1. Medicamentos prescritos por mes
    // ============================
    public Map<YearMonth, Integer> contarMedicamentosPorMes(Usuario usuario, String codigoMedicamento,
                                                            YearMonth desde,
                                                            YearMonth hasta) throws Exception {

        validarUsuario(usuario);

        Map<YearMonth, Integer> resultado = new HashMap<>();

        for (Receta receta : recetaDAO.listar()) {
            if (receta.getFecha() == null) continue;

            YearMonth ym = YearMonth.from(receta.getFecha());
            if (desde != null && ym.isBefore(desde)) continue;
            if (hasta != null && ym.isAfter(hasta)) continue;

            for (DetalleReceta detalle : receta.getDetalles()) {
                if (codigoMedicamento == null ||
                        detalle.getMedicamento().getCodigo().equalsIgnoreCase(codigoMedicamento)) {

                    resultado.merge(ym, detalle.getCantidad(), Integer::sum);
                }
            }
        }
        return resultado;
    }

    // ============================
    // 2. Recetas por estado
    // ============================
    public Map<EstadoReceta, Integer> contarRecetasPorEstado(Usuario usuario) throws Exception {
        validarUsuario(usuario);

        return recetaDAO.listar().stream()
                .collect(Collectors.groupingBy(Receta::getEstado, Collectors.summingInt(r -> 1)));
    }

    // ============================
    // Validación
    // ============================
    private void validarUsuario(Usuario usuario) throws Exception {
        if (usuario == null) {
            throw new Exception("Debe iniciar sesión para acceder al dashboard.");
        }
        if (!(usuario instanceof Medico
                || usuario instanceof Farmaceuta
                || usuario instanceof Administrador)) {
            throw new Exception("Usuario no autorizado para dashboard.");
        }
    }
}