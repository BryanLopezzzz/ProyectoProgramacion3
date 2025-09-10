package hospital.view.busqueda;

import hospital.controller.MedicamentoController;
import hospital.model.Administrador;
import hospital.model.Medicamento;
import hospital.view.EditarMedicamentoView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class BuscarMedicamentoView implements Initializable {

    @FXML private TextField txtBuscar;
    @FXML private ComboBox<String> btnFiltro;
    @FXML private TableView<Medicamento> tblMedicos;
    @FXML private TableColumn<Medicamento, String> colCodigo;
    @FXML private TableColumn<Medicamento, String> colNombre;
    @FXML private TableColumn<Medicamento, String> colPresentacion;
    @FXML private Button btnBuscar;
    @FXML private Button btnAgregarMedicamento;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditarMedicamento;
    @FXML private Button btnVolver;
    @FXML private Button btnReporte;

    private final MedicamentoController medicamentoController;
    private final Administrador administrador;
    private ObservableList<Medicamento> medicamentos;

    public BuscarMedicamentoView() {
        this.medicamentoController = new MedicamentoController();
        this.administrador = new Administrador();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configurarTabla();
        configurarFiltro();
        cargarMedicamentos();
    }

    private void configurarTabla() {
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colPresentacion.setCellValueFactory(new PropertyValueFactory<>("presentacion"));

        medicamentos = FXCollections.observableArrayList();
        tblMedicos.setItems(medicamentos);
    }

    private void configurarFiltro() {
        btnFiltro.getItems().addAll("ID", "Nombre");
        btnFiltro.setValue("ID");
    }

    private void cargarMedicamentos() {
        try {
            List<Medicamento> lista = medicamentoController.listar(administrador);
            medicamentos.clear();
            medicamentos.addAll(lista);
        } catch (Exception e) {
            mostrarError("Error al cargar medicamentos: " + e.getMessage());
        }
    }

    @FXML
    private void Buscar() {
        String textoBusqueda = txtBuscar.getText();
        if (textoBusqueda == null || textoBusqueda.trim().isEmpty()) {
            cargarMedicamentos();
            return;
        }

        try {
            String filtro = btnFiltro.getValue();
            List<Medicamento> resultados;

            if ("ID".equals(filtro)) {
                Medicamento medicamento = medicamentoController.buscarPorCodigo(administrador, textoBusqueda.trim());
                if (medicamento != null) {
                    resultados = List.of(medicamento);
                } else {
                    resultados = List.of();
                }
            } else {
                resultados = medicamentoController.buscarPorNombre(administrador, textoBusqueda.trim());
            }

            medicamentos.clear();
            medicamentos.addAll(resultados);

        } catch (Exception e) {
            mostrarError("Error al buscar medicamentos: " + e.getMessage());
        }
    }

    @FXML
    private void AgregarMedicamento() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/hospital/view/AgregarMedicamento.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Agregar Medicamento");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            cargarMedicamentos();
        } catch (Exception e) {
            mostrarError("Error al abrir ventana agregar medicamento: " + e.getMessage());
        }
    }

    @FXML
    private void EliminarMedicamento() {
        Medicamento seleccionado = tblMedicos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarError("Debe seleccionar un medicamento para eliminar.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText("¿Está seguro de eliminar el medicamento: " + seleccionado.getNombre() + "?");

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try {
                medicamentoController.borrar(administrador, seleccionado.getCodigo());
                cargarMedicamentos();
            } catch (Exception e) {
                mostrarError("Error al eliminar medicamento: " + e.getMessage());
            }
        }
    }

    @FXML
    private void EditarMedicamento() {
        Medicamento seleccionado = tblMedicos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarError("Debe seleccionar un medicamento para editar.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/hospital/view/EditarMedicamentoView.fxml"));
            Parent root = loader.load();

            EditarMedicamentoView editarController = loader.getController();
            editarController.setMedicamento(seleccionado);

            Stage stage = new Stage();
            stage.setTitle("Editar Medicamento");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            cargarMedicamentos();
        } catch (Exception e) {
            mostrarError("Error al abrir ventana editar medicamento: " + e.getMessage());
        }
    }

    @FXML
    private void Volver() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/hospital/view/dashboard.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) btnVolver.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Dashboard");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error al cargar el dashboard.");
            alert.showAndWait();
        }
    }

    @FXML
    private void GenerarReporte() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Reporte de Medicamentos");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Archivos XML", "*.xml")
        );
        fileChooser.setInitialFileName("reporte_medicamentos.xml");

        Stage stage = (Stage) btnReporte.getScene().getWindow();
        File archivo = fileChooser.showSaveDialog(stage);

        if (archivo != null) {
            try {
                medicamentoController.generarReporte(administrador, archivo.getAbsolutePath());
            } catch (Exception e) {
                mostrarError("Error al generar reporte: " + e.getMessage());
            }
        }
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
