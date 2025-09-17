package hospital.viewController.registro;

import hospital.controller.FarmaceutaController;
import hospital.model.Administrador;
import hospital.model.Farmaceuta;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class RegistroFarmaceutaViewController {

    @FXML
    private TextField txtIdentificacion;

    @FXML
    private TextField txtNombre;

    @FXML
    private Button btnGuardar;

    @FXML
    private Button btnVolver;

    private final FarmaceutaController farmaceutaController = new FarmaceutaController();
    private final Administrador admin = new Administrador(); // admin logueado

    @FXML
    public void initialize() {

        configurarValidaciones();
    }

    private void configurarValidaciones() {
        txtIdentificacion.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z0-9]*")) {
                txtIdentificacion.setText(oldValue);
            }
        });

        txtNombre.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*")) {
                txtNombre.setText(oldValue);
            }
        });
    }

    @FXML
    public void guardarFarmaceuta(ActionEvent event) {
        if (!validarCampos()) {
            return;
        }

        try {
            Farmaceuta nuevoFarmaceuta = new Farmaceuta();
            nuevoFarmaceuta.setId(txtIdentificacion.getText().trim());
            nuevoFarmaceuta.setNombre(txtNombre.getText().trim());

            farmaceutaController.agregar(admin, nuevoFarmaceuta);

            mostrarInfo("Farmaceuta agregado exitosamente.");

            limpiarCampos();

            volverABusqueda();

        } catch (Exception e) {
            mostrarError("Error al agregar farmaceuta: " + e.getMessage());
        }
    }

    @FXML
    public void Volver(ActionEvent event) {
        volverABusqueda();
    }

    private boolean validarCampos() {
        StringBuilder errores = new StringBuilder();

        String id = txtIdentificacion.getText().trim();
        if (id.isEmpty()) {
            errores.append("- El ID es obligatorio.\n");
        } else if (id.length() < 3) {
            errores.append("- El ID debe tener al menos 3 caracteres.\n");
        }

        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) {
            errores.append("- El nombre es obligatorio.\n");
        } else if (nombre.length() < 2) {
            errores.append("- El nombre debe tener al menos 2 caracteres.\n");
        }

        if (errores.length() > 0) {
            mostrarError("Por favor corrija los siguientes errores:\n\n" + errores.toString());
            return false;
        }

        return true;
    }

    private void limpiarCampos() {
        txtIdentificacion.clear();
        txtNombre.clear();
        txtIdentificacion.requestFocus();
    }

    private void volverABusqueda() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/hospital/viewController/farmaceutasAdmin.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = (Stage) btnVolver.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Gestión de Farmaceutas");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarError("Error al volver a la vista de búsqueda: " + e.getMessage());
        }
    }

    public void cargarDatos(String id, String nombre) {
        if (id != null) txtIdentificacion.setText(id);
        if (nombre != null) txtNombre.setText(nombre);
    }

    // Método para configurar si es edición (deshabilitar ID)
    public void configurarModoEdicion(boolean esEdicion) {
        txtIdentificacion.setEditable(!esEdicion);
        txtIdentificacion.setDisable(esEdicion);

        if (esEdicion) {
            txtNombre.requestFocus();
        }
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

    private void mostrarConfirmacion(String mensaje, Runnable accion) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);

        if (alert.showAndWait().get() == ButtonType.OK) {
            accion.run();
        }
    }
}