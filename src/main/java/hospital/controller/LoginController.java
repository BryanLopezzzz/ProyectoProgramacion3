package hospital.controller;

import hospital.logica.Sesion;
import hospital.model.Usuario;
import hospital.logica.UsuarioManager;

public class LoginController {
    private final UsuarioManager usuarioManager;

    public LoginController() {
        this.usuarioManager = new UsuarioManager();
    }

    // ==== LOGIN ====
    public Usuario login(String id, String clave) throws Exception {
        Usuario u = usuarioManager.login(id, clave);
        Sesion.setUsuario(u);
        return u;
    }

    // ==== LOGOUT ====
    public void logout() {
        Sesion.setUsuario(null);
    }

    // ==== CAMBIO DE CLAVE ====
    public void cambiarClave(String actual, String nueva) throws Exception {
        Usuario usuarioActual = UsuarioManager.getUsuarioActual();
        if (usuarioActual == null) {
            throw new Exception("No hay un usuario autenticado.");
        }
        usuarioManager.cambiarClave(usuarioActual.getId(), actual, nueva);
        // actualizar el objeto en memoria
        usuarioActual.setClave(nueva);
    }

    // ==== Estado de sesión ====
    public Usuario getUsuarioActual() {
        return UsuarioManager.getUsuarioActual();
    }

    public boolean isLoggedIn() {
        return UsuarioManager.getUsuarioActual() != null;
    }
}
