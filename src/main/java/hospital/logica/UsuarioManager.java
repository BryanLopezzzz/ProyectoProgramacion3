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

        Usuario u = medicoLogica.buscarPorId(id);
        if (u != null && u.getClave().equals(clave)) return u;

        u = farmaceutaLogica.buscarPorId(id);
        if (u != null && u.getClave().equals(clave)) return u;

        u = administradorLogica.buscarPorId(id);
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

        Administrador a = administradorLogica.buscarPorId(id);
        if (a != null) {
            if (!a.getClave().equals(actual))
                throw new Exception("Clave actual incorrecta.");
            a.setClave(nueva);
            administradorLogica.modificar(a);
            return;
        }

        throw new Exception("Usuario no encontrado.");
    }

    public boolean existeUsuario(String id) {
        if (medicoLogica.buscarPorId(id) != null) return true;
        if (farmaceutaLogica.buscarPorId(id) != null) return true;
        if (administradorLogica.buscarPorId(id) != null) return true;
        return false;
    }

    // ==== Listar todos los usuarios (opcional, para debugging o dashboard) ====
    public List<Usuario> listarTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        usuarios.addAll(medicoLogica.listar());
        usuarios.addAll(farmaceutaLogica.listar());
        usuarios.addAll(administradorLogica.listar());
        return usuarios;
    }
}
