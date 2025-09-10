package hospital.controller;

import hospital.model.Administrador;
import hospital.model.Farmaceuta;
import hospital.logica.FarmaceutaLogica;
import java.util.List;

public class FarmaceutaController {

    private final FarmaceutaLogica logica;

    public FarmaceutaController() {
        this.logica = new FarmaceutaLogica();
    }

    public void agregar(Administrador admin, Farmaceuta f) throws Exception {
        validarAdmin(admin);
        logica.agregar(f);
    }

    public List<Farmaceuta> listar(Administrador admin) throws Exception {
        validarAdmin(admin);
        return logica.listar();
    }

    public Farmaceuta buscarPorId(Administrador admin, String id) throws Exception {
        validarAdmin(admin);
        return logica.buscarPorId(id);
    }

    public List<Farmaceuta> buscarPorNombre(Administrador admin, String nombre) throws Exception {
        validarAdmin(admin);
        return logica.buscarPorNombre(nombre);
    }

    public void modificar(Administrador admin, Farmaceuta f) throws Exception {
        validarAdmin(admin);
        logica.modificar(f);
    }

    public void borrar(Administrador admin, String id) throws Exception {
        validarAdmin(admin);
        logica.eliminar(id);
    }

    private void validarAdmin(Administrador admin) throws Exception {
        if (admin == null) {
            throw new Exception("Solo los administradores pueden ejecutar esta acción.");
        }
    }
}
