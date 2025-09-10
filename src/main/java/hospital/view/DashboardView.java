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
    private Label lblUsuario;

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
    private Button btnHistoricoRecetas;

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

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;

        if (lblUsuario != null && usuario != null) {
            lblUsuario.setText(usuario.getNombre());
        }
    }

    @FXML
    public void initialize() {
        System.out.println("initialize called. lblUsuario: " + (lblUsuario != null ? "not null" : "null"));
        if (usuario != null && lblUsuario != null) {
            lblUsuario.setText(usuario.getNombre() + " / " + usuario.getId());
            System.out.println("lblUsuario text set in initialize to: " + lblUsuario.getText());
        }
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

    @FXML
    public void irAMedicos() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/hospital/view/medicosAdmin.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) btnMedicos.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Administración de Médicos");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error al cargar la vista de médicos: " + e.getMessage());
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
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error al cargar la vista de farmaceutas: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    public void irAPacientes() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("pacientesAdmin.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) btnPacientes.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Administración de Pacientes");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error al cargar la vista de pacientes.");
            alert.showAndWait();
        }
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
    public void irAPreescribirReceta() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/hospital/view/prescribirReceta.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) btnPrescribirReceta.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Prescribir Receta");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error al cargar la vista de prescribir receta.");
            alert.showAndWait();
        }
    }

    @FXML
    public void irADespacho() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/hospital/view/despachoFarmaceuta.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) btnDespachoReceta.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Despacho de recetas");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error al cargar la vista de Despacho.");
            alert.showAndWait();
        }
    }

    @FXML
    public void irAHistoricoRecetas() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/hospital/view/historicoRecetas.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) btnHistoricoRecetas.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Historial de Recetas");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error al cargar la vista de historial de recetas.");
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
}
