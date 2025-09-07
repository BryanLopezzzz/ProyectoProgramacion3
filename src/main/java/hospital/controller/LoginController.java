package hospital.controller;

import hospital.model.Usuario;
import hospital.logica.UsuarioManager;

public class LoginController {
    private final UsuarioManager usuarioManager;
    private Usuario usuarioActual; // almacena el usuario logueado en sesión

    public LoginController() {
        this.usuarioManager = new UsuarioManager();
    }

    // ==== LOGIN ====
    public Usuario login(String id, String clave) throws Exception {
        Usuario u = usuarioManager.login(id, clave);
        this.usuarioActual = u;
        return u;
    }

    // ==== LOGOUT ====
    public void logout() {
        this.usuarioActual = null;
    }

    // ==== CAMBIO DE CLAVE ====
    public void cambiarClave(String actual, String nueva) throws Exception {
        if (usuarioActual == null) {
            throw new Exception("No hay un usuario autenticado.");
        }
        usuarioManager.cambiarClave(usuarioActual.getId(), actual, nueva);
        // actualizar el objeto en memoria
        usuarioActual.setClave(nueva);
    }

    // ==== Estado de sesión ====
    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public boolean isLoggedIn() {
        return usuarioActual != null;
    }
}
