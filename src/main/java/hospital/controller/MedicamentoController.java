package hospital.controller;

import hospital.model.datos.MedicamentoDAO;
import hospital.model.entidades.Medicamento;
import hospital.model.entidades.Administrador;
import java.util.List;

public class MedicamentoController extends ControllerGeneric<Medicamento> {
    public MedicamentoController() {
        super(new MedicamentoDAO());
    }

    // Búsqueda por código
    public Medicamento buscarPorCodigo(Administrador admin, String codigo) throws Exception {
        validarAdmin(admin);
        return ((MedicamentoDAO) dao).buscarPorCodigo(codigo);
    }

    // Búsqueda por nombre (ya lo hereda del genérico, pero si quieres alias explícito):
    public List<Medicamento> buscarPorNombre(Administrador admin, String nombre) throws Exception {
        validarAdmin(admin);
        return dao.buscarPorNombre(nombre);
    }
}
