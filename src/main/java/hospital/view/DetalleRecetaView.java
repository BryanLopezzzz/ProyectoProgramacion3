package hospital.view;

import hospital.controller.MedicamentoController;
import hospital.model.Administrador;
import hospital.model.DetalleReceta;
import hospital.model.Medicamento;
import hospital.model.Medico;
import hospital.model.Receta;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class DetalleRecetaView {

    @FXML private TextArea txtIndicaciones;
    @FXML private TextField txtCantidad;
    @FXML private TextField txtDuracion;
    @FXML private Button btnAgregar;
    @FXML private Button btnVolver;

    private Medico medico;
    private String recetaId;
    private String medicamentoId;

    private final MedicamentoController medicamentoController = new MedicamentoController();

    private DetalleReceta detalleCreado;

    private boolean modoEdicion = false;
    private DetalleReceta detalleEditable;

    public void setContext(Medico medico, String recetaId, String medicamentoId) {
        this.medico = medico;
        this.recetaId = recetaId;
        this.medicamentoId = medicamentoId;
    }

    public void setDetalleParaEditar(DetalleReceta detalle) {
        if (detalle == null) return;
        this.modoEdicion = true;
        this.detalleEditable = detalle;
        // Prefill UI
        txtCantidad.setText(String.valueOf(detalle.getCantidad()));
        txtDuracion.setText(String.valueOf(detalle.getDiasTratamiento()));
        txtIndicaciones.setText(detalle.getIndicaciones());
    }

    public void setReceta(Receta receta) {
        txtCantidad.setEditable(false);
        txtDuracion.setEditable(false);
        txtIndicaciones.setEditable(false);
        btnAgregar.setVisible(false);

        if (receta != null && !receta.getDetalles().isEmpty()) {
            var primer = receta.getDetalles().get(0);
            txtCantidad.setText(String.valueOf(primer.getCantidad()));
            txtDuracion.setText(String.valueOf(primer.getDiasTratamiento()));
            txtIndicaciones.setText(primer.getIndicaciones());
        }
    }

    @FXML
    private void Agregar() {
        try {
            int cantidad = Integer.parseInt(txtCantidad.getText().trim());
            int dias = Integer.parseInt(txtDuracion.getText().trim());
            String indicaciones = txtIndicaciones.getText().trim();

            if (modoEdicion && detalleEditable != null) {
                detalleEditable.setCantidad(cantidad);
                detalleEditable.setDiasTratamiento(dias);
                detalleEditable.setIndicaciones(indicaciones);
                this.detalleCreado = detalleEditable;
            } else {
                DetalleReceta nuevo = new DetalleReceta();
                nuevo.setCantidad(cantidad);
                nuevo.setDiasTratamiento(dias);
                nuevo.setIndicaciones(indicaciones);

                Medicamento medicamentoCompleto = medicamentoController.buscarPorCodigo(
                        new Administrador(),
                        medicamentoId
                );

                if (medicamentoCompleto == null) {
                    mostrarError("Error", "No se pudo encontrar el medicamento con código: " + medicamentoId);
                    return;
                }

                nuevo.setMedicamento(medicamentoCompleto);
                this.detalleCreado = nuevo;
            }

            Alerta.info("Informacion", "Agregado correctamente.");
            cerrarVentana();

        } catch (NumberFormatException e) {
            mostrarError("Entrada inválida", "Cantidad y duración deben ser números enteros.");
        } catch (Exception e) {
            mostrarError("Error", "Error al procesar el medicamento: " + e.getMessage());
        }
    }

    @FXML
    private void Volver() {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) btnVolver.getScene().getWindow();
        stage.close();
    }

    public DetalleReceta getDetalleCreado() {
        return detalleCreado;
    }

    private void mostrarError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarInfo(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, mensaje);
        alert.showAndWait();
    }
}