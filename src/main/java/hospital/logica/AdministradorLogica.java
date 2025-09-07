package hospital.logica;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import hospital.datos.AdministradorDatos;
import hospital.model.Administrador;
import hospital.datos.conector.AdministradorConector;
import hospital.logica.mapper.AdministradorMapper;
import hospital.datos.entidades.AdministradorEntidad;

public class AdministradorLogica {
    private final AdministradorDatos datos;

    public AdministradorLogica() {
        this.datos = new AdministradorDatos("data/administradores.xml");
    }

    public void agregar(Administrador admin) throws Exception {
        validarAlta(admin);

        AdministradorConector con = datos.load();

        // Unicidad de ID
        boolean existe = con.getAdministradores().stream()
                .anyMatch(a -> a.getId().equalsIgnoreCase(admin.getId()));
        if (existe) throw new Exception("Ya existe un administrador con id: " + admin.getId());

        // Regla del enunciado: clave inicial = id
        admin.setClave(admin.getId());

        con.getAdministradores().add(AdministradorMapper.toXML(admin));
        ordenarPorNombre(con);
        datos.save(con);
    }

    // ==== Listar ====
    public List<Administrador> listar() {
        return datos.load().getAdministradores().stream()
                .map(AdministradorMapper::fromXML)
                .collect(Collectors.toList());
    }

    public Administrador buscarPorId(String id) {
        if (id == null) return null;
        return datos.load().getAdministradores().stream()
                .filter(a -> a.getId().equalsIgnoreCase(id))
                .map(AdministradorMapper::fromXML)
                .findFirst()
                .orElse(null);
    }

    public List<Administrador> buscarPorNombre(String nombre) {
        if (nombre == null) nombre = "";
        final String q = nombre.toLowerCase();
        return datos.load().getAdministradores().stream()
                .filter(a -> a.getNombre() != null && a.getNombre().toLowerCase().contains(q))
                .map(AdministradorMapper::fromXML)
                .collect(Collectors.toList());
    }

    // ==== Modificar ====
    public void modificar(Administrador admin) throws Exception {
        validarModificacion(admin);

        AdministradorConector con = datos.load();
        AdministradorEntidad actual = con.getAdministradores().stream()
                .filter(a -> a.getId().equalsIgnoreCase(admin.getId()))
                .findFirst()
                .orElseThrow(() -> new Exception("No existe administrador con id: " + admin.getId()));

        // Campos editables
        actual.setNombre(admin.getNombre());
        // La clave no se cambia aquí, se hace desde el módulo login/cambio clave

        ordenarPorNombre(con);
        datos.save(con);
    }

    public void borrar(String id) throws Exception {
        if (id == null || id.isBlank()) throw new Exception("El id es obligatorio.");

        AdministradorConector con = datos.load();
        boolean removed = con.getAdministradores().removeIf(a -> a.getId().equalsIgnoreCase(id));
        if (!removed) throw new Exception("No existe administrador con id: " + id);

        datos.save(con);
    }

    private void validarAlta(Administrador a) throws Exception {
        if (a == null) throw new Exception("El administrador no puede ser nulo.");
        if (a.getId() == null || a.getId().isBlank()) throw new Exception("El id es obligatorio.");
        if (a.getNombre() == null || a.getNombre().isBlank()) throw new Exception("El nombre es obligatorio.");
        // La clave la forzamos = id en agregar()
    }

    private void validarModificacion(Administrador a) throws Exception {
        if (a == null) throw new Exception("El administrador no puede ser nulo.");
        if (a.getId() == null || a.getId().isBlank()) throw new Exception("El id es obligatorio.");
        if (a.getNombre() == null || a.getNombre().isBlank()) throw new Exception("El nombre es obligatorio.");
    }

    private void ordenarPorNombre(AdministradorConector con) {
        con.getAdministradores().sort(Comparator.comparing(
                e -> Objects.toString(e.getNombre(), ""), String.CASE_INSENSITIVE_ORDER
        ));
    }
}
