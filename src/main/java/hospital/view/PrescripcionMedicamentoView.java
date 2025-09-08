package hospital.view;

import hospital.controller.RecetaController;
import hospital.model.Medico;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PrescripcionMedicamentoView {

    @FXML
    private TextArea txtIndicaciones;

    @FXML
    private TextField txtCantidad;

    @FXML
    private TextField txtDuracion;

    @FXML
    private Button btnAgregar;

    @FXML
    private Button btnSalir;

    private final RecetaController recetaController;
    private Medico medicoActual;
    private String recetaId;
    private String medicamentoId;

    public PrescripcionMedicamentoView() {
        this.recetaController = new RecetaController();
    }

    @FXML
    private void initialize() {
        // Configurar validaciones en los campos numéricos
        configurarValidacionNumerica(txtCantidad);
        configurarValidacionNumerica(txtDuracion);

        // Configurar el botón para que esté habilitado solo cuando los campos estén llenos
        configurarValidacionFormulario();
    }

    /**
     * Configura las propiedades iniciales necesarias para el funcionamiento de la vista
     */
    public void configurarVista(Medico medico, String recetaId, String medicamentoId) {
        this.medicoActual = medico;
        this.recetaId = recetaId;
        this.medicamentoId = medicamentoId;
    }

    /**
     * Maneja el evento del botón "Agregar"
     */
    @FXML
    private void Agregar(ActionEvent event) {
        try {
            // Validar que todos los campos estén llenos
            if (!validarCampos()) {
                return;
            }

            // Obtener valores de los campos
            String indicaciones = txtIndicaciones.getText().trim();
            int cantidad = Integer.parseInt(txtCantidad.getText().trim());
            int diasTratamiento = Integer.parseInt(txtDuracion.getText().trim());

            // Validar que los valores sean positivos
            if (cantidad <= 0) {
                mostrarAlerta("Error de Validación", "La cantidad debe ser mayor que 0.");
                return;
            }

            if (diasTratamiento <= 0) {
                mostrarAlerta("Error de Validación", "Los días de tratamiento deben ser mayores que 0.");
                return;
            }

            // Agregar el detalle a la receta
            recetaController.agregarDetalle(
                    medicoActual,
                    recetaId,
                    medicamentoId,
                    cantidad,
                    indicaciones,
                    diasTratamiento
            );

            // Mostrar mensaje de éxito
            mostrarAlerta("Éxito", "El medicamento ha sido agregado correctamente a la receta.");

            // Limpiar formulario
            limpiarCampos();

            // Cerrar la ventana
            cerrarVentana();

        } catch (NumberFormatException e) {
            mostrarAlerta("Error de Formato", "La cantidad y duración deben ser números enteros válidos.");
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al agregar el medicamento: " + e.getMessage());
        }
    }

    /**
     * Maneja el evento del botón "Volver" (aunque no está definido en el FXML con fx:id)
     */
    @FXML
    private void volver(ActionEvent event) {
        cerrarVentana();
    }

    private boolean validarCampos() {
        if (txtIndicaciones.getText() == null || txtIndicaciones.getText().trim().isEmpty()) {
            mostrarAlerta("Campo Requerido", "Las indicaciones son obligatorias.");
            txtIndicaciones.requestFocus();
            return false;
        }

        if (txtCantidad.getText() == null || txtCantidad.getText().trim().isEmpty()) {
            mostrarAlerta("Campo Requerido", "La cantidad es obligatoria.");
            txtCantidad.requestFocus();
            return false;
        }

        if (txtDuracion.getText() == null || txtDuracion.getText().trim().isEmpty()) {
            mostrarAlerta("Campo Requerido", "La duración del tratamiento es obligatoria.");
            txtDuracion.requestFocus();
            return false;
        }

        return true;
    }


    private void configurarValidacionNumerica(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    private void configurarValidacionFormulario() {
        // Listener para habilitar/deshabilitar el botón según el estado de los campos
        Runnable validarFormulario = () -> {
            boolean formularioValido =
                    txtIndicaciones.getText() != null && !txtIndicaciones.getText().trim().isEmpty() &&
                            txtCantidad.getText() != null && !txtCantidad.getText().trim().isEmpty() &&
                            txtDuracion.getText() != null && !txtDuracion.getText().trim().isEmpty();

            btnAgregar.setDisable(!formularioValido);
        };

        txtIndicaciones.textProperty().addListener((obs, oldText, newText) -> validarFormulario.run());
        txtCantidad.textProperty().addListener((obs, oldText, newText) -> validarFormulario.run());
        txtDuracion.textProperty().addListener((obs, oldText, newText) -> validarFormulario.run());

        // Ejecutar validación inicial
        validarFormulario.run();
    }

    private void limpiarCampos() {
        txtIndicaciones.clear();
        txtCantidad.clear();
        txtDuracion.clear();
    }


    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }


    private void cerrarVentana() {
        Stage stage = (Stage) btnSalir.getScene().getWindow();
        stage.close();
    }
}