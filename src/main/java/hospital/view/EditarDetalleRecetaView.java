package hospital.view;

import hospital.controller.RecetaController;
import hospital.logica.Sesion;
import hospital.model.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

public class EditarDetalleRecetaView {

    @FXML
    private ComboBox<EstadoReceta> btnFiltro;

    @FXML
    private Button btnCambiarEstado;

    @FXML
    private Button btnVolver;

    private Receta receta;
    private final RecetaController recetaController = new RecetaController();

    public void initialize() {
        btnFiltro.setItems(FXCollections.observableArrayList(EstadoReceta.values()));
    }

    public void setReceta(Receta receta) {
        this.receta = receta;
        if (receta != null) {
            btnFiltro.setValue(receta.getEstado());
        }
    }

    @FXML
    private void cambiarEstado(ActionEvent event) {
        if (receta == null || btnFiltro.getValue() == null) {
            Alerta.error("Error", "Debe seleccionar un estado.");
            return;
        }

        try {
            Farmaceuta farmaceuta = (Farmaceuta) Sesion.getUsuario();
            EstadoReceta nuevoEstado = btnFiltro.getValue();

            recetaController.actualizarEstado(farmaceuta, receta.getId(), nuevoEstado);
            receta.setEstado(nuevoEstado);

            Alerta.info("Éxito", "Estado actualizado a: " + nuevoEstado);
            cerrarVentana();

        } catch (Exception e) {
            Alerta.error("Error", "No se pudo actualizar: " + e.getMessage());
        }
    }

    @FXML
    private void Volver(ActionEvent event) {
        cerrarVentana();
    }

    private void cerrarVentana() {
        ((Stage) btnVolver.getScene().getWindow()).close();
    }
}