package hospital.view;

import hospital.controller.RecetaController;
import hospital.model.Medico;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class DetalleRecetaView {

    @FXML
    private TextArea txtIndicaciones;

    @FXML
    private TextField txtCantidad;

    @FXML
    private TextField txtDuracion;

    @FXML
    private Button btnAgregar;

    @FXML
    private Button btnVolver;

    private final RecetaController recetaController = new RecetaController();

    // Estos se inyectan desde la vista anterior
    private Medico medico;
    private String recetaId;
    private String medicamentoId;

    public void setContext(Medico medico, String recetaId, String medicamentoId) {
        this.medico = medico;
        this.recetaId = recetaId;
        this.medicamentoId = medicamentoId;
    }

    @FXML
    private void Agregar() {
        try {
            int cantidad = Integer.parseInt(txtCantidad.getText().trim());
            int dias = Integer.parseInt(txtDuracion.getText().trim());
            String indicaciones = txtIndicaciones.getText().trim();

            recetaController.agregarDetalle(medico, recetaId, medicamentoId, cantidad, indicaciones, dias);

            mostrarInfo("Detalle agregado correctamente.");
            cerrarVentana();
        } catch (Exception e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    private void Volver() {
        cerrarVentana();
    }

    private void cerrarVentana() {
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

    private void mostrarInfo(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
