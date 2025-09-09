package hospital.controller;

import hospital.logica.RecetaLogica;
import hospital.model.EstadoReceta;
import hospital.model.Receta;
import hospital.model.DetalleReceta;
import hospital.model.Usuario;
import java.io.IOException;
import java.time.YearMonth;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class DashboardController {
    private final RecetaLogica recetaLogica;

    @FXML
    private Button btnMedicos;

    @FXML
    private Button btnMedicamentos;

    public DashboardController() {
        this.recetaLogica = new RecetaLogica();
    }

    @FXML
    public void irAMedicamentos() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/hospital/view/medicamentosAdmin.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Medicamentos");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void irAMedicos() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/hospital/view/medicosAdmin.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Médicos");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
