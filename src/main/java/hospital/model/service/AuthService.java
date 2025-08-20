package hospital.model.service;

import hospital.model.datos.UsuarioDAO;
import hospital.model.entidades.Usuario;

public class AuthService {
    private final UsuarioDAO usuarioDAO;
    private Usuario usuarioActual; // el que está logueado

    public AuthService() {
        this.usuarioDAO = new UsuarioDAO();
    }

    public Usuario login(String id, String clave) throws Exception {
        Usuario u = usuarioDAO.login(id, clave);
        if (u == null) {
            throw new Exception("Usuario o clave incorrecta.");
        }
        this.usuarioActual = u;
        return u;
    }

    public void cerrarSesion() {
        this.usuarioActual = null;
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public boolean estaAutenticado() {
        return usuarioActual != null; //es como preguntarle al sistema “Ya hay alguien logueado?”.
    }
    // ========================
    // CAMBIO DE CLAVE
    // ========================
    public void cambiarClave(String claveActual, String nuevaClave) throws Exception {
        if (usuarioActual == null) {
            throw new Exception("No hay un usuario autenticado.");
        }

        boolean actualizado = usuarioDAO.cambiarClave(usuarioActual.getId(), claveActual, nuevaClave);
        if (!actualizado) {
            throw new Exception("No se pudo cambiar la clave. Verifique la clave actual.");
        }

        // si se actualizó, reflejamos en el objeto en memoria
        usuarioActual.setClave(nuevaClave);
    }
}
