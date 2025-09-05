package hospital.controller;

import hospital.model.Usuario;

public class LoginController {
    private final AuthService authService;

    public LoginController() {
        this.authService = new AuthService();
    }

    public Usuario login(String id, String clave) throws Exception {
        return authService.login(id, clave);
    }

    public void logout() {
        authService.cerrarSesion();
    }

    public Usuario getUsuarioActual() {
        return authService.getUsuarioActual();
    }

    public boolean isLoggedIn() {
        return authService.estaAutenticado();
    }
}
