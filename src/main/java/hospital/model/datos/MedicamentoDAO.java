package hospital.model.datos;

import hospital.model.entidades.Medicamento;
import java.util.ArrayList;
import java.util.List;
//falta la restricción de que solo el administrador pueda usar esta funcionalidad. Eso se maneja en la capa de controlador/interfaz.
public class MedicamentoDAO {
    private static final String FILE_PATH = "data/medicamentos.xml";
    private final ArchivoManager<Medicamento> fileManager = new ArchivoManager<>(Medicamento.class);
    private List<Medicamento> medicamentos;

    public MedicamentoDAO() {
        this.medicamentos = cargar();
    }

    // ==== CRUD ====

    public List<Medicamento> listar() {
        return new ArrayList<>(medicamentos);
    }

    public void agregar(Medicamento m) {
        medicamentos.add(m);
        guardar();
    }

    public void modificar(Medicamento medicamentoActualizado) {
        for (int i = 0; i < medicamentos.size(); i++) {
            if (medicamentos.get(i).getCodigo().equalsIgnoreCase(medicamentoActualizado.getCodigo())) {
                medicamentos.set(i, medicamentoActualizado);
                guardar();
                return;
            }
        }
    }

    public void eliminar(String codigo) {
        medicamentos.removeIf(m -> m.getCodigo().equalsIgnoreCase(codigo));
        guardar();
    }

    public Medicamento buscarPorCodigo(String codigo) {
        return medicamentos.stream()
                .filter(m -> m.getCodigo().equalsIgnoreCase(codigo))
                .findFirst()
                .orElse(null);
    }

    public List<Medicamento> buscarPorNombre(String nombre) {
        List<Medicamento> resultados = new ArrayList<>();
        for (Medicamento m : medicamentos) {
            if (m.getNombre().toLowerCase().contains(nombre.toLowerCase())) {
                resultados.add(m);
            }
        }
        return resultados;
    }

    // ==== Persistencia ====

    private void guardar() {
        try {
            fileManager.saveList(medicamentos, FILE_PATH);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Medicamento> cargar() {
        try {
            return fileManager.loadList(FILE_PATH);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
