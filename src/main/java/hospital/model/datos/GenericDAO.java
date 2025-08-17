package hospital.model.datos;

import java.util.ArrayList;
import java.util.List;

public abstract class GenericDAO<T> {
    private final ArchivoManager<T> fileManager;
    private final String filePath;
    protected List<T> elementos;

    public GenericDAO(Class<T> type, String filePath) {
        this.fileManager = new ArchivoManager<>(type);
        this.filePath = filePath;
        this.elementos = cargar();
    }

    // ==== CRUD genérico ====

    public List<T> listar() {
        return new ArrayList<>(elementos);
    }

    public void agregar(T obj) {
        if (buscarPorId(getId(obj)) != null) {
            throw new IllegalArgumentException("Ya existe un registro con el ID: " + getId(obj));
        }
        elementos.add(obj);
        guardar();
    }

    public void modificar(T actualizado) {
        String id = getId(actualizado);
        for (int i = 0; i < elementos.size(); i++) {
            if (getId(elementos.get(i)).equals(id)) {
                elementos.set(i, actualizado);
                guardar();
                return;
            }
        }
    }

    public void eliminar(String id) {
        elementos.removeIf(e -> getId(e).equals(id));
        guardar();
    }

    public T buscarPorId(String id) {
        return elementos.stream()
                .filter(e -> getId(e).equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }
    // ==== Búsqueda por nombre genérica ====
    public List<T> buscarPorNombre(String nombre) {
        List<T> resultados = new ArrayList<>();
        for (T e : elementos) {
            if (getNombre(e).toLowerCase().contains(nombre.toLowerCase())) {
                resultados.add(e);
            }
        }
        return resultados;
    }

    // ==== persistencia ====
    private void guardar() {
        try {
            fileManager.saveList(elementos, filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<T> cargar() {
        try {
            return fileManager.loadList(filePath);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    // metodo abstracto para obtener el ID/codigo de cualquier entidad
    protected abstract String getId(T obj);
    // metodo abstracto para obtener el nombre/descrpcion de cualquier entidad
    protected abstract String getNombre(T obj);
}
