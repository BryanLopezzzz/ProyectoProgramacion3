package hospital.controller;

import hospital.logica.EstadisticaRecetaLogica;
import hospital.model.Usuario;

import java.time.YearMonth;
import java.util.LinkedHashMap;

public class DashboardController {
    private final EstadisticaRecetaLogica estadistica;

    public DashboardController() {
        this.estadistica = new EstadisticaRecetaLogica();
    }

    public LinkedHashMap<String, Integer> contarMedicamentosPorMes(
            Usuario usuario,
            YearMonth desde,
            YearMonth hasta
    ) throws Exception {
        validarUsuario(usuario);
        return estadistica.medicamentosPorMes(desde, hasta);
    }

    public LinkedHashMap<String, Long> contarRecetasPorEstado(Usuario usuario) throws Exception {
        validarUsuario(usuario);
        return estadistica.recetasPorEstado();
    }

    private void validarUsuario(Usuario usuario) throws Exception {
        if (usuario == null) {
            throw new Exception("Debe haber un usuario autenticado para acceder al dashboard.");
        }
    }
}
