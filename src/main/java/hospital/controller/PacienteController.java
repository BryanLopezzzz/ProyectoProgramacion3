package hospital.controller;

import hospital.model.Administrador;
import hospital.model.Medicamento;
import hospital.model.Paciente;
import hospital.logica.PacienteLogica;

import java.util.List;

public class PacienteController {

    private final PacienteLogica pacienteLogica;

    public PacienteController() {
        this.pacienteLogica = new PacienteLogica();
    }

    public void agregar(Administrador admin, Paciente paciente) throws Exception {
        validarAdmin(admin);
        pacienteLogica.agregar(paciente);
    }

    public List<Paciente> listar(Administrador admin) throws Exception {
        validarAdmin(admin);
        return pacienteLogica.listar();
    }

    public Paciente buscarPorId(Administrador admin, String id) throws Exception {
        validarAdmin(admin);
        return pacienteLogica.buscarPorId(id);
    }

    public List<Paciente> buscarPorNombre(Administrador admin, String nombre) throws Exception {
        validarAdmin(admin);
        return pacienteLogica.listar().stream()
                .filter(p -> p.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .toList();
    }

    private void validarAdmin(Administrador admin) throws Exception {
        if (admin == null) {
            throw new Exception("Solo los administradores pueden ejecutar esta acción.");
        }
    }

    public void generarReporte(Administrador admin, String rutaReporte) throws Exception {
        validarAdmin(admin);
        pacienteLogica.generarReporte(rutaReporte);
    }

    public boolean eliminar(Administrador admin, String codigo) throws Exception {
        validarAdmin(admin);
        return pacienteLogica.eliminar(codigo);
    }

    public void modificar(Administrador admin, Paciente medicamento) throws Exception {
        validarAdmin(admin);
        pacienteLogica.actualizar(medicamento);
    }
}