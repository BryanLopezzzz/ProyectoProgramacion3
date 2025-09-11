package hospital.logica;

import hospital.model.Usuario;
import hospital.model.Medico;
import hospital.model.Farmaceuta;
import hospital.model.Administrador;
import hospital.logica.MedicoLogica;
import hospital.logica.FarmaceutaLogica;
import hospital.logica.AdministradorLogica;

import java.util.ArrayList;
import java.util.List;

public class UsuarioManager {
    private final MedicoLogica medicoLogica = new MedicoLogica();
    private final FarmaceutaLogica farmaceutaLogica = new FarmaceutaLogica();
    private final AdministradorLogica administradorLogica = new AdministradorLogica();
    private static Usuario usuarioActual;

    public enum TipoUsuario {
        ADMINISTRADOR, MEDICO, FARMACEUTA, DESCONOCIDO
    }
    public static Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public static void setUsuarioActual(Usuario usuario) {
        usuarioActual = usuario;
    }

    public static void logout() {
        usuarioActual = null;
    }


    // ==== LOGIN ====
    public Usuario login(String id, String clave) throws Exception {
        if (id == null || id.isBlank())
            throw new Exception("El id es obligatorio.");
        if (clave == null || clave.isBlank())
            throw new Exception("La clave es obligatoria.");

        Usuario u = administradorLogica.buscarPorId(id);
        if (u != null && u.getClave().equals(clave)) return u;

        u = farmaceutaLogica.buscarPorId(id);
        if (u != null && u.getClave().equals(clave)) return u;

        u = medicoLogica.buscarPorId(id);
        if (u != null && u.getClave().equals(clave)) return u;

        throw new Exception("Credenciales incorrectas.");
    }

    // ==== Cambio de clave ====
    public void cambiarClave(String id, String actual, String nueva) throws Exception {
        if (id == null || id.isBlank())
            throw new Exception("El id es obligatorio.");
        if (actual == null || actual.isBlank())
            throw new Exception("La clave actual es obligatoria.");
        if (nueva == null || nueva.isBlank())
            throw new Exception("La nueva clave no puede estar vacía.");

        // Buscar en cada tipo de usuario
        Administrador a = administradorLogica.buscarPorId(id);
        if (a != null) {
            if (!a.getClave().equals(actual))
                throw new Exception("Clave actual incorrecta.");
            a.setClave(nueva);
            administradorLogica.modificar(a);
            return;
        }

        Medico m = medicoLogica.buscarPorId(id);
        if (m != null) {
            if (!m.getClave().equals(actual))
                throw new Exception("Clave actual incorrecta.");
            m.setClave(nueva);
            medicoLogica.modificar(m); // Persistir
            return;
        }

        Farmaceuta f = farmaceutaLogica.buscarPorId(id);
        if (f != null) {
            if (!f.getClave().equals(actual))
                throw new Exception("Clave actual incorrecta.");
            f.setClave(nueva);
            farmaceutaLogica.modificar(f);
            return;
        }
        throw new Exception("Usuario no encontrado.");
    }

    public boolean existeUsuario(String id) {
        if (administradorLogica.buscarPorId(id) != null) return true;
        if (medicoLogica.buscarPorId(id) != null) return true;
        if (farmaceutaLogica.buscarPorId(id) != null) return true;
        return false;
    }

    public List<Usuario> listarTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        usuarios.addAll(medicoLogica.listar());
        usuarios.addAll(farmaceutaLogica.listar());
        usuarios.addAll(administradorLogica.listar());
        return usuarios;
    }

    public TipoUsuario determinarTipoUsuario(String id) {
        if (id == null || id.isEmpty()) {
            return TipoUsuario.DESCONOCIDO;
        }
        if (id.equalsIgnoreCase("admin")) {
            return TipoUsuario.ADMINISTRADOR;
        }

        String prefijo = id.substring(0, 1).toUpperCase();
        switch (prefijo) {
            case "M": return TipoUsuario.MEDICO;
            case "F": return TipoUsuario.FARMACEUTA;
            default: return TipoUsuario.DESCONOCIDO;
        }
    }

    public TipoUsuario getTipoUsuarioActual() {
        if (usuarioActual == null) {
            return TipoUsuario.DESCONOCIDO;
        }
        return determinarTipoUsuario(usuarioActual.getId());
    }

    public boolean tienePermiso(String id, String funcionalidad) {
        TipoUsuario tipo = determinarTipoUsuario(id);

        switch (tipo) {
            case ADMINISTRADOR:
                return true;
            case MEDICO:
                return funcionalidad.equals("PRESCRIBIR") ||
                        funcionalidad.equals("DASHBOARD") ||
                        funcionalidad.equals("HISTORICO") ||
                        funcionalidad.equals("CAMBIAR_CLAVE") ||
                        funcionalidad.equals("ACERCA_DE");

            case FARMACEUTA:
                return funcionalidad.equals("DESPACHO") ||
                        funcionalidad.equals("DASHBOARD") ||
                        funcionalidad.equals("HISTORICO") ||
                        funcionalidad.equals("CAMBIAR_CLAVE") ||
                        funcionalidad.equals("ACERCA_DE");

            default:
                return false;
        }
    }

    public boolean usuarioActualTienePermiso(String funcionalidad) {
        if (usuarioActual == null) {
            return false;
        }
        return tienePermiso(usuarioActual.getId(), funcionalidad);
    }

    public String getNombreTipoUsuario(String id) {
        TipoUsuario tipo = determinarTipoUsuario(id);
        switch (tipo) {
            case ADMINISTRADOR: return "Administrador";
            case MEDICO: return "Médico";
            case FARMACEUTA: return "Farmaceuta";
            default: return "Usuario Desconocido";
        }
    }

    public String getNombreTipoUsuarioActual() {
        if (usuarioActual == null) {
            return "Sin usuario";
        }
        return getNombreTipoUsuario(usuarioActual.getId());
    }

    public boolean validarFormatoId(String id) {
        if (id == null || id.length() < 2) {
            return false;
        }

        if (id.equalsIgnoreCase("admin")) return true;

        String prefijo = id.substring(0, 1).toUpperCase();
        switch (prefijo) {
            case "M":
                return id.matches("^[Mm]\\d+.*");
            case "F":
                return id.matches("^[Ff]\\d+.*");
            default:
                return false;
        }
    }

    public String getMensajeErrorFormatoId(String id) {
        if (id == null || id.isEmpty()) {
            return "El ID no puede estar vacío.";
        }
        if (id.equalsIgnoreCase("admin")) {
            return "ID válido: Administrador del sistema.";
        }
        if (id.length() < 2) {
            return "El ID debe tener al menos 2 caracteres.\n\n" +
                    "Formatos válidos:\n" +
                    "• Médicos: M001, M002, etc.\n" +
                    "• Farmaceutas: F001, F002, etc.\n";
        }

        String prefijo = id.substring(0, 1).toUpperCase();
        switch (prefijo) {
            case "M":
                if (!id.matches("^[Mm]\\d+.*")) {
                    return "Los IDs de médicos deben comenzar con 'M' seguido de números.\nEjemplo: M001";
                }
                break;
            case "F":
                if (!id.matches("^[Ff]\\d+.*")) {
                    return "Los IDs de farmaceutas deben comenzar con 'F' seguido de números.\nEjemplo: F001";
                }
                break;
            default:
                return "El ID debe comenzar con:\n" +
                        "• 'M' para médicos (ej: M001)\n" +
                        "• 'F' para farmaceutas (ej: F001)\n"+
                        "• 'admin' para el administrador del sistema";
        }

        return "Formato de ID correcto.";
    }
}
