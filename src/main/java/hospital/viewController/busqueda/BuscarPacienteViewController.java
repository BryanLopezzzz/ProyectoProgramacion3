package hospital.viewController.busqueda;

import hospital.controller.PacienteController;
import hospital.viewController.EditarPacienteViewController;
import hospital.model.Administrador;
import hospital.model.Paciente;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class BuscarPacienteViewController {

    @FXML
    private TextField txtBuscar;

    @FXML
    private ComboBox<String> btnFiltro;

    @FXML
    private TableView<Paciente> tblPacientes;

    @FXML
    private TableColumn<Paciente, String> colIdentificacionPaciente;

    @FXML
    private TableColumn<Paciente, String> colNombrePaciente;

    @FXML
    private TableColumn<Paciente, String> colTelefonoPaciente;

    @FXML
    private TableColumn<Paciente, String> colFechaNac;

    @FXML
    private Button btnAgregarPaciente;

    @FXML
    private Button btnEliminarPaciente;

    @FXML
    private Button btnEditarPaciente;

    @FXML
    private Button btnVolver;

    @FXML
    private Button btnReporte;

    @FXML
    private Button btnBuscar;

    private final PacienteController pacienteController = new PacienteController();
    private final Administrador admin = new Administrador(); // puedes pasar el admin logueado
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private ObservableList<Paciente> pacientesObs;

    @FXML
    public void initialize() {
        // Configurar columnas
        colIdentificacionPaciente.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getId()));
        colNombrePaciente.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNombre()));
        colTelefonoPaciente.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTelefono()));
        colFechaNac.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getFechaNacimiento().format(formatter)
        ));

        // Inicializar filtro
        btnFiltro.setItems(FXCollections.observableArrayList("Nombre", "ID"));
        btnFiltro.setValue("Nombre");

        cargarPacientes();
    }

    private void cargarPacientes() {
        try {
            List<Paciente> lista = pacienteController.listar(admin);
            pacientesObs = FXCollections.observableArrayList(lista);
            tblPacientes.setItems(pacientesObs);
        } catch (Exception e) {
            mostrarError("Error al cargar pacientes: " + e.getMessage());
        }
    }

    @FXML
    public void Filtrar(ActionEvent event) {
        BuscarPaciente(event);
    }

    @FXML
    public void AgregarPaciente(ActionEvent event) {
        try {
            // Usar la ventana de editar pero sin cargar datos
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/hospital/view/agregarPaciente.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = (Stage) btnAgregarPaciente.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Agregar Paciente");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarError("Error al abrir la ventana de agregar paciente: " + e.getMessage());
        }
    }

    @FXML
    public void EliminarPaciente(ActionEvent event) {
        Paciente seleccionado = tblPacientes.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarError("Debe seleccionar un paciente para eliminar.");
            return;
        }

        // Agregar confirmación
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("¿Está seguro que desea eliminar el paciente?");
        confirmacion.setContentText("Paciente: " + seleccionado.getNombre() + " (ID: " + seleccionado.getId() + ")");

        if (confirmacion.showAndWait().get() == ButtonType.OK) {
            try {
                boolean eliminado = pacienteController.eliminar(admin, seleccionado.getId());
                if (eliminado) {
                    mostrarInfo("Paciente eliminado correctamente.");
                    cargarPacientes();
                } else {
                    mostrarError("No se pudo eliminar el paciente.");
                }
            } catch (Exception e) {
                mostrarError("Error al eliminar: " + e.getMessage());
            }
        }
    }

    @FXML
    public void EditarPaciente(ActionEvent event) {
        Paciente seleccionado = tblPacientes.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarError("Debe seleccionar un paciente para editar.");
            return;
        }

        try {
            // Cargar la vista de edición
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/hospital/view/editarPaciente.fxml"));
            Parent root = fxmlLoader.load();

            // Obtener el controller de la vista de edición y pasarle el paciente
            EditarPacienteViewController editarController = fxmlLoader.getController();
            editarController.cargarPaciente(seleccionado);

            // Cambiar la escena
            Stage stage = (Stage) btnEditarPaciente.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Editar Paciente");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarError("Error al abrir la ventana de edición: " + e.getMessage());
        }
    }

    @FXML
    public void GenerarReporte(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Reporte de Pacientes");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Archivos XML", "*.xml")
        );
        fileChooser.setInitialFileName("reporte_pacientes.xml");

        Stage stage = (Stage) btnReporte.getScene().getWindow();
        File archivo = fileChooser.showSaveDialog(stage);

        if (archivo != null) {
            try {
                pacienteController.generarReporte(admin, archivo.getAbsolutePath());
                mostrarInfo("Reporte generado exitosamente en: " + archivo.getAbsolutePath());
            } catch (Exception e) {
                mostrarError("Error al generar reporte: " + e.getMessage());
            }
        }
    }

    @FXML
    public void BuscarPaciente(ActionEvent event) {
        String texto = txtBuscar.getText().toLowerCase();
        String filtro = btnFiltro.getValue();

        if (texto.isEmpty()) {
            tblPacientes.setItems(pacientesObs); // muestra todo si no hay filtro
            return;
        }

        List<Paciente> filtrados;
        if ("Nombre".equals(filtro)) {
            filtrados = pacientesObs.stream()
                    .filter(p -> p.getNombre().toLowerCase().contains(texto))
                    .collect(Collectors.toList());
        } else { // ID
            filtrados = pacientesObs.stream()
                    .filter(p -> p.getId().toLowerCase().contains(texto))
                    .collect(Collectors.toList());
        }

        tblPacientes.setItems(FXCollections.observableArrayList(filtrados));
    }

    @FXML
    private void Volver(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/hospital/view/dashboard.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) btnVolver.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Dashboard");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarError("Error al cargar el dashboard.");
        }
    }

    // Utilitarios
    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR, mensaje);
        alert.showAndWait();
    }

    private void mostrarInfo(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, mensaje);
        alert.showAndWait();
    }
}
