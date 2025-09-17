package hospital.viewController.busqueda;

import hospital.controller.FarmaceutaController;
import hospital.model.Administrador;
import hospital.model.Farmaceuta;
import hospital.viewController.EditarFarmaceutaViewController;
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
import java.util.List;

public class BuscarFarmaceutaView {

    @FXML
    private TextField txtBuscar;

    @FXML
    private ComboBox<String> btnFiltrar;

    @FXML
    private TableView<Farmaceuta> tblFarmaceutas;

    @FXML
    private TableColumn<Farmaceuta, String> colIdentificacion;

    @FXML
    private TableColumn<Farmaceuta, String> colNombre;

    @FXML
    private Button btnAgregarFarmaceuta;

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnEditar;

    @FXML
    private Button btnVolver;

    @FXML
    private Button btnReporte;

    @FXML
    private Button btnBuscar;

    private final FarmaceutaController farmaceutaController = new FarmaceutaController();
    private final Administrador admin = new Administrador(); // puedes pasar el admin logueado

    private ObservableList<Farmaceuta> farmaceutasObs;

    @FXML
    public void initialize() {
        // Configurar columnas
        colIdentificacion.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getId()));
        colNombre.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNombre()));

        // Inicializar filtro
        btnFiltrar.setItems(FXCollections.observableArrayList("ID", "Nombre"));
        btnFiltrar.setValue("ID");

        cargarFarmaceutas();
    }

    private void cargarFarmaceutas() {
        try {
            List<Farmaceuta> lista = farmaceutaController.listar(admin);
            farmaceutasObs = FXCollections.observableArrayList(lista);
            tblFarmaceutas.setItems(farmaceutasObs);
        } catch (Exception e) {
            mostrarError("Error al cargar farmaceutas: " + e.getMessage());
        }
    }

    @FXML
    public void Filtrar(ActionEvent event) {
        Buscar(event);
    }

    @FXML
    public void AgregarFarmaceuta(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/hospital/viewController/agregarFarmaceuta.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = (Stage) btnAgregarFarmaceuta.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Agregar Farmaceuta");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarError("Error al abrir la ventana de agregar farmaceuta: " + e.getMessage());
        }
    }

    @FXML
    public void EliminarFarmaceuta(ActionEvent event) {
        Farmaceuta seleccionado = tblFarmaceutas.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarError("Debe seleccionar un farmaceuta para eliminar.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("¿Está seguro que desea eliminar el farmaceuta?");
        confirmacion.setContentText("Farmaceuta: " + seleccionado.getNombre() + " (ID: " + seleccionado.getId() + ")");

        if (confirmacion.showAndWait().get() == ButtonType.OK) {
            try {
                farmaceutaController.borrar(admin, seleccionado.getId());
                mostrarInfo("Farmaceuta eliminado correctamente.");
                cargarFarmaceutas();
            } catch (Exception e) {
                mostrarError("Error al eliminar farmaceuta: " + e.getMessage());
            }
        }
    }

    @FXML
    public void EditarFarmaceuta(ActionEvent event) {
        Farmaceuta seleccionado = tblFarmaceutas.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarError("Debe seleccionar un farmaceuta para editar.");
            return;
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/hospital/viewController/editarFarmaceuta.fxml"));
            Parent root = fxmlLoader.load();

            EditarFarmaceutaViewController editarController = fxmlLoader.getController();
            editarController.cargarFarmaceuta(seleccionado);

            Stage stage = (Stage) btnEditar.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Editar Farmaceuta");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarError("Error al abrir la ventana de edición: " + e.getMessage());
        }
    }

    @FXML
    public void GenerarReporte(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Reporte de Farmaceutas");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Archivos XML", "*.xml")
        );
        fileChooser.setInitialFileName("reporte_farmaceutas.xml");

        Stage stage = (Stage) btnReporte.getScene().getWindow();
        File archivo = fileChooser.showSaveDialog(stage);

        if (archivo != null) {
            try {
                farmaceutaController.generarReporte(admin, archivo.getAbsolutePath());
            } catch (Exception e) {
                mostrarError("Error al generar reporte: " + e.getMessage());
            }
        }
    }

    @FXML
    public void Buscar(ActionEvent event) {
        String criterio = txtBuscar.getText().trim();
        String filtro = btnFiltrar.getValue();

        if (criterio.isEmpty()) {
            cargarFarmaceutas();
            return;
        }

        try {
            List<Farmaceuta> resultados;
            if ("Nombre".equalsIgnoreCase(filtro)) {
                resultados = farmaceutaController.buscarPorNombre(admin, criterio);
            } else { // ID
                Farmaceuta f = farmaceutaController.buscarPorId(admin, criterio);
                resultados = (f != null) ? List.of(f) : List.of();
            }

            farmaceutasObs = FXCollections.observableArrayList(resultados);
            tblFarmaceutas.setItems(farmaceutasObs);

            if (resultados.isEmpty()) {
                mostrarInfo("No se encontraron farmaceutas con el criterio especificado.");
            }

        } catch (Exception e) {
            mostrarError("Error en búsqueda: " + e.getMessage());
        }
    }

    @FXML
    private void Volver(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/hospital/viewController/dashboard.fxml"));
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
}