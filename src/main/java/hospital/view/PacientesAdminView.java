package hospital.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class PacientesAdminView {
    @FXML
    private TextField txtBuscar;

    @FXML
    private ComboBox<String> btnFiltro;

    @FXML
    private TableView tblPacientes;

    @FXML
    private TableColumn colIdentificacionPaciente;

    @FXML
    private TableColumn colNombrePaciente;

    @FXML
    private TableColumn colTelefonoPaciente;

    @FXML
    private TableColumn colFechaNac;

    @FXML
    private Button btnAgregarPaciente;

    @FXML
    private Button btnEliminarPaciente;

    @FXML
    private Button btnEditarPaciente;

    @FXML
    private Button btnVolver;

    @FXML
    private Button btnReporte;

    @FXML
    private Button btnBuscar;

    @FXML
    public void Filtrar(ActionEvent event) {

    }

    @FXML
    public void AgregarPaciente(ActionEvent event) {

    }

    @FXML
    public void EliminarPaciente(ActionEvent event) {

    }

    @FXML
    public void EditarPaciente(ActionEvent event) {

    }

    @FXML
    public void GenerarReporte(ActionEvent event) {

    }

    @FXML
    public void BuscarPaciente(ActionEvent event) {

    }

    @FXML
    private void Volver(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/hospital/view/dashboard.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) btnVolver.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Dashboard");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error al cargar el dashboard.");
            alert.showAndWait();
        }
    }
}
