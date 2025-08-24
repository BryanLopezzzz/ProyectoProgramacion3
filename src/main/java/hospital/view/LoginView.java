package hospital.view;

import hospital.controller.LoginController;
import hospital.model.entidades.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.stage.Stage;
public class LoginView {
    @FXML
    private TextField TXT_Usuario;

    @FXML // este se usará para mostrar el texto plano cuando se presione el ojito
    private TextField TXT_ClaveVisible;

    @FXML
    private PasswordField PWF_Clave;

    @FXML
    private Button BTN_Login;

    @FXML
    private Button BTN_Salir;

    @FXML
    private Button BTN_VisibilidadClave;

    private final LoginController loginController = new LoginController();

    public void initialize() {
        // Al inicio, ocultar el campo de texto visible
        TXT_ClaveVisible.setVisible(false);
        TXT_ClaveVisible.setManaged(false); // evita ocupar espacio en layout
    }
    // ====================
    // LOGIN
    // ====================
    @FXML
    private void login(ActionEvent event) {
        try {
            String usuario = TXT_Usuario.getText();
            String clave = PWF_Clave.isVisible() ? PWF_Clave.getText() : TXT_ClaveVisible.getText();

            Usuario u = loginController.login(usuario, clave);

            mostrarAlerta("Éxito", "Bienvenido " + u.getNombre(), Alert.AlertType.INFORMATION);

            Stage stage = (Stage) BTN_Login.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            mostrarAlerta("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    // ====================
    // SALIR
    // ====================
    @FXML
    private void salir(ActionEvent event) {
        Stage stage = (Stage) BTN_Salir.getScene().getWindow();
        stage.close();
    }

    // ====================
    // VISIBILIDAD CLAVE
    // ====================
    @FXML
    private void toggleVisibilidadClave(ActionEvent event) {
        if (PWF_Clave.isVisible()) {
            // mostrar clave en texto plano
            TXT_ClaveVisible.setText(PWF_Clave.getText());
            TXT_ClaveVisible.setVisible(true);
            TXT_ClaveVisible.setManaged(true);

            PWF_Clave.setVisible(false);
            PWF_Clave.setManaged(false);
        } else {
            // volver a ocultar clave
            PWF_Clave.setText(TXT_ClaveVisible.getText());
            PWF_Clave.setVisible(true);
            PWF_Clave.setManaged(true);

            TXT_ClaveVisible.setVisible(false);
            TXT_ClaveVisible.setManaged(false);
        }
    }

    // ====================
    // ALERTAS
    // ====================
    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
