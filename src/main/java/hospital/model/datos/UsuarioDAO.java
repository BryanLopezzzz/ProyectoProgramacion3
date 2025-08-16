package hospital.model.datos;

import hospital.model.entidades.Usuario;
import hospital.model.entidades.Medico;
import hospital.model.entidades.Farmaceuta;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    private final MedicoDAO medicoDAO = new MedicoDAO();
    private final FarmaceutaDAO farmaceutaDAO = new FarmaceutaDAO();

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
        // 3. Si implementas Administrador
        // u = administradorDAO.buscarPorId(id);
        // if (u != null && u.getClave().equals(clave)) {
        //     return u;
        // }

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

        // 3. Administrador (si lo implementas)
        // Administrador a = administradorDAO.buscarPorId(id);
        // if (a != null && a.getClave().equals(claveActual)) {
        //     a.setClave(nuevaClave);
        //     administradorDAO.modificar(a);
        //     return true;
        // }

        return false;
    }

    public boolean existeUsuario(String id) {
        if (medicoDAO.buscarPorId(id) != null) return true;
        if (farmaceutaDAO.buscarPorId(id) != null) return true;
        // if (administradorDAO.buscarPorId(id) != null) return true;
        return false;
    }

    // ==== Listar todos los usuarios (opcional, para debugging o dashboard) ====
    public List<Usuario> listarTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        usuarios.addAll(medicoDAO.listar());
        usuarios.addAll(farmaceutaDAO.listar());
        // usuarios.addAll(administradorDAO.listar());
        return usuarios;
    }
}
