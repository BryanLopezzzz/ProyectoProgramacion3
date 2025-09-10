package hospital.view;

import hospital.model.Usuario;
import hospital.controller.LoginController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import hospital.view.Alerta;

public class CambioClaveView {

    @FXML
    private PasswordField txtContrasenaAnterior;

    @FXML
    private PasswordField txtNuevaContrasena;

    @FXML
    private PasswordField txtConfirmarContrasena;

    @FXML
    private Button btnCambiar;

    @FXML
    private Button btnVerAnterior;

    @FXML
    private Button btnVerNueva;

    @FXML
    private Button btnVerConfirmar;

    private Usuario usuario;
    private LoginController loginController;

    public void setUsuario(Usuario usuario) {

        this.usuario = usuario;
    }
    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }

    @FXML
    private void cambiarContrasena() {
        String actual = txtContrasenaAnterior.getText();
        String nueva = txtNuevaContrasena.getText();
        String confirmar = txtConfirmarContrasena.getText();

        if (actual.isEmpty() || nueva.isEmpty() || confirmar.isEmpty()) {
            Alerta.error("Error", "Todos los campos son obligatorios.");
            return;
        }
        if (!nueva.equals(confirmar)) {
            Alerta.error("Error", "La confirmación no coincide con la nueva contraseña.");
            return;
        }

        try {
            loginController.cambiarClave(actual, nueva);
            Alerta.info("Éxito", "Contraseña cambiada correctamente.");
            limpiarCampos();
        } catch (Exception e) {
            Alerta.error("Error", e.getMessage());
        }
    }

    @FXML
    private void verContrasenaAnterior() {

    }

    @FXML
    private void verNuevaContrasena() {

    }

    @FXML
    private void verConfirmarContrasena() {

    }

    private void limpiarCampos() {
        txtContrasenaAnterior.clear();
        txtNuevaContrasena.clear();
        txtConfirmarContrasena.clear();
    }
}
