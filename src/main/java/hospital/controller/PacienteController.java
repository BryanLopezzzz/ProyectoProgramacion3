package hospital.controller;

import hospital.model.Administrador;
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

    // búsqueda adicional específica de pacientes
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
}