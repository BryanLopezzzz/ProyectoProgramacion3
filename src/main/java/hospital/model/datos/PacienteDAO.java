package hospital.model.datos;

import hospital.model.entidades.Paciente;
import java.util.ArrayList;
import java.util.List;

public class PacienteDAO {

    private static final String FILE_PATH = "data/pacientes.xml";
    private final ArchivoManager<Paciente> fileManager = new ArchivoManager<>(Paciente.class);
    private List<Paciente> pacientes;

    public PacienteDAO() {
        this.pacientes = cargar();
    }

    // ==== CRUD ====

    public List<Paciente> listar() {
        return new ArrayList<>(pacientes);
    }

    public void agregar(Paciente p) {
        pacientes.add(p);
        guardar();
    }

    public void modificar(Paciente pacienteActualizado) {
        for (int i = 0; i < pacientes.size(); i++) {
            if (pacientes.get(i).getId().equals(pacienteActualizado.getId())) {
                pacientes.set(i, pacienteActualizado);
                guardar();
                return;
            }
        }
    }

    public void eliminar(String id) {
        pacientes.removeIf(p -> p.getId().equals(id));
        guardar();
    }

    public Paciente buscarPorId(String id) {
        return pacientes.stream()
                .filter(p -> p.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    public List<Paciente> buscarPorNombre(String nombre) {
        List<Paciente> resultados = new ArrayList<>();
        for (Paciente p : pacientes) {
            if (p.getNombre().toLowerCase().contains(nombre.toLowerCase())) {
                resultados.add(p);
            }
        }
        return resultados;
    }

    // ==== Persistencia ====

    private void guardar() {
        try {
            fileManager.saveList(pacientes, FILE_PATH);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Paciente> cargar() {
        try {
            return fileManager.loadList(FILE_PATH);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
