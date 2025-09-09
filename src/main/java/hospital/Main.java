package hospital;

import hospital.logica.AdministradorLogica;
import hospital.model.Administrador;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public void start(Stage primaryStage) throws Exception {
        // admin quemado por el momento
        // Usuario: admin Clave: admin
        AdministradorLogica adminLogica = new AdministradorLogica();
        if (adminLogica.buscarPorId("admin") == null) {
            Administrador superAdmin = new Administrador();
            superAdmin.setId("admin");
            superAdmin.setNombre("Super Usuario");
            adminLogica.agregar(superAdmin);
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/hospital/view/login.fxml"));
        Scene scene = new Scene(loader.load());
        primaryStage.setTitle("Login Hospital");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

