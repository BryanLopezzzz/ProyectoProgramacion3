package hospital.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

public class CambioClaveView {

    @FXML
    private PasswordField pwd_claveActual;
    @FXML
    private PasswordField pwd_claveNueva;
    @FXML
    private Button btn_visibilidadClaveActual;
    @FXML
    private Button btn_visibilidadClaveNueva;
    @FXML
    private Button btn_guardar;
    @FXML
    private Button btn_salir;

    // Campos de texto para mostrar las claves en texto plano
    private TextField txt_claveActualVisible;
    private TextField txt_claveNuevaVisible;

    @FXML
    public void initialize() {
        // Crear campos de texto para mostrar las claves en texto plano
        txt_claveActualVisible = new TextField();
        txt_claveNuevaVisible = new TextField();

        // Al inicio, ocultar los campos de texto visibles
        txt_claveActualVisible.setVisible(false);
        txt_claveActualVisible.setManaged(false);
        txt_claveNuevaVisible.setVisible(false);
        txt_claveNuevaVisible.setManaged(false);
    }

    private void configurarCamposVisibles() {

    }

    @FXML
    private void guardar(ActionEvent event) {
        try {

        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo guardar");
        }
    }

    // ====================
    // SALIR
    // ====================

    @FXML
    private void toggleVisibilidadClaveActual(ActionEvent event) {

    }

    // ====================
    // VISIBILIDAD CLAVE NUEVA
    // ====================
    @FXML
    private void toggleVisibilidadClaveNueva(ActionEvent event) {

    }

    private void mostrarAlerta(String titulo, String mensaje){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}