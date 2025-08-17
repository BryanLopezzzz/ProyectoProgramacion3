package hospital.controller;

import hospital.model.datos.PacienteDAO;
import hospital.model.entidades.Paciente;
import hospital.model.entidades.Administrador;
import java.util.List;

public class PacienteController extends ControllerGeneric<Paciente> {
    public PacienteController() {
        super(new PacienteDAO());
    }

    // Si quieres permitir búsqueda por nombre también
    public List<Paciente> buscarPorNombre(Administrador admin, String nombre) throws Exception {
        validarAdmin(admin);
        return dao.buscarPorNombre(nombre);
    }
}