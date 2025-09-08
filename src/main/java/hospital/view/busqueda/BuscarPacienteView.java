package hospital.view.busqueda;

import hospital.controller.PacienteController;
import hospital.model.Administrador;
import hospital.model.Paciente;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class BuscarPacienteView {

    @FXML private TextField txtBuscar;
    @FXML private ComboBox<String> btnFiltro;
    @FXML private TableView<Paciente> tableMedicos;
    @FXML private TableColumn<Paciente, String> colIdentificacion;
    @FXML private TableColumn<Paciente, String> colNombre;
    @FXML private TableColumn<Paciente, String> colTelefono;
    @FXML private TableColumn<Paciente, String> colFechaNacimiento;
    @FXML private Button btnVolver;
    @FXML private Button btnSeleccionar;

    private ObservableList<Paciente> listaPacientes = FXCollections.observableArrayList();
    private Paciente pacienteSeleccionado;
    private final PacienteController pacienteController = new PacienteController();
    private Administrador admin;

    @FXML
    public void initialize() {
        // Configurar columnas
        colIdentificacion.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getId()));
        colNombre.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNombre()));
        colTelefono.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getTelefono()));
        colFechaNacimiento.setCellValueFactory(c -> {
            if (c.getValue().getFechaNacimiento() != null) {
                return new javafx.beans.property.SimpleStringProperty(
                        c.getValue().getFechaNacimiento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                );
            } else {
                return new javafx.beans.property.SimpleStringProperty("");
            }
        });

        // Opciones de filtro
        btnFiltro.setItems(FXCollections.observableArrayList("ID", "Nombre"));
        btnFiltro.setValue("Nombre");
    }

    public void setAdmin(Administrador admin) {
        this.admin = admin;
        cargarPacientes();
    }

    private void cargarPacientes() {
        try {
            List<Paciente> pacientes = pacienteController.listar(admin);
            listaPacientes.setAll(pacientes);
            tableMedicos.setItems(listaPacientes);
        } catch (Exception e) {
            mostrarError("Error al cargar pacientes", e.getMessage());
        }
    }

    @FXML
    private void Filtrar(ActionEvent event) {
        String criterio = btnFiltro.getValue();
        String texto = txtBuscar.getText().toLowerCase();

        ObservableList<Paciente> filtrados = FXCollections.observableArrayList();

        for (Paciente p : listaPacientes) {
            if (criterio.equals("Nombre") && p.getNombre().toLowerCase().contains(texto)) {
                filtrados.add(p);
            } else if (criterio.equals("ID") && p.getId().contains(texto)) {
                filtrados.add(p);
            }
        }

        tableMedicos.setItems(filtrados);
    }

    @FXML
    private void Volver(ActionEvent event) {
        Stage stage = (Stage) btnVolver.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void Seleccionar(ActionEvent event) {
        pacienteSeleccionado = tableMedicos.getSelectionModel().getSelectedItem();
        if (pacienteSeleccionado == null) {
            mostrarError("Selección inválida", "Debe seleccionar un paciente.");
            return;
        }
        ((Stage) btnSeleccionar.getScene().getWindow()).close();
    }

    public Paciente getPacienteSeleccionado() {
        return pacienteSeleccionado;
    }

    private void mostrarError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
