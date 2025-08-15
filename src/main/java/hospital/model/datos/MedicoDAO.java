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
    private List<Medico> medicos;

    public MedicoDAO() {
        this.medicos = cargar();
    }

    // ==== Esto es parte de lo básico que pide el enunciado ====

    public List<Medico> listar() {
        return new ArrayList<>(medicos);
    }

    public void agregar(Medico m) {
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

    // ==== Persistencia XML usando DOM ====

    private void guardar() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            Element root = doc.createElement("medicos");
            doc.appendChild(root);

            for (Medico m : medicos) {
                Element medicoElem = doc.createElement("medico");

                Element id = doc.createElement("id");
                id.appendChild(doc.createTextNode(m.getId()));
                medicoElem.appendChild(id);

                Element nombre = doc.createElement("nombre");
                nombre.appendChild(doc.createTextNode(m.getNombre()));
                medicoElem.appendChild(nombre);

                Element especialidad = doc.createElement("especialidad");
                especialidad.appendChild(doc.createTextNode(m.getEspecialidad()));
                medicoElem.appendChild(especialidad);

                root.appendChild(medicoElem);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);

            File file = new File(FILE_PATH);
            file.getParentFile().mkdirs(); // crea carpeta si no existe
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);

        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }
    }

    private List<Medico> cargar() {
        List<Medico> lista = new ArrayList<>();
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                return lista;
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);

            NodeList nodos = doc.getElementsByTagName("medico");
            for (int i = 0; i < nodos.getLength(); i++) {
                Node nodo = nodos.item(i);
                if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                    Element elem = (Element) nodo;
                    Medico m = new Medico();
                    m.setId(getTagValue("id", elem));
                    m.setNombre(getTagValue("nombre", elem));
                    m.setEspecialidad(getTagValue("especialidad", elem));
                    lista.add(m);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    private String getTagValue(String tag, Element elem) {
        NodeList nl = elem.getElementsByTagName(tag);
        if (nl != null && nl.getLength() > 0) {
            return nl.item(0).getTextContent();
        }
        return "";
    }
}
