package hospital.viewController;

import hospital.controller.RecetaController;
import hospital.viewController.busqueda.BuscarPacientePreescripcionView;
import javafx.collections.FXCollections;
import hospital.model.*;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import hospital.model.Receta;

import java.io.IOException;
import java.util.List;


public class DespachoViewController {
    @FXML
    private TextField txtBuscar;

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
    private Button btnBuscar;

    private Paciente pacienteSeleccionado;

    private final RecetaController recetaController = new RecetaController();
    private final ObservableList<Receta> recetasObservable = FXCollections.observableArrayList();

    public void initialize() {
        colIdentificacionReceta.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getId()
        ));

        colNombreMedicamento.setCellValueFactory(data -> {
            if (!data.getValue().getDetalles().isEmpty()) {
                return new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getDetalles().get(0).getMedicamento().getNombre()
                );
            }
            return new javafx.beans.property.SimpleStringProperty("-");
        });

        colPresentacion.setCellValueFactory(data -> {
            if (!data.getValue().getDetalles().isEmpty()) {
                return new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getDetalles().get(0).getMedicamento().getPresentacion()
                );
            }
            return new javafx.beans.property.SimpleStringProperty("-");
        });

        colFechaConfeccion.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getFecha() != null ? data.getValue().getFecha().toString() : "-"
        ));

        colEstado.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getEstado().name()
        ));

        tblRecetas.setItems(recetasObservable);
        cargarRecetas();
    }

    private void cargarRecetas() {
        try {
            List<Receta> recetas = recetaController.listarRecetas();
            recetasObservable.setAll(recetas);
        } catch (Exception e) {
            Alerta.error("Error", "Error cargando recetas: " + e.getMessage());
        }
    }

    @FXML
    private void BuscarPaciente(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/hospital/viewController/buscarPacientePrescripcion.fxml"));
            Parent root = loader.load();

            BuscarPacientePreescripcionView buscarView = loader.getController();

            Stage stage = new Stage();
            stage.setTitle("Buscar Paciente");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(btnBuscar.getScene().getWindow());


            stage.showAndWait();

            Paciente pacienteNuevo = buscarView.getPacienteSeleccionado();
            if (pacienteNuevo != null) {
                pacienteSeleccionado = pacienteNuevo;

                txtBuscar.setText(pacienteSeleccionado.getNombre() + " (" + pacienteSeleccionado.getId() + ")");

                filtrarRecetasPorPaciente();

                Alerta.info("Paciente seleccionado",
                        "Mostrando recetas para: " + pacienteSeleccionado.getNombre());
            }

        } catch (IOException e) {
            Alerta.error("Error", "No se pudo abrir la ventana de búsqueda de pacientes: " + e.getMessage());
        }
    }

    private void filtrarRecetasPorPaciente() {
        if (pacienteSeleccionado != null) {
            try {
                List<Receta> recetasPaciente = recetaController.listarRecetasPorPaciente(pacienteSeleccionado.getId());
                recetasObservable.setAll(recetasPaciente);
            } catch (Exception e) {
                Alerta.error("Error", "Error filtrando recetas del paciente: " + e.getMessage());
            }
        }
    }
    @FXML
    public void VerDetalle(ActionEvent event) {
        Receta seleccionada = tblRecetas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            Alerta.info("Detalle", "Debe seleccionar una receta de la lista.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/hospital/viewController/verRecetaDespacho.fxml"));
            Parent root = loader.load();
            VerRecetaDespachoViewController controller = loader.getController();
            controller.setReceta(seleccionada);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Detalle de Receta");
            stage.show();
        } catch (Exception e) {
            Alerta.error("Error", "No se pudo cargar el detalle de la receta.");
        }
    }

    @FXML
    private void CambiarEstado(ActionEvent event) {
        Receta seleccionada = tblRecetas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            Alerta.info("Error", "Seleccione una receta primero.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/hospital/viewController/editarEstadoReceta.fxml"));
            Parent root = loader.load();
            EditarDetalleRecetaViewController controller = loader.getController();
            controller.setReceta(seleccionada);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Cambiar Estado");
            stage.showAndWait();

            tblRecetas.refresh();

        } catch (Exception e) {
            Alerta.error("Error", "No se pudo abrir la ventana.");
        }
    }

    private EstadoReceta siguienteEstado(EstadoReceta actual) {
        return switch (actual) {
            case CONFECCIONADA -> EstadoReceta.EN_PROCESO;
            case EN_PROCESO -> EstadoReceta.LISTA;
            case LISTA -> EstadoReceta.ENTREGADA;
            default -> actual;
        };
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
            Alerta.error("Error", "Error al cargar el dashboard.");
        }
    }
}

