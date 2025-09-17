package hospital.viewController;

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

public class EditarFarmaceutaViewController {

    @FXML
    private TextField txtIdentificacionFarmaceuta;

    @FXML
    private TextField txtNombreFarmaceuta;

    @FXML
    private Button btnGuardar;

    @FXML
    private Button btnVolver;

    private final FarmaceutaController farmaceutaController = new FarmaceutaController();
    private final Administrador admin = new Administrador();

    private Farmaceuta farmaceutaOriginal;

    @FXML
    public void initialize() {
        configurarValidaciones();
        configurarCampos();
    }

    private void configurarValidaciones() {
        txtNombreFarmaceuta.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*")) {
                txtNombreFarmaceuta.setText(oldValue);
            }
        });
    }

    private void configurarCampos() {
        txtIdentificacionFarmaceuta.setEditable(false);
        txtIdentificacionFarmaceuta.setStyle(txtIdentificacionFarmaceuta.getStyle() + "; -fx-background-color: #f0f0f0;");
        txtNombreFarmaceuta.requestFocus();
    }

    public void cargarFarmaceuta(Farmaceuta farmaceuta) {
        if (farmaceuta == null) {
            mostrarError("No se pudo cargar el farmaceuta para edición.");
            return;
        }

        this.farmaceutaOriginal = farmaceuta;

        txtIdentificacionFarmaceuta.setText(farmaceuta.getId());
        txtNombreFarmaceuta.setText(farmaceuta.getNombre());
    }

    @FXML
    public void Guardar(ActionEvent event) {
        if (!validarCampos()) {
            return;
        }

        String nuevoNombre = txtNombreFarmaceuta.getText().trim();
        if (farmaceutaOriginal != null && nuevoNombre.equals(farmaceutaOriginal.getNombre())) {
            mostrarInfo("No se detectaron cambios para guardar.");
            return;
        }

        mostrarConfirmacion("¿Está seguro que desea guardar los cambios?", () -> {
            try {
                Farmaceuta farmaceutaActualizado = new Farmaceuta();
                farmaceutaActualizado.setId(txtIdentificacionFarmaceuta.getText().trim());
                farmaceutaActualizado.setNombre(nuevoNombre);

                if (farmaceutaOriginal != null && farmaceutaOriginal.getClave() != null) {
                    farmaceutaActualizado.setClave(farmaceutaOriginal.getClave());
                }

                farmaceutaController.modificar(admin, farmaceutaActualizado);

                mostrarInfo("Farmaceuta actualizado exitosamente.");

                volverABusqueda();

            } catch (Exception e) {
                mostrarError("Error al actualizar farmaceuta: " + e.getMessage());
            }
        });
    }

    @FXML
    public void Volver(ActionEvent event) {
        if (hayCambiosSinGuardar()) {
            mostrarConfirmacion("Hay cambios sin guardar. ¿Está seguro que desea salir?",
                    this::volverABusqueda);
        } else {
            volverABusqueda();
        }
    }

    private boolean validarCampos() {
        StringBuilder errores = new StringBuilder();

        String id = txtIdentificacionFarmaceuta.getText().trim();
        if (id.isEmpty()) {
            errores.append("- El ID no puede estar vacío.\n");
        }

        String nombre = txtNombreFarmaceuta.getText().trim();
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

    private boolean hayCambiosSinGuardar() {
        if (farmaceutaOriginal == null) {
            return false;
        }

        String nombreActual = txtNombreFarmaceuta.getText().trim();
        return !nombreActual.equals(farmaceutaOriginal.getNombre());
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