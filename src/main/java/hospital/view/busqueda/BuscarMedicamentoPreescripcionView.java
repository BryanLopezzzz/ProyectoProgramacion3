package hospital.view.busqueda;

import hospital.controller.MedicamentoController;
import hospital.model.Administrador;
import hospital.model.Medicamento;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class BuscarMedicamentoPreescripcionView implements Initializable {

    @FXML private TextField txtBuscar;
    @FXML private ComboBox<String> btnFiltro;
    @FXML private TableView<Medicamento> tblMedicamento;
    @FXML private TableColumn<Medicamento, String> colCodigo;
    @FXML private TableColumn<Medicamento, String> colNombreMedicamento;
    @FXML private TableColumn<Medicamento, String> colPresentacion;
    @FXML private Button btnVolver;
    @FXML private Button btnSeleccionar;

    private final MedicamentoController medicamentoController;
    private final Administrador administrador;
    private ObservableList<Medicamento> medicamentos;
    private Medicamento medicamentoSeleccionado;

    public BuscarMedicamentoPreescripcionView() {
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
        colNombreMedicamento.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colPresentacion.setCellValueFactory(new PropertyValueFactory<>("presentacion"));

        medicamentos = FXCollections.observableArrayList();
        tblMedicamento.setItems(medicamentos);
    }

    private void configurarFiltro() {
        btnFiltro.getItems().addAll("Código", "Nombre");
        btnFiltro.setValue("Código");
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
    private void Filtrar() {
        Buscar();
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

            if ("Código".equals(filtro)) {
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
    private void Seleccionar() {
        medicamentoSeleccionado = tblMedicamento.getSelectionModel().getSelectedItem();
        if (medicamentoSeleccionado == null) {
            mostrarError("Debe seleccionar un medicamento.");
            return;
        }
        Stage stage = (Stage) tblMedicamento.getScene().getWindow();
        stage.close();
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
            mostrarError("Error al volver: " + e.getMessage());
        }
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public Medicamento getMedicamentoSeleccionado() {
        return medicamentoSeleccionado;
    }
}

