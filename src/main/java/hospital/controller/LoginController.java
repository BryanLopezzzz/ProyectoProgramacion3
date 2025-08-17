package hospital.controller;

import hospital.model.datos.UsuarioDAO;
import hospital.model.entidades.Usuario;

public class LoginController {
    private final UsuarioDAO usuarioDAO;

    public LoginController() {
        this.usuarioDAO = new UsuarioDAO();
    }

    // ========================
    // LOGIN
    // ========================
    public Usuario login(String id, String clave) throws Exception {
        Usuario u = usuarioDAO.login(id, clave);
        if (u == null) {
            throw new Exception("Usuario o clave incorrecta.");
        }
        return u;
    }

    // ========================
    // VALIDACIÓN USUARIO
    // ========================
    public boolean existeUsuario(String id) {
        return usuarioDAO.existeUsuario(id);
    }
}
