package hospital.controller;

import hospital.model.Administrador;

import java.util.List;

public abstract class ControllerGeneric<T> {
    protected final GenericService<T> service;

    public ControllerGeneric(GenericService<T> service) {
        this.service = service;
    }

    public void agregar(Administrador admin, T entidad) throws Exception {
        validarAdmin(admin);
        service.agregar(entidad);
    }

    public void modificar(Administrador admin, T entidad) throws Exception {
        validarAdmin(admin);
        service.modificar(entidad);
    }

    public void eliminar(Administrador admin, String id) throws Exception {
        validarAdmin(admin);
        service.eliminar(id);
    }

    public T buscarPorId(Administrador admin, String id) throws Exception {
        validarAdmin(admin);
        return service.buscarPorId(id);
    }

    public List<T> listar(Administrador admin) throws Exception {
        validarAdmin(admin);
        return service.listar();
    }

    protected void validarAdmin(Administrador admin) throws Exception {
        if (admin == null) {
            throw new Exception("Solo los administradores pueden ejecutar esta acción.");
        }
    }
}
