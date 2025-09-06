package hospital.logica;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import hospital.model.Farmaceuta;
import hospital.datos.FarmaceutaDatos;
import hospital.datos.conector.FarmaceutaConector;
import hospital.datos.entidades.FarmaceutaEntidad;
import hospital.logica.mapper.FarmaceutaMapper;

public class FarmaceutaLogica {
    private final FarmaceutaDatos datos;

    public FarmaceutaLogica() {
        this.datos = new FarmaceutaDatos("data/farmaceutas.xml");
    }

    public void agregar(Farmaceuta farmaceuta) throws Exception {
        validarAlta(farmaceuta);

        FarmaceutaConector con = datos.load();

        boolean existe = con.getFarmaceutas().stream()
                .anyMatch(f -> f.getId().equalsIgnoreCase(farmaceuta.getId()));
        if (existe) throw new Exception("Ya existe un farmaceuta con id: " + farmaceuta.getId());

        // Regla: clave = id al agregar
        farmaceuta.setClave(farmaceuta.getId());

        con.getFarmaceutas().add(FarmaceutaMapper.toXML(farmaceuta));
        ordenarPorNombre(con);
        datos.save(con);
    }

    public List<Farmaceuta> listar() {
        return datos.load().getFarmaceutas().stream()
                .map(FarmaceutaMapper::fromXML)
                .collect(Collectors.toList());
    }

    public Farmaceuta buscarPorId(String id) {
        if (id == null) return null;
        return datos.load().getFarmaceutas().stream()
                .filter(f -> f.getId().equalsIgnoreCase(id))
                .map(FarmaceutaMapper::fromXML)
                .findFirst()
                .orElse(null);
    }

    public List<Farmaceuta> buscarPorNombre(String nombre) {
        if (nombre == null) nombre = "";
        final String q = nombre.toLowerCase();
        return datos.load().getFarmaceutas().stream()
                .filter(f -> f.getNombre() != null && f.getNombre().toLowerCase().contains(q))
                .map(FarmaceutaMapper::fromXML)
                .collect(Collectors.toList());
    }

    public void modificar(Farmaceuta farmaceuta) throws Exception {
        validarModificacion(farmaceuta);

        FarmaceutaConector con = datos.load();
        FarmaceutaEntidad actual = con.getFarmaceutas().stream()
                .filter(f -> f.getId().equalsIgnoreCase(farmaceuta.getId()))
                .findFirst()
                .orElseThrow(() -> new Exception("No existe farmaceuta con id: " + farmaceuta.getId()));

        actual.setNombre(farmaceuta.getNombre());
        // La clave no se cambia aquí, solo desde el módulo de login/cambio clave.

        ordenarPorNombre(con);
        datos.save(con);
    }

    public void borrar(String id) throws Exception {
        if (id == null || id.isBlank()) throw new Exception("El id es obligatorio.");

        FarmaceutaConector con = datos.load();
        boolean removed = con.getFarmaceutas().removeIf(f -> f.getId().equalsIgnoreCase(id));
        if (!removed) throw new Exception("No existe farmaceuta con id: " + id);

        datos.save(con);
    }

    private void validarAlta(Farmaceuta f) throws Exception {
        if (f == null) throw new Exception("El farmaceuta no puede ser nulo.");
        if (f.getId() == null || f.getId().isBlank()) throw new Exception("El id es obligatorio.");
        if (f.getNombre() == null || f.getNombre().isBlank()) throw new Exception("El nombre es obligatorio.");
    }

    private void validarModificacion(Farmaceuta f) throws Exception {
        if (f == null) throw new Exception("El farmaceuta no puede ser nulo.");
        if (f.getId() == null || f.getId().isBlank()) throw new Exception("El id es obligatorio.");
        if (f.getNombre() == null || f.getNombre().isBlank()) throw new Exception("El nombre es obligatorio.");
    }

    private void ordenarPorNombre(FarmaceutaConector con) {
        con.getFarmaceutas().sort(Comparator.comparing(
                e -> Objects.toString(e.getNombre(), ""), String.CASE_INSENSITIVE_ORDER
        ));
    }
}