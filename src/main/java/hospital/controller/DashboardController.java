package hospital.controller;

import hospital.model.entidades.*;
import hospital.logica.service.DashboardService;
import java.time.YearMonth;
import java.util.Map;

public class DashboardController {
    private final DashboardService dashboardService;

    public DashboardController() {
        this.dashboardService = new DashboardService();
    }

    public Map<YearMonth, Integer> contarMedicamentosPorMes(Usuario usuario, String codigoMedicamento,
                                                            YearMonth desde,
                                                            YearMonth hasta) throws Exception {
        return dashboardService.contarMedicamentosPorMes(usuario, codigoMedicamento, desde, hasta);
    }

    public Map<EstadoReceta, Integer> contarRecetasPorEstado(Usuario usuario) throws Exception {
        return dashboardService.contarRecetasPorEstado(usuario);
    }
}
