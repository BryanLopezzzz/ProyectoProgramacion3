package hospital.controller;

import hospital.logica.RecetaLogica;
import hospital.model.EstadoReceta;
import hospital.model.Receta;
import hospital.model.DetalleReceta;
import hospital.model.Usuario;
import java.time.YearMonth;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class DashboardController {
    private final RecetaLogica recetaLogica;

    public DashboardController() {
        this.recetaLogica = new RecetaLogica();
    }

   //Cuenta cuántas unidades de un medicamento se prescribieron por mes
    public Map<YearMonth, Integer> contarMedicamentosPorMes(
            Usuario usuario, String codigoMedicamento,
            YearMonth desde, YearMonth hasta) throws Exception {

        validarUsuario(usuario);

        List<Receta> recetas = recetaLogica.listar();

        return recetas.stream()
                .filter(r -> r.getFecha() != null)
                .filter(r -> {
                    YearMonth ym = YearMonth.from(r.getFecha());
                    return !ym.isBefore(desde) && !ym.isAfter(hasta);
                })
                .flatMap(r -> r.getDetalles().stream()
                        .filter(d -> d.getMedicamento().getCodigo().equalsIgnoreCase(codigoMedicamento))
                        .map(d -> Map.entry(YearMonth.from(r.getFecha()), d.getCantidad()))
                )
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        Integer::sum // en caso de colisión (varios detalles mismo mes)
                ));
    }

    //Cuenta recetas por estado
    public Map<EstadoReceta, Integer> contarRecetasPorEstado(Usuario usuario) throws Exception {
        validarUsuario(usuario);

        List<Receta> recetas = recetaLogica.listar();

        return recetas.stream()
                .collect(Collectors.groupingBy(
                        Receta::getEstado,
                        Collectors.summingInt(r -> 1)
                ));
    }

    private void validarUsuario(Usuario usuario) throws Exception {
        if (usuario == null) {
            throw new Exception("Debe haber un usuario autenticado para acceder al dashboard.");
        }
    }
}
