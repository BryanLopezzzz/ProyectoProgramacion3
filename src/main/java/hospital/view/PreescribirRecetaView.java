package hospital.view;
import hospital.controller.RecetaController;
import hospital.model.DetalleReceta;
import hospital.model.Medico;
import hospital.model.Paciente;
import hospital.model.Receta;
import hospital.model.EstadoReceta;
import hospital.view.busqueda.BuscarPacientePreescripcionView;
import hospital.view.busqueda.BuscarMedicamentoView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

public class PreescribirRecetaView {

    @FXML private TextField txtFechaConfeccion;
    @FXML private TextField txtBuscarPaciente;
    @FXML private Button btnBuscarPaciente;
    @FXML private DatePicker dtpFechaRetiro;
    @FXML private TableView<DetalleReceta> tblRecetas;
    @FXML private TableColumn<DetalleReceta, String> colNombreMedicamento;
    @FXML private TableColumn<DetalleReceta, String> colIndicaciones;
    @FXML private TableColumn<DetalleReceta, String> colPresentacion;
    @FXML private TableColumn<DetalleReceta, Integer> conCantidad;
    @FXML private TableColumn<DetalleReceta, Integer> colDuracion;
    @FXML private Button btnAgregarReceta;
    @FXML private Button btnEliminarReceta;
    @FXML private Button btnEditarReceta;
    @FXML private Button btnVolver;
    @FXML private Button btnPreescribir;

    private final RecetaController recetaController = new RecetaController();
    private ObservableList<DetalleReceta> listaDetalles = FXCollections.observableArrayList();
    private Paciente pacienteSeleccionado;
    private Medico medico; // Se inyecta desde la vista anterior
    private Receta recetaActual; // La receta que se está creando

