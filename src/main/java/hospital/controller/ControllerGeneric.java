package hospital.controller;

import hospital.model.datos.GenericDAO;
import hospital.model.entidades.Administrador;
import java.util.List;

public abstract class ControllerGeneric<T> {
    protected final GenericDAO<T> dao;

    public ControllerGeneric(GenericDAO<T> dao) {
        this.dao = dao;
    }

    public void agregar(Administrador admin, T entidad) throws Exception {
        validarAdmin(admin);
        dao.agregar(entidad);
    }

    public void modificar(Administrador admin, T entidad) throws Exception {
        validarAdmin(admin);
        dao.modificar(entidad);
    }

    public void eliminar(Administrador admin, String id) throws Exception {
        validarAdmin(admin);
        dao.eliminar(id);
    }

    public T buscarPorId(Administrador admin, String id) throws Exception {
        validarAdmin(admin);
        return dao.buscarPorId(id);
    }

    public List<T> listar(Administrador admin) throws Exception {
        validarAdmin(admin);
        return dao.listar();
    }

    protected void validarAdmin(Administrador admin) throws Exception {
        if (admin == null) {
            throw new Exception("Solo los administradores pueden ejecutar esta acción.");
        }
    }
}