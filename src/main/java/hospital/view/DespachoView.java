package hospital.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class DespachoView {
    @FXML
    private TextField txtBuscar;

    @FXML
    private TableView tblRecetas;

    @FXML
    private TableColumn colIdentificacionReceta;

    @FXML
    private TableColumn colNombreMedicamento;

    @FXML
    private TableColumn colPresentacion;

    @FXML
    private TableColumn colFechaConfeccion;

    @FXML
    private TableColumn colEstado;

    @FXML
    private Button btnVerDetalle;

    @FXML
    private Button btnVolver;

    @FXML
    private Button btnBuscar;

    @FXML
    public void VerDetalle(ActionEvent event) {

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