    @FXML
    public void initialize() {
        configurarTabla();
        configurarFechaConfeccion();
        configurarFechaRetiro();

        // Listener para habilitar/deshabilitar botones
        tblRecetas.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean haySeleccion = newSelection != null;
            btnEliminarReceta.setDisable(!haySeleccion);
            btnEditarReceta.setDisable(!haySeleccion);
        });
    }

    private void configurarTabla() {
        // Configurar las columnas de la tabla
        colNombreMedicamento.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getMedicamento() != null ?
                                cellData.getValue().getMedicamento().getNombre() : ""));

        colIndicaciones.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getIndicaciones()));

        colPresentacion.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getMedicamento() != null ?
                                cellData.getValue().getMedicamento().getPresentacion() : ""));

        conCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colDuracion.setCellValueFactory(new PropertyValueFactory<>("diasTratamiento"));

        // Asignar la lista observable a la tabla
        tblRecetas.setItems(listaDetalles);

        // Deshabilitar botones inicialmente
        btnEliminarReceta.setDisable(true);
        btnEditarReceta.setDisable(true);
    }

    private void configurarFechaConfeccion() {
        // Mostrar fecha actual como fecha de confección (solo lectura)
        LocalDate hoy = LocalDate.now();
        txtFechaConfeccion.setText(hoy.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }

    private void configurarFechaRetiro() {
        // Establecer fecha de retiro por defecto (7 días después)
        dtpFechaRetiro.setValue(LocalDate.now().plusDays(7));
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    @FXML
    private void BuscarPaciente(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/hospital/view/busqueda/buscarPacientePreescripcion.fxml"));
            Parent root = loader.load();

            BuscarPacientePreescripcionView buscarView = loader.getController();

            Stage stage = new Stage();
            stage.setTitle("Buscar Paciente");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(btnBuscarPaciente.getScene().getWindow());
            stage.showAndWait();

            pacienteSeleccionado = buscarView.getPacienteSeleccionado();
            if (pacienteSeleccionado != null) {
                txtBuscarPaciente.setText(pacienteSeleccionado.getNombre() + " (" + pacienteSeleccionado.getId() + ")");
                btnPreescribir.setDisable(false);
            }

        } catch (IOException e) {
            mostrarError("Error", "No se pudo abrir la ventana de búsqueda de pacientes: " + e.getMessage());
        }
    }

    @FXML
    private void AgregarReceta(ActionEvent event) {
        if (pacienteSeleccionado == null) {
            mostrarAdvertencia("Paciente requerido", "Debe seleccionar un paciente antes de agregar medicamentos.");
            return;
        }

        try {
            // Crear receta si no existe
            if (recetaActual == null) {
                crearRecetaTemporal();
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/hospital/view/busqueda/buscarMedicamento.fxml"));
            Parent root = loader.load();

            BuscarMedicamentoView buscarMedicamentoView = loader.getController();

            Stage stage = new Stage();
            stage.setTitle("Seleccionar Medicamento");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(btnAgregarReceta.getScene().getWindow());
            stage.showAndWait();

            // Después de seleccionar medicamento, abrir vista de detalles
            // Esta funcionalidad requeriría modificaciones adicionales en BuscarMedicamentoView

        } catch (IOException e) {
            mostrarError("Error", "No se pudo abrir la ventana de selección de medicamentos: " + e.getMessage());
        }
    }

    @FXML
    private void EliminarReceta(ActionEvent event) {
        DetalleReceta detalleSeleccionado = tblRecetas.getSelectionModel().getSelectedItem();

        if (detalleSeleccionado == null) {
            mostrarAdvertencia("Selección requerida", "Debe seleccionar un detalle para eliminar.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("¿Eliminar medicamento?");
        confirmacion.setContentText("¿Está seguro que desea eliminar " +
                detalleSeleccionado.getMedicamento().getNombre() + " de la receta?");

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            listaDetalles.remove(detalleSeleccionado);

            // Si no quedan detalles, habilitar de nuevo el botón de agregar
            if (listaDetalles.isEmpty()) {
                btnPreescribir.setDisable(pacienteSeleccionado == null);
            }
        }
    }

    @FXML
    private void EditarReceta(ActionEvent event) {
        DetalleReceta detalleSeleccionado = tblRecetas.getSelectionModel().getSelectedItem();

        if (detalleSeleccionado == null) {
            mostrarAdvertencia("Selección requerida", "Debe seleccionar un detalle para editar.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/hospital/view/detalleReceta.fxml"));
            Parent root = loader.load();

            DetalleRecetaView detalleView = loader.getController();
            detalleView.setContext(medico, recetaActual.getId(),
                    detalleSeleccionado.getMedicamento().getCodigo());

            Stage stage = new Stage();
            stage.setTitle("Editar Detalle de Receta");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(btnEditarReceta.getScene().getWindow());
            stage.showAndWait();

            // Recargar los detalles de la receta
            actualizarTablaDetalles();

        } catch (IOException e) {
            mostrarError("Error", "No se pudo abrir el formulario de edición: " + e.getMessage());
        }
    }

    @FXML
    private void Preescribir(ActionEvent event) {
        if (pacienteSeleccionado == null) {
            mostrarAdvertencia("Paciente requerido", "Debe seleccionar un paciente.");
            return;
        }

        if (dtpFechaRetiro.getValue() == null) {
            mostrarAdvertencia("Fecha requerida", "Debe establecer una fecha de retiro.");
            return;
        }

        if (dtpFechaRetiro.getValue().isBefore(LocalDate.now())) {
            mostrarAdvertencia("Fecha inválida", "La fecha de retiro no puede ser anterior a hoy.");
            return;
        }

        if (listaDetalles.isEmpty()) {
            mostrarAdvertencia("Medicamentos requeridos", "Debe agregar al menos un medicamento a la receta.");
            return;
        }

        try {
            // Crear la receta final
            Receta receta = new Receta();
            receta.setId(generarIdReceta());
            receta.setPaciente(pacienteSeleccionado);
            receta.setMedico(medico);
            receta.setFecha(LocalDate.now());
            receta.setFechaRetiro(dtpFechaRetiro.getValue());
            receta.setEstado(EstadoReceta.CONFECCIONADA);

            // Agregar todos los detalles
            for (DetalleReceta detalle : listaDetalles) {
                receta.agregarDetalle(detalle);
            }

            // Guardar la receta
            recetaController.crearReceta(medico, receta);

            mostrarInformacion("Éxito", "Receta preescrita correctamente con ID: " + receta.getId());

            // Limpiar formulario
            limpiarFormulario();

        } catch (Exception e) {
            mostrarError("Error al preescribir", e.getMessage());
        }
    }

    @FXML
    private void Volver(ActionEvent event) {
        // Confirmar si hay cambios sin guardar
        if (!listaDetalles.isEmpty() || pacienteSeleccionado != null) {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar salida");
            confirmacion.setHeaderText("¿Salir sin guardar?");
            confirmacion.setContentText("Hay cambios sin guardar. ¿Está seguro que desea salir?");

            Optional<ButtonType> resultado = confirmacion.showAndWait();
            if (resultado.isEmpty() || resultado.get() != ButtonType.OK) {
                return;
            }
        }

        Stage stage = (Stage) btnVolver.getScene().getWindow();
        stage.close();
    }

    // Métodos de utilidad
    private void crearRecetaTemporal() {
        recetaActual = new Receta();
        recetaActual.setId(generarIdReceta());
        recetaActual.setPaciente(pacienteSeleccionado);
        recetaActual.setMedico(medico);
        recetaActual.setFecha(LocalDate.now());
        recetaActual.setFechaRetiro(dtpFechaRetiro.getValue());
        recetaActual.setEstado(EstadoReceta.CONFECCIONADA);
    }

    private String generarIdReceta() {
        return "REC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private void actualizarTablaDetalles() {
        if (recetaActual != null) {
            listaDetalles.setAll(recetaActual.getDetalles());
        }
    }

    private void limpiarFormulario() {
        pacienteSeleccionado = null;
        recetaActual = null;
        txtBuscarPaciente.clear();
        listaDetalles.clear();
        dtpFechaRetiro.setValue(LocalDate.now().plusDays(7));
        btnPreescribir.setDisable(true);
    }

    // Métodos para mostrar alertas
    private void mostrarError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarAdvertencia(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Advertencia");
        alert.setHeaderText(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarInformacion(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // Métodos públicos para integración con otras vistas
    public void agregarDetalleReceta(DetalleReceta detalle) {
        listaDetalles.add(detalle);
        if (recetaActual != null) {
            recetaActual.agregarDetalle(detalle);
        }
    }

    public Receta getRecetaActual() {
        return recetaActual;
    }

    public Paciente getPacienteSeleccionado() {
        return pacienteSeleccionado;
    }
}