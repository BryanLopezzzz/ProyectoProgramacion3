package hospital.view;

import hospital.controller.DashboardController;
import hospital.controller.LoginController;
import hospital.logica.Sesion;
import hospital.logica.UsuarioManager;
import hospital.model.Medico;
import hospital.model.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.Pane;
import java.io.IOException;
import java.time.YearMonth;

public class DashboardView {
    @FXML
    private Label lblUsuario;

    @FXML
    private Button btnMedicos;

    @FXML
    private Button btnFarmaceutas;

    @FXML
    private Button btnPacientes;

    @FXML
    private Button btnMedicamentos;

    @FXML
    private Button btnPrescribirReceta;

    @FXML
    private Button btnDespachoReceta;

    @FXML
    private Button btnHistoricoRecetas;

    @FXML
    private Button btnAcercaDe;

    @FXML
    private Button btnCambiarClave;

    @FXML
    private Button btnLogout;

    @FXML
    private Pane paneLineChart;

    @FXML
    private Pane panePieChart;

    private final DashboardController dashboardController = new DashboardController();
    private final UsuarioManager usuarioManager = new UsuarioManager();
    private LoginController loginController;

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }

    public LoginController getLoginController() {
        return loginController;
    }

    @FXML
    public void initialize() {
        Usuario usuario = Sesion.getUsuario();
        if (usuario != null && lblUsuario != null) {
            lblUsuario.setText(usuario.getNombre());
        }
        configurarPermisosPorRol(usuario);
        mostrarGraficoLineas(usuario);
        mostrarGraficoPastel(usuario);
    }
    private void configurarPermisosPorRol(Usuario usuario) {
        if (usuario == null || usuario.getId() == null) {
            ocultarTodosLosBotones();
            return;
        }

        UsuarioManager.TipoUsuario tipo = usuarioManager.determinarTipoUsuario(usuario.getId());

        switch (tipo) {
            case MEDICO:
                configurarPermisosMedico();
                break;
            case FARMACEUTA:
                configurarPermisosFarmaceuta();
                break;
            case ADMINISTRADOR:
                configurarPermisosAdministrador();
                break;
            default:
                ocultarTodosLosBotones();
                System.err.println("Tipo de usuario no reconocido: " + usuario.getId());
                break;
        }
    }

    private void configurarPermisosAdministrador() {
        btnMedicos.setVisible(true);
        btnFarmaceutas.setVisible(true);
        btnPacientes.setVisible(true);
        btnMedicamentos.setVisible(true);

        btnPrescribirReceta.setVisible(false);
        btnDespachoReceta.setVisible(false);
        btnHistoricoRecetas.setVisible(true);
        btnCambiarClave.setVisible(true);
        btnAcercaDe.setVisible(true);
        btnLogout.setVisible(true);
    }

    private void configurarPermisosMedico() {
        // Ocultar funciones no permitidas
        btnMedicos.setVisible(false);
        btnFarmaceutas.setVisible(false);
        btnPacientes.setVisible(false);
        btnMedicamentos.setVisible(false);
        btnDespachoReceta.setVisible(false);

        // Mostrar funciones permitidas
        btnPrescribirReceta.setVisible(true);
        btnHistoricoRecetas.setVisible(true);
        btnCambiarClave.setVisible(true);
        btnAcercaDe.setVisible(true);
        btnLogout.setVisible(true);
    }

    private void configurarPermisosFarmaceuta() {
        // Ocultar funciones no permitidas
        btnMedicos.setVisible(false);
        btnFarmaceutas.setVisible(false);
        btnPacientes.setVisible(false);
        btnMedicamentos.setVisible(false);
        btnPrescribirReceta.setVisible(false);

        // Mostrar funciones permitidas
        btnDespachoReceta.setVisible(true);
        btnHistoricoRecetas.setVisible(true);
        btnCambiarClave.setVisible(true);
        btnAcercaDe.setVisible(true);
        btnLogout.setVisible(true);
    }

    private void ocultarTodosLosBotones() {
        btnMedicos.setVisible(false);
        btnFarmaceutas.setVisible(false);
        btnPacientes.setVisible(false);
        btnMedicamentos.setVisible(false);
        btnPrescribirReceta.setVisible(false);
        btnDespachoReceta.setVisible(false);

        // Solo funciones básicas
        btnHistoricoRecetas.setVisible(true);
        btnCambiarClave.setVisible(true);
        btnAcercaDe.setVisible(true);
        btnLogout.setVisible(true);
    }

    private void mostrarGraficoLineas(Usuario usuario) {
        if (usuario == null) return;
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Medicamentos por mes");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Medicamentos");

        try {
            var datos = dashboardController.contarMedicamentosPorMes(
                    usuario,
                    YearMonth.now().minusMonths(5),
                    YearMonth.now()
            );
            datos.forEach((mes, cantidad) -> series.getData().add(new XYChart.Data<>(mes, cantidad)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        lineChart.getData().add(series);
        paneLineChart.getChildren().clear();
        paneLineChart.getChildren().add(lineChart);
    }

    private void mostrarGraficoPastel(Usuario usuario) {
        if (usuario == null) return;
        PieChart pieChart = new PieChart();
        pieChart.setTitle("Recetas por estado");

        try {
            var datos = dashboardController.contarRecetasPorEstado(usuario);
            datos.forEach((estado, cantidad) ->
                    pieChart.getData().add(new PieChart.Data(estado, cantidad))
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        panePieChart.getChildren().clear();
        panePieChart.getChildren().add(pieChart);
    }

    @FXML
    public void irAMedicos() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/hospital/view/medicosAdmin.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) btnMedicos.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Administración de Médicos");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alerta.error("Error", "Error al cargar la vista de médicos");
        }
    }

    @FXML
    public void irAFarmaceutas() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/hospital/view/farmaceutasAdmin.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) btnFarmaceutas.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Administración de Farmaceutas");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alerta.error("Error", "Error al cargar la vista de farmaceutas");
        }
    }

    @FXML
    public void irAPacientes() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("pacientesAdmin.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) btnPacientes.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Administración de Pacientes");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alerta.error("Error", "Error al cargar la vista de pacientes");
        }
    }

    @FXML
    public void irAMedicamentos() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("medicamentosAdmin.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) btnMedicamentos.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Administración de Medicamentos");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alerta.error("Error", "Error al cargar la vista de medicamentos");
        }
    }

    @FXML
    public void irAPreescribirReceta() {
        try {
            Usuario usuarioActual = Sesion.getUsuario();

            // Verificar que el usuario sea realmente un médico
            UsuarioManager.TipoUsuario tipo = usuarioManager.determinarTipoUsuario(usuarioActual.getId());
            if (tipo != UsuarioManager.TipoUsuario.MEDICO) {
                Alerta.error("Acceso denegado", "Solo los médicos pueden prescribir recetas.");
                return;
            }

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/hospital/view/prescribirReceta.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            // Obtener el controlador y configurar el médico
            PreescribirRecetaView controller = fxmlLoader.getController();

            // Crear objeto Medico con los datos del usuario autenticado
            Medico medico = new Medico();
            medico.setId(usuarioActual.getId());
            medico.setNombre(usuarioActual.getNombre());
            // Agregar otros campos necesarios según tu modelo Medico

            controller.setMedico(medico);

            Stage stage = (Stage) btnPrescribirReceta.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Prescribir Receta");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alerta.error("Error", "Error al cargar la vista de prescribir receta: " + e.getMessage());
        }
    }

    @FXML
    public void irADespacho() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/hospital/view/despachoFarmaceuta.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) btnDespachoReceta.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Despacho de recetas");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alerta.error("Error", "Error al cargar la vista de Despacho.");
        }
    }

    @FXML
    public void irAHistoricoRecetas() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/hospital/view/historicoRecetas.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) btnHistoricoRecetas.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Historial de Recetas");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alerta.error("Error", "Error al cargar la vista de historial de recetas.");
        }
    }

    @FXML
    public void irAAcercaDe() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/hospital/view/acercaDe1.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) btnAcercaDe.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Acerca de");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alerta.error("Error", "Error al cargar la vista de Acerca de.");
        }
    }

    @FXML
    public void irACambiarClave() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/hospital/view/cambiarContraseña.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            CambioClaveView cambioClaveView = fxmlLoader.getController();
            cambioClaveView.setUsuario(Sesion.getUsuario());
            cambioClaveView.setLoginController(loginController);

            Stage stage = (Stage) btnCambiarClave.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Cambiar Contraseña");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alerta.error("Error", "Error al cargar la vista de cambio de contraseña.");
        }
    }
    @FXML
    public void logout(){
        if (loginController != null) {
            loginController.logout();
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/hospital/view/login.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) btnLogout.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Login - Sistema Hospital");
            stage.show();
        } catch (IOException e) {
           Alerta.error("Error","Error al regresar al login");
        }
    }
}
