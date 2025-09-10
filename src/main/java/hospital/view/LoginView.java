package hospital.view;

import hospital.controller.LoginController;
import hospital.model.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class LoginView {
    @FXML
    private TextField txtUsuario;
    @FXML
    private PasswordField txtClave;
    @FXML
    private TextField txtClaveVisible;
    @FXML
    private Button btnVerClave;
    @FXML
    private javafx.scene.image.ImageView imgVerClave;
    @FXML
    private Button btnEntrar;

    private final LoginController loginController = new LoginController();
    private boolean claveVisible = false;

    @FXML
    private void login() {
        String id = txtUsuario.getText();
        String clave = claveVisible ? txtClaveVisible.getText() : txtClave.getText();

        try {
            Usuario u = loginController.login(id, clave);

            // cargar la vista principal(esto puede cambiar si el menu correcto no es ese)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/hospital/view/dashboard.fxml"));
            Scene scene = new Scene(loader.load());

            // mandar el usuario al dashboard
            DashboardView dashboardView = loader.getController();
            dashboardView.setUsuario(u);

            Stage stage = new Stage();
            stage.setTitle("Menú Principal");
            stage.setScene(scene);
            stage.show();

            // cerrar la ventana de login
            Stage loginStage = (Stage) btnEntrar.getScene().getWindow();
            loginStage.close();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
    }

    @FXML
    private void toggleVisibilidadClave() {
        claveVisible = !claveVisible;

        if (claveVisible) {
            txtClaveVisible.setText(txtClave.getText());
            txtClaveVisible.setVisible(true);
            txtClave.setVisible(false);
            imgVerClave.setImage(new javafx.scene.image.Image(getClass().getResourceAsStream("/icons/eye-off.png")));
        } else {
            txtClave.setText(txtClaveVisible.getText());
            txtClave.setVisible(true);
            txtClaveVisible.setVisible(false);
            imgVerClave.setImage(new javafx.scene.image.Image(getClass().getResourceAsStream("/icons/eye.png")));
        }
    }
}
