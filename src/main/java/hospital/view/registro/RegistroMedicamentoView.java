package hospital.view.registro;

import hospital.controller.MedicamentoController;
import hospital.model.Administrador;
import hospital.model.Medicamento;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class RegistroMedicamentoView implements Initializable {

    @FXML
    private TextField txtCodigo;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtPresentacion;

    @FXML
    private Button btnGuardar;

    @FXML
    private Button btnVolver;

    private final MedicamentoController medicamentoController;
    private final Administrador administrador;

    public RegistroMedicamentoView() {
        this.medicamentoController = new MedicamentoController();
        this.administrador = new Administrador();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    private void guardar() {
        String codigo = txtCodigo.getText();
        String nombre = txtNombre.getText();
        String presentacion = txtPresentacion.getText();

        if (codigo == null || codigo.trim().isEmpty()) {
            mostrarError("El código es obligatorio.");
            return;
        }

        if (nombre == null || nombre.trim().isEmpty()) {
            mostrarError("El nombre es obligatorio.");
            return;
        }

        if (presentacion == null || presentacion.trim().isEmpty()) {
            mostrarError("La presentación es obligatoria.");
            return;
        }

        try {
            Medicamento medicamento = new Medicamento(
                    codigo.trim(),
                    nombre.trim(),
                    presentacion.trim()
            );

            medicamentoController.agregar(administrador, medicamento);

            // Cerrar ventana tras agregar exitosamente
            Stage stage = (Stage) btnGuardar.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            mostrarError("Error al agregar medicamento: " + e.getMessage());
        }
    }

    @FXML
    private void Volver() {
        Stage stage = (Stage) btnVolver.getScene().getWindow();
        stage.close();
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}