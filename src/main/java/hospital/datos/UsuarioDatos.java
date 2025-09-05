package hospital.datos;

import hospital.model.Usuario;
import hospital.model.Medico;
import hospital.model.Farmaceuta;
import hospital.model.Administrador;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDatos {
    private final MedicoDatos medicoDAO = new MedicoDatos();
    private final FarmaceutaDatos farmaceutaDAO = new FarmaceutaDatos();
    private final AdministradorDatos administradorDAO = new AdministradorDatos();

    // ==== LOGIN ====
    public Usuario login(String id, String clave) {
        Usuario u = medicoDAO.buscarPorId(id);
        if (u != null && u.getClave().equals(clave)) {
            return u;
        }
        u = farmaceutaDAO.buscarPorId(id);
        if (u != null && u.getClave().equals(clave)) {
            return u;
        }

        u = administradorDAO.buscarPorId(id);
        if (u != null && u.getClave().equals(clave)) {
            return u;
        }

        return null;
    }

    // ==== Cambio de clave ====
    public boolean cambiarClave(String id, String claveActual, String nuevaClave) {
        Medico m = medicoDAO.buscarPorId(id);
        if (m != null && m.getClave().equals(claveActual)) {
            m.setClave(nuevaClave);
            medicoDAO.modificar(m);
            return true;
        }
        Farmaceuta f = farmaceutaDAO.buscarPorId(id);
        if (f != null && f.getClave().equals(claveActual)) {
            f.setClave(nuevaClave);
            farmaceutaDAO.modificar(f);
            return true;
        }


        Administrador a = administradorDAO.buscarPorId(id);
        if (a != null && a.getClave().equals(claveActual)) {
            a.setClave(nuevaClave);
            administradorDAO.modificar(a);
            return true;
        }

        return false;
    }

    public boolean existeUsuario(String id) {
        if (medicoDAO.buscarPorId(id) != null) return true;
        if (farmaceutaDAO.buscarPorId(id) != null) return true;
         if (administradorDAO.buscarPorId(id) != null) return true;
        return false;
    }

    // ==== Listar todos los usuarios (opcional, para debugging o dashboard) ====
    public List<Usuario> listarTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        usuarios.addAll(medicoDAO.listar());
        usuarios.addAll(farmaceutaDAO.listar());
        usuarios.addAll(administradorDAO.listar());
        return usuarios;
    }
}
