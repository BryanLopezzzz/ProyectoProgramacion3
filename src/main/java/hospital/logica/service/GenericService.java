package hospital.logica.service;

import java.util.List;

public interface GenericService<T> {
    void agregar(T entidad) throws Exception;
    void modificar(T entidad) throws Exception;
    void eliminar(String id) throws Exception;
    T buscarPorId(String id) throws Exception;
    List<T> listar();
}
