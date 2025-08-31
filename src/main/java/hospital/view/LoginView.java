package hospital.view;

import hospital.controller.LoginController;
import hospital.model.entidades.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class LoginView {
    @FXML
    private TextField TXT_Usuario;
    @FXML
    private PasswordField PWF_Clave;
    @FXML
    private TextField TXT_ClaveVisible;
    @FXML
    private Button BTN_VisibilidadClave;
    @FXML
    private Button BTN_Login;
    @FXML
    private Button BTN_Salir;

    private final LoginController loginController = new LoginController();
    private boolean claveVisible = false;

    @FXML
    private void login() {
        String id = TXT_Usuario.getText();
        String clave = claveVisible ? TXT_ClaveVisible.getText() : PWF_Clave.getText();

        try {
            Usuario u = loginController.login(id, clave);
            System.out.println("Bienvenido " + u.getNombre());

            // cargar la vista principal(esto puede cambiar si el menu correcto no es ese)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/hospital/view/Menu_Admin_View.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = new Stage();
            stage.setTitle("Menú Principal");
            stage.setScene(scene);
            stage.show();

            // cerrar la ventana de login
            Stage loginStage = (Stage) BTN_Login.getScene().getWindow();
            loginStage.close();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
    }
    @FXML
    private void salir() {
        System.exit(0);
    }
    @FXML
    private void toggleVisibilidadClave() {
        claveVisible = !claveVisible;

        if (claveVisible) {
            TXT_ClaveVisible.setText(PWF_Clave.getText());
            TXT_ClaveVisible.setVisible(true);
            PWF_Clave.setVisible(false);
        } else {
            PWF_Clave.setText(TXT_ClaveVisible.getText());
            PWF_Clave.setVisible(true);
            TXT_ClaveVisible.setVisible(false);
        }
    }
}
