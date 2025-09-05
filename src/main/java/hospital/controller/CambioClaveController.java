package hospital.controller;

public class CambioClaveController {
    private final AuthService authService;

    public CambioClaveController(AuthService authService) {
        this.authService = authService;
    }

    public void cambiarClave(String claveActual, String nuevaClave) throws Exception {
        authService.cambiarClave(claveActual, nuevaClave);
    }
}