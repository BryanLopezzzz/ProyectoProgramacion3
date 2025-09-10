package hospital.view.registro;

import hospital.controller.PacienteController;
import hospital.model.Administrador;
import hospital.model.Paciente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;

public class RegistroPacienteView {

    // Corregir los nombres de las variables para que coincidan con los IDs del FXML
    @FXML private TextField txtIdentificacion;
    @FXML private TextField txtNombre;
    @FXML private TextField txtTelefono; // El FXML no tiene fx:id para este campo, necesita agregarse
    @FXML private DatePicker dtpFechaNac;

    @FXML private Button btnGuardar; // El FXML no tiene fx:id, necesita agregarse
    @FXML private Button btnVolver;

    private final PacienteController pacienteController = new PacienteController();
    private final Administrador admin = new Administrador(); // Puedes pasar el admin logueado

    @FXML
    public void initialize() {
    }

    @FXML
    public void Guardar(ActionEvent event) {
        try {
            // Validar campos
            if (!validarCampos()) {
                return;
            }

            // Crear nuevo paciente
            Paciente nuevoPaciente = new Paciente();
            nuevoPaciente.setId(txtIdentificacion.getText().trim());
            nuevoPaciente.setNombre(txtNombre.getText().trim());
            nuevoPaciente.setTelefono(txtTelefono.getText().trim());
            nuevoPaciente.setFechaNacimiento(dtpFechaNac.getValue());

            // Verificar que el ID no exista ya
            if (pacienteController.buscarPorId(admin, nuevoPaciente.getId()) != null) {
                mostrarError("Ya existe un paciente con el ID: " + nuevoPaciente.getId());
                return;
            }

            // Guardar paciente
            pacienteController.agregar(admin, nuevoPaciente);

            mostrarInfo("Paciente registrado correctamente.");

            // Limpiar formulario después de guardar
            limpiarCampos();

        } catch (Exception e) {
            mostrarError("Error al registrar paciente: " + e.getMessage());
        }
    }

    @FXML
    public void Volver(ActionEvent event) {
        volverABusqueda();
    }

    private boolean validarCampos() {
        StringBuilder errores = new StringBuilder();

        // Validar ID
        if (txtIdentificacion.getText().trim().isEmpty()) {
            errores.append("- El ID es obligatorio.\n");
        }

        // Validar nombre
        if (txtNombre.getText().trim().isEmpty()) {
            errores.append("- El nombre es obligatorio.\n");
        }

        // Validar teléfono
        if (txtTelefono.getText().trim().isEmpty()) {
            errores.append("- El teléfono es obligatorio.\n");
        }

        // Validar fecha de nacimiento
        if (dtpFechaNac.getValue() == null) {
            errores.append("- La fecha de nacimiento es obligatoria.\n");
        } else if (dtpFechaNac.getValue().isAfter(LocalDate.now())) {
            errores.append("- La fecha de nacimiento no puede ser futura.\n");
        }

        // Validaciones adicionales
        String id = txtIdentificacion.getText().trim();
        if (!id.isEmpty() && !esIdValido(id)) {
            errores.append("- El ID debe contener solo números y letras.\n");
        }

        String telefono = txtTelefono.getText().trim();
        if (!telefono.isEmpty() && !esTelefonoValido(telefono)) {
            errores.append("- El formato del teléfono no es válido.\n");
        }

        if (errores.length() > 0) {
            mostrarError("Por favor corrija los siguientes errores:\n\n" + errores.toString());
            return false;
        }

        return true;
    }

    private boolean esIdValido(String id) {
        // Permitir solo letras, números y guiones
        return id.matches("^[a-zA-Z0-9-]+$") && id.length() >= 3;
    }

    private boolean esTelefonoValido(String telefono) {
        // Permitir números, espacios, guiones y paréntesis
        return telefono.matches("^[0-9\\s\\-\\(\\)\\+]+$") && telefono.length() >= 8;
    }

    private void limpiarCampos() {
        txtIdentificacion.clear();
        txtNombre.clear();
        txtTelefono.clear();
        dtpFechaNac.setValue(null);

        // Poner foco en el primer campo
        txtIdentificacion.requestFocus();
    }

    private void volverABusqueda() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/hospital/view/busqueda/buscarPaciente.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) btnVolver.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Buscar Pacientes");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarError("Error al volver a la vista de búsqueda.");
        }
    }

    // Métodos utilitarios para mostrar alertas
    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Ha ocurrido un error");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarInfo(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText("Operación exitosa");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

}