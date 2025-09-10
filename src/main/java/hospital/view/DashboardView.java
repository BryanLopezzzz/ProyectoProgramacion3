package hospital.view;

import hospital.controller.DashboardController;
// import hospital.model.Usuario;
import hospital.controller.LoginController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardView {
    @FXML
    private Button btnMedicos;
    @FXML
    private Button btnFarmaceutas;
    @FXML
    private Button btnPacientes;
    @FXML
    private Button btnMedicamentos;
    @FXML
    private Button btnPrescribirReceta;
    @FXML
    private Button btnDespachoReceta;
    @FXML
    private Button btnHistoricoReceta;
    @FXML
    private Button btnAcercaDe;
    @FXML
    private Button btnLogout;

    private final DashboardController dashboardController = new DashboardController();

    public void irAMedicos() {
        dashboardController.irAMedicos();
    }
}
