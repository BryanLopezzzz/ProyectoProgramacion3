package hospital.view;

import hospital.controller.HistoricoRecetasController;
import hospital.model.Receta;
import hospital.model.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;

public class HistoricoRecetasView {

    @FXML
    private TableView<Receta> tblRecetas;
    @FXML
    private TableColumn<Receta, String> colIdentificacionReceta;
    @FXML
    private TableColumn<Receta, String> colNombreMedicamento;
    @FXML
    private TableColumn<Receta, String> colPresentacion;
    @FXML
    private TableColumn<Receta, String> colFechaConfeccion;
    @FXML
    private TableColumn<Receta, String> colEstado;
    @FXML
    private Button btnVerDetalle;
    @FXML
    private Button btnVolver;
    @FXML
    private TextField txtBuscar;
    @FXML
    private ComboBox<String> cmbFiltrar;

    @FXML
    private void VerDetalle() {

    }

    @FXML
    private void Volver(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/hospital/view/dashboard.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) btnVolver.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Dashboard");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
