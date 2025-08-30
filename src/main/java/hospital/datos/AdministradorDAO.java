package hospital.datos;

import hospital.model.entidades.Administrador;
import java.util.ArrayList;
import java.util.List;

public class AdministradorDAO {
    private static final String FILE_PATH = "data/administradores.xml";
    private final ArchivoManager<Administrador> fileManager = new ArchivoManager<>(Administrador.class);
    private List<Administrador> administradores;

    public AdministradorDAO() {
        this.administradores = cargar();
    }

    // ==== CRUD ====
    public List<Administrador> listar() {
        return new ArrayList<>(administradores);
    }

    public void agregar(Administrador a) {
        if (buscarPorId(a.getId()) != null) {
            throw new IllegalArgumentException("Ya existe un administrador con el ID: " + a.getId());
        }
        a.setClave(a.getId()); // regla: clave inicial = id
        administradores.add(a);
        guardar();
    }

    public void modificar(Administrador adminActualizado) {
        for (int i = 0; i < administradores.size(); i++) {
            if (administradores.get(i).getId().equals(adminActualizado.getId())) {
                administradores.set(i, adminActualizado);
                guardar();
                return;
            }
        }
    }

    public void eliminar(String id) {
        administradores.removeIf(a -> a.getId().equals(id));
        guardar();
    }

    public Administrador buscarPorId(String id) {
        return administradores.stream()
                .filter(a -> a.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    // ==== Persistencia ====
    private void guardar() {
        try {
            fileManager.saveList(administradores, FILE_PATH);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Administrador> cargar() {
        try {
            return fileManager.loadList(FILE_PATH);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
