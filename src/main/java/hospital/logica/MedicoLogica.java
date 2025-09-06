package hospital.logica;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import hospital.datos.MedicoDatos;
import hospital.model.Medico;
import hospital.datos.conector.MedicoConector;
import hospital.logica.mapper.MedicoMapper;
import hospital.datos.entidades.MedicoEntidad;

public class MedicoLogica {
    private final MedicoDatos datos;

    public MedicoLogica() {
        this.datos = new MedicoDatos("data/medicos.xml");
    }

    public void agregar(Medico medico) throws Exception {
        validarMedicoAlta(medico);

        MedicoConector con = datos.load();

        // Unicidad de ID
        boolean existe = con.getMedicos().stream()
                .anyMatch(m -> m.getId().equalsIgnoreCase(medico.getId()));
        if (existe) throw new Exception("Ya existe un médico con el id: " + medico.getId());

        // Regla de enunciado: al agregar, la clave queda igual al id
        medico.setClave(medico.getId()); // fuerza la regla del enunciado

        con.getMedicos().add(MedicoMapper.toXML(medico));
        ordenarPorNombre(con);
        datos.save(con);
    }

    public List<Medico> listar() {
        return datos.load().getMedicos().stream()
                .map(MedicoMapper::fromXML)
                .collect(Collectors.toList());
    }

    public Medico buscarPorId(String id) {
        if (id == null) return null;
        return datos.load().getMedicos().stream()
                .filter(m -> m.getId().equalsIgnoreCase(id))
                .map(MedicoMapper::fromXML)
                .findFirst()
                .orElse(null);
    }

    public List<Medico> buscarPorNombre(String nombre) {
        if (nombre == null) nombre = "";
        final String q = nombre.toLowerCase();
        return datos.load().getMedicos().stream()
                .filter(m -> m.getNombre() != null && m.getNombre().toLowerCase().contains(q))
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

        // Actualiza campos editables
        actual.setNombre(medico.getNombre());
        actual.setEspecialidad(medico.getEspecialidad());

        // La clave NO se cambia aquí (lo harían desde “cambiar clave” del login general)

        ordenarPorNombre(con);
        datos.save(con);
    }

    public void borrar(String id) throws Exception {
        if (id == null || id.isBlank()) throw new Exception("El id es obligatorio.");

        MedicoConector con = datos.load();
        boolean removed = con.getMedicos().removeIf(m -> m.getId().equalsIgnoreCase(id));
        if (!removed) throw new Exception("No existe médico con id: " + id);

        datos.save(con);
    }

    private void validarMedicoAlta(Medico m) throws Exception {
        if (m == null) throw new Exception("El médico no puede ser nulo.");
        if (m.getId() == null || m.getId().isBlank()) throw new Exception("El id es obligatorio.");
        if (m.getNombre() == null || m.getNombre().isBlank()) throw new Exception("El nombre es obligatorio.");
        if (m.getEspecialidad() == null || m.getEspecialidad().isBlank())
            throw new Exception("La especialidad es obligatoria.");
        // m.getClave() la forzamos = id en agregar()
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
}
