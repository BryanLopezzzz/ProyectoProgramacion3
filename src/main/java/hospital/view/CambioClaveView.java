package hospital.view;

import hospital.model.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;

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

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @FXML
    private void cambiarContrasena() {

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
}
