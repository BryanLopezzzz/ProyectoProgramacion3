package hospital.view.registro;

import hospital.model.Paciente;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;

public class RegistroPacienteView {
    @FXML private TextField txtNombrePaciente;
    @FXML private TextField txtIdentificacionPaciente;
    @FXML private TextField txtTelefonoPaciente;
    @FXML private DatePicker dtpFechaNacimineto;

    @FXML private Button btnGuardarPaciente;
    @FXML private Button btnBorrar;
    @FXML private Button btnLimpiarPaciente;

    private Paciente paciente;

    /// public void setPaciente(Paciente paciente) {}

    public void guardarPaciente(){

    }


}
