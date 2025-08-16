package hospital.model.datos;

import hospital.model.entidades.Medico;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class MedicoDAO {

    private static final String FILE_PATH = "data/medicos.xml"; // ruta del archivo XML
    private final ArchivoManager<Medico> fileManager = new ArchivoManager<>(Medico.class);
    private List<Medico> medicos;

    public MedicoDAO() {
        this.medicos = cargar();
    }

    // ==== Esto es parte de lo básico que pide el enunciado ====

    public List<Medico> listar() {
        return new ArrayList<>(medicos);
    }

    public void agregar(Medico m) {
        if (buscarPorId(m.getId()) != null) {
            throw new IllegalArgumentException("Ya existe un médico con el ID: " + m.getId());
        }

        // Regla del enunciado: clave inicial = id
        m.setClave(m.getId());
        medicos.add(m);
        guardar();
    }

    public void modificar(Medico medicoActualizado) {
        for (int i = 0; i < medicos.size(); i++) {
            if (medicos.get(i).getId().equals(medicoActualizado.getId())) {
                medicos.set(i, medicoActualizado);
                guardar();
                return;
            }
        }
    }

    public void eliminar(String id) {
        medicos.removeIf(m -> m.getId().equals(id));
        guardar();
    }

    public Medico buscarPorId(String id) {
        return medicos.stream()
                .filter(m -> m.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    public List<Medico> buscarPorNombre(String nombre) {
        List<Medico> resultados = new ArrayList<>();
        for (Medico m : medicos) {
            if (m.getNombre().toLowerCase().contains(nombre.toLowerCase())) {
                resultados.add(m);
            }
        }
        return resultados;
    }

    // ==== Persistencia con FileManager ====

    private void guardar() {
        try {
            fileManager.saveList(medicos, FILE_PATH);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Medico> cargar() {
        try {
            return fileManager.loadList(FILE_PATH);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
