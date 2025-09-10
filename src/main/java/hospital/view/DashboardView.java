package hospital.view;

import hospital.controller.DashboardController;
// import hospital.model.Usuario;
import hospital.controller.LoginController;
import hospital.model.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.Pane;
import java.io.IOException;
import java.time.YearMonth;

public class DashboardView {
    @FXML
    private Button btnMedicos;
    @FXML
    private Button btnFarmaceutas;
    @FXML
    private Button btnPacientes;
    @FXML
    private Button btnMedicamentos;
    @FXML
    private Button btnPrescribirReceta;
    @FXML
    private Button btnDespachoReceta;
    @FXML
    private Button btnHistoricoReceta;
    @FXML
    private Button btnAcercaDe;
    @FXML
    private Button btnLogout;
    @FXML
    private Pane paneLineChart;
    @FXML
    private Pane panePieChart;

    private final DashboardController dashboardController = new DashboardController();
    private Usuario usuario;

    @FXML
    public void irAMedicos() {
        // Aquí cargas otra vista o escena
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Ir a gestión de médicos");
        alert.showAndWait();
    }
    @FXML
    public void irAMedicamentos() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("medicamentosAdmin.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) btnMedicamentos.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Administración de Medicamentos");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error al cargar la vista de medicamentos.");
            alert.showAndWait();
        }
    }

    @FXML
    public void irAFarmaceutas() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/hospital/view/farmaceutasAdmin.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) btnFarmaceutas.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Administración de Farmaceutas");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error al cargar la vista de farmaceutas.");
            alert.showAndWait();
        }
    }

    @FXML
    public void irAAcercaDe() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/hospital/view/acercaDe1.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) btnAcercaDe.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Acerca de");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error al cargar la vista de Acerca de.");
            alert.showAndWait();
        }
    }

    @FXML
    public void initialize() {
        mostrarGraficoLineas();
        mostrarGraficoPastel();
    }

    private void mostrarGraficoLineas() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Medicamentos por mes");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Medicamentos");

        try {
            var datos = dashboardController.contarMedicamentosPorMes(
                    usuario,
                    "A001",
                    YearMonth.now().minusMonths(5),
                    YearMonth.now()
            );
            datos.forEach((mes, cantidad) -> series.getData().add(new XYChart.Data<>(mes, cantidad)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        lineChart.getData().add(series);
        paneLineChart.getChildren().clear();
        paneLineChart.getChildren().add(lineChart);
    }

    private void mostrarGraficoPastel() {
        PieChart pieChart = new PieChart();
        pieChart.setTitle("Recetas por estado");

        try {
            var datos = dashboardController.contarRecetasPorEstado(usuario);
            datos.forEach((estado, cantidad) ->
                    pieChart.getData().add(new PieChart.Data(estado, cantidad))
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        panePieChart.getChildren().clear();
        panePieChart.getChildren().add(pieChart);
    }
}
