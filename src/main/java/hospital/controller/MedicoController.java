package hospital.controller;

import hospital.model.Administrador;
import hospital.model.Medico;

import java.util.List;

public class MedicoController extends ControllerGeneric<Medico> {

    private final MedicoService medicoService;

    public MedicoController() {
        super(new MedicoService()); // pasamos el service al padre
        this.medicoService = (MedicoService) super.service;
    }

    // Método adicional que no está en el genérico
    public List<Medico> buscarPorNombre(Administrador admin, String nombre) throws Exception {
        validarAdmin(admin);
        return medicoService.buscarPorNombre(nombre);
    }
}
