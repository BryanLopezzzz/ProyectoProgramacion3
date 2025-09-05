package hospital.controller;

import hospital.model.Administrador;
import hospital.model.Paciente;

import java.util.List;

public class PacienteController extends ControllerGeneric<Paciente> {

    private final PacienteService pacienteService;

    public PacienteController() {
        super(new PacienteService());
        this.pacienteService = (PacienteService) super.service;
    }

    // búsqueda adicional específica de pacientes
    public List<Paciente> buscarPorNombre(Administrador admin, String nombre) throws Exception {
        validarAdmin(admin);
        return pacienteService.buscarPorNombre(nombre);
    }
}