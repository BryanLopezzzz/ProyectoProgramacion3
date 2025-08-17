package hospital.controller;

import hospital.model.datos.MedicoDAO;
import hospital.model.entidades.Medico;
import hospital.model.entidades.Administrador;
import java.util.List;

public class MedicoController extends ControllerGeneric<Medico> {
    public MedicoController() {
        super(new MedicoDAO());
    }

    public List<Medico> buscarPorNombre(Administrador admin, String nombre) throws Exception {
        validarAdmin(admin);
        return ((MedicoDAO) dao).buscarPorNombre(nombre);
    }
}