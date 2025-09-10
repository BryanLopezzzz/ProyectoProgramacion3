package hospital.view;

import hospital.controller.MedicoController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class MedicosAdminView {
    @FXML
    private Button btnVolver;

    @FXML
    public void AgregarMedico(ActionEvent event) {

    }

    @FXML
    public void EliminarMedico(ActionEvent event) {

    }

    @FXML
    public void EditarMedico(ActionEvent event) {

    }

    @FXML
    public void GenerarReporte(ActionEvent event) {

    }

    @FXML
    public void Buscar(ActionEvent event) {

    }

    @FXML
    private void Volver() {
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