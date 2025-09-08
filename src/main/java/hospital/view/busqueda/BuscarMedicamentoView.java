package hospital.view.busqueda;

import hospital.controller.MedicamentoController;
import hospital.model.Administrador;
import hospital.model.Medicamento;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;

public class BuscarMedicamentoView {

    @FXML
    private TextField txtBuscar;

    @FXML
    private ComboBox<String> btnFiltro;

    @FXML
    private TableView<Medicamento> tblMedicamento;

    @FXML
    private Button btnVolver;

    @FXML
    private TableColumn<Medicamento, String> colCodigo;

    @FXML
    private TableColumn<Medicamento, String> colNombreMedicamento;

    @FXML
    private TableColumn<Medicamento, String> colPresentacion;

    private final MedicamentoController controller = new MedicamentoController();
    private Administrador admin;  // se inyecta desde fuera (ej: login)

    // Método para inyectar el admin desde otra vista/controlador
    public void setAdministrador(Administrador admin) {
        this.admin = admin;
    }

    @FXML
    public void initialize() {
        // Inicializar columnas de la tabla
        colCodigo.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCodigo()));
        colNombreMedicamento.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNombre()));
        colPresentacion.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPresentacion()));

        // Inicializar opciones del filtro
        btnFiltro.getItems().addAll("Código", "Nombre");
        btnFiltro.getSelectionModel().select("Código");

        // Cargar medicamentos iniciales
        cargarMedicamentos();
    }

    private void cargarMedicamentos() {
        try {
            if (admin == null) {
                mostrarError("Debe iniciar sesión como administrador.");
                return;
            }
            List<Medicamento> lista = controller.listar(admin);
            ObservableList<Medicamento> data = FXCollections.observableArrayList(lista);
            tblMedicamento.setItems(data);
        } catch (Exception e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    private void Filtrar() {
        String criterio = txtBuscar.getText().trim();
        String filtro = btnFiltro.getValue();

        try {
            if (admin == null) {
                mostrarError("Debe iniciar sesión como administrador.");
                return;
            }

            if (criterio.isEmpty()) {
                cargarMedicamentos();
                return;
            }

            if ("Código".equals(filtro)) {
                Medicamento m = controller.buscarPorCodigo(admin, criterio);
                if (m != null) {
                    tblMedicamento.setItems(FXCollections.observableArrayList(m));
                } else {
                    tblMedicamento.setItems(FXCollections.emptyObservableList());
                }
            } else if ("Nombre".equals(filtro)) {
                List<Medicamento> lista = controller.buscarPorNombre(admin, criterio);
                tblMedicamento.setItems(FXCollections.observableArrayList(lista));
            }
        } catch (Exception e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    private void Seleccionar() {
        Medicamento seleccionado = tblMedicamento.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            System.out.println("Medicamento seleccionado: " + seleccionado.getNombre());
            // Aquí podrías cerrar ventana o enviar el medicamento a otra vista/controlador
        } else {
            mostrarError("Debe seleccionar un medicamento de la tabla.");
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
