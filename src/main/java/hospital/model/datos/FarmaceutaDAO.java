package hospital.model.datos;

import hospital.model.entidades.Farmaceuta;
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

public class FarmaceutaDAO {

    private static final String FILE_PATH = "data/farmaceutas.xml"; // ruta del archivo XML
    private final ArchivoManager<Farmaceuta> fileManager = new ArchivoManager<>(Farmaceuta.class);
    private List<Farmaceuta> farmaceutas;

    public FarmaceutaDAO() {
        this.farmaceutas = cargar();
    }

    // ==== Operaciones CRUD ====

    public List<Farmaceuta> listar() {
        return new ArrayList<>(farmaceutas);
    }

    public void agregar(Farmaceuta f) {
        // Regla del enunciado: clave inicial = id
        f.setClave(f.getId());

        farmaceutas.add(f);
        guardar();
    }

    public void modificar(Farmaceuta farmaceutaActualizado) {
        for (int i = 0; i < farmaceutas.size(); i++) {
            if (farmaceutas.get(i).getId().equals(farmaceutaActualizado.getId())) {
                farmaceutas.set(i, farmaceutaActualizado);
                guardar();
                return;
            }
        }
    }

    public void eliminar(String id) {
        farmaceutas.removeIf(f -> f.getId().equals(id));
        guardar();
    }

    public Farmaceuta buscarPorId(String id) {
        return farmaceutas.stream()
                .filter(f -> f.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    public List<Farmaceuta> buscarPorNombre(String nombre) {
        List<Farmaceuta> resultados = new ArrayList<>();
        for (Farmaceuta f : farmaceutas) {
            if (f.getNombre().toLowerCase().contains(nombre.toLowerCase())) {
                resultados.add(f);
            }
        }
        return resultados;
    }

    // ==== Persistencia con FileManager ====

    private void guardar() {
        try {
            fileManager.saveList(farmaceutas, FILE_PATH);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Farmaceuta> cargar() {
        try {
            return fileManager.loadList(FILE_PATH);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}