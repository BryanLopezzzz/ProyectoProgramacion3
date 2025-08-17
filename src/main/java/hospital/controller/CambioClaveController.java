package hospital.controller;

import hospital.model.datos.UsuarioDAO;

public class CambioClaveController {
    private final UsuarioDAO usuarioDAO;

    public CambioClaveController() {
        this.usuarioDAO = new UsuarioDAO();
    }

    // ========================
    // CAMBIO DE CLAVE
    // ========================

    public void cambiarClave(String id, String claveActual, String nuevaClave) throws Exception {
        boolean actualizado = usuarioDAO.cambiarClave(id, claveActual, nuevaClave);
        if (!actualizado) {
            throw new Exception("No se pudo cambiar la clave. Credenciales incorrectas.");
        }
    }
}
