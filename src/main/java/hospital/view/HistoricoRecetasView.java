package hospital.view;

import hospital.controller.HistoricoRecetasController;
import hospital.logica.Sesion;
import hospital.model.EstadoReceta;
import hospital.model.Receta;
import hospital.model.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.util.List;

public class HistoricoRecetasView {

    @FXML
    private TableView<Receta> tblRecetas;
    @FXML
    private TableColumn<Receta, String> colIdentificacionReceta;
    @FXML
    private TableColumn<Receta, String> colNombreMedicamento;
    @FXML
    private TableColumn<Receta, String> colPresentacion;
    @FXML
    private TableColumn<Receta, String> colFechaConfeccion;
    @FXML
    private TableColumn<Receta, String> colEstado;
    @FXML
    private Button btnVerDetalle;
    @FXML
    private Button btnVolver;
    @FXML
    private TextField txtBuscar;
    @FXML
    private ComboBox<String> cmbFiltrar;

    private HistoricoRecetasController controller = new HistoricoRecetasController();
    private ObservableList<Receta> recetasObservable;

    @FXML
    public void initialize() {
        colIdentificacionReceta.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombreMedicamento.setCellValueFactory(new PropertyValueFactory<>("primerMedicamento"));
        colPresentacion.setCellValueFactory(new PropertyValueFactory<>("presentacionPrimerMedicamento"));
        colFechaConfeccion.setCellValueFactory(new PropertyValueFactory<>("fechaConfeccion"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        cmbFiltrar.setItems(FXCollections.observableArrayList("Paciente", "Médico", "Estado"));
        Usuario usuarioActual = Sesion.getUsuario();
        cargarRecetas(controller.listarRecetas(usuarioActual));
    }

    @FXML
    private void VerDetalle() {
        Receta seleccionada = tblRecetas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            Alerta.info("Info","Debe seleccionar una receta para ver el detalle.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/hospital/view/verRecetaDespacho.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Detalle de la Receta");

            VerRecetaDespachoView detalleController = loader.getController();
            detalleController.setReceta(seleccionada);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alerta.error("Error","Error al abrir detalle de receta: " + e.getMessage());
        }
    }

    private void cargarRecetas(List<Receta> recetas) {
        recetasObservable = FXCollections.observableArrayList(recetas);
        tblRecetas.setItems(recetasObservable);
    }

    @FXML
    private void Volver(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/hospital/view/dashboard.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) btnVolver.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Dashboard");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void Buscar() {
        String criterio = txtBuscar.getText().trim();
        String filtro = cmbFiltrar.getValue();

        Usuario usuario = Sesion.getUsuario();

        if (criterio.isEmpty() || filtro == null) {
            cargarRecetas(controller.listarRecetas(usuario));
            return;
        }

        List<Receta> resultados;
        switch (filtro) {
            case "Paciente":
                resultados = controller.buscarPorPaciente(usuario, criterio);
                break;
            case "Médico":
                resultados = controller.buscarPorMedico(usuario, criterio);
                break;
            case "Estado":
                try {
                    EstadoReceta estado = EstadoReceta.valueOf(criterio.toUpperCase());
                    resultados = controller.buscarPorEstado(usuario, estado);
                } catch (IllegalArgumentException ex) {
                    Alerta.error("Error","Estado inválido. Valores posibles: CONFECCIONADA, PROCESO, LISTA, ENTREGADA");
                    return;
                }
                break;
            default:
                resultados = controller.listarRecetas(usuario);
        }
        cargarRecetas(resultados);
    }

}
