package hospital.logica.service;

import hospital.datos.GenericDAO;

import java.util.List;

public abstract class GenericServiceImpl<T> implements GenericService<T> {

    protected final GenericDAO<T> dao;

    public GenericServiceImpl(GenericDAO<T> dao) {
        this.dao = dao;
    }

    @Override
    public void agregar(T entidad) throws Exception {
        dao.agregar(entidad);
    }

    @Override
    public void modificar(T entidad) throws Exception {
        dao.modificar(entidad);
    }

    @Override
    public void eliminar(String id) throws Exception {
        dao.eliminar(id);
    }

    @Override
    public T buscarPorId(String id) throws Exception {
        return dao.buscarPorId(id);
    }

    @Override
    public List<T> listar() {
        return dao.listar();
    }
}
