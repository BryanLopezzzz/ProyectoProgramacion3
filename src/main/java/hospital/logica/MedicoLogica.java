package hospital.logica;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import hospital.datos.MedicoDatos;
import hospital.datos.conector.MedicamentoConector;
import hospital.datos.conector.PacienteConector;
import hospital.logica.mapper.PacienteMapper;
import hospital.model.Medico;
import hospital.datos.conector.MedicoConector;
import hospital.logica.mapper.MedicoMapper;
import hospital.datos.entidades.MedicoEntidad;
import hospital.model.Paciente;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;

public class MedicoLogica {
    private final MedicoDatos datos;

    public MedicoLogica() {
        this.datos = new MedicoDatos("data/medicos.xml");
    }

    public void agregar(Medico medico) throws Exception {
        validarMedicoAlta(medico);

        MedicoConector con = datos.load();

        boolean existe = con.getMedicos().stream()
                .anyMatch(m -> m.getId().equalsIgnoreCase(medico.getId()));
        if (existe) throw new Exception("Ya existe un médico con el id: " + medico.getId());

        medico.setClave(medico.getId());

        con.getMedicos().add(MedicoMapper.toXML(medico));
        ordenarPorNombre(con);
        datos.save(con);
    }

    public List<Medico> listar() {
        return datos.load().getMedicos().stream()
                .map(MedicoMapper::fromXML)
                .collect(Collectors.toList());
    }

    public void modificar(Medico medico) throws Exception {
        validarMedicoModificacion(medico);

        MedicoConector con = datos.load();
        MedicoEntidad actual = con.getMedicos().stream()
                .filter(m -> m.getId().equalsIgnoreCase(medico.getId()))
                .findFirst()
                .orElseThrow(() -> new Exception("No existe médico con id: " + medico.getId()));

        actual.setNombre(medico.getNombre());
        actual.setEspecialidad(medico.getEspecialidad());

        ordenarPorNombre(con);
        datos.save(con);
    }

    private void validarMedicoAlta(Medico m) throws Exception {
        if (m == null) throw new Exception("El médico no puede ser nulo.");
        if (m.getId() == null || m.getId().isBlank()) throw new Exception("El id es obligatorio.");
        if (m.getNombre() == null || m.getNombre().isBlank()) throw new Exception("El nombre es obligatorio.");
        if (m.getEspecialidad() == null || m.getEspecialidad().isBlank())
            throw new Exception("La especialidad es obligatoria.");
    }

    private void validarMedicoModificacion(Medico m) throws Exception {
        if (m == null) throw new Exception("El médico no puede ser nulo.");
        if (m.getId() == null || m.getId().isBlank()) throw new Exception("El id es obligatorio.");
        if (m.getNombre() == null || m.getNombre().isBlank()) throw new Exception("El nombre es obligatorio.");
        if (m.getEspecialidad() == null || m.getEspecialidad().isBlank())
            throw new Exception("La especialidad es obligatoria.");
    }

    private void ordenarPorNombre(MedicoConector con) {
        con.getMedicos().sort(Comparator.comparing(
                e -> Objects.toString(e.getNombre(), ""), String.CASE_INSENSITIVE_ORDER
        ));
    }

    public Medico actualizar(Medico actualizado) throws Exception {
        validarMedicoModificacion(actualizado);

        MedicoConector data = datos.load();
        for (int i = 0; i < data.getMedicos().size(); i++) {
            MedicoEntidad actual = data.getMedicos().get(i);

            if (actual.getId().equalsIgnoreCase(actualizado.getId())) {
                data.getMedicos().set(i, MedicoMapper.toXML(actualizado));
                datos.save(data);
                return actualizado;
            }
        }
        throw new Exception("No existe médico con id: " + actualizado.getId());
    }

    public Medico buscarPorId(String medicoId) {
        return datos.load().getMedicos().stream()
                .map(MedicoMapper::fromXML) // usa el mapper sencillo
                .filter(m -> m.getId().equalsIgnoreCase(medicoId))
                .findFirst()
                .orElse(null);
    }

    public boolean eliminar(String id) throws Exception {
        MedicoConector conector = datos.load();
        boolean eliminado = conector.getMedicos().removeIf(r -> r.getId().equalsIgnoreCase(id));
        if (eliminado) {
            datos.save(conector);
        }
        return eliminado;
    }

    public List<Medico> buscarPorNombre(String nombre) {
        if (nombre == null) nombre = "";
        final String q = nombre.toLowerCase();
        return datos.load().getMedicos().stream()
                .filter(m -> m.getNombre() != null && m.getNombre().toLowerCase().contains(q))
                .map(MedicoMapper::fromXML)
                .collect(Collectors.toList());
    }

    public void generarReporte(String rutaReporte) {
        try {
            List<Medico> lista = listar();

            MedicoConector conector = new MedicoConector();
            for (Medico m : lista) {
                conector.getMedicos().add(MedicoMapper.toXML(m));
            }

            JAXBContext context = JAXBContext.newInstance(MedicamentoConector.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

            marshaller.marshal(conector, new File(rutaReporte));

        } catch (Exception e) {
            throw new RuntimeException("Error al generar reporte: " + e.getMessage(), e);
        }
    }
}
