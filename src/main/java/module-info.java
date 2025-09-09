module ProyectoProgramacion3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires jakarta.xml.bind;

    opens hospital.view to javafx.fxml;
    opens hospital.controller to javafx.fxml;
    opens hospital.datos.conector to jakarta.xml.bind;
    opens hospital.datos.entidades to jakarta.xml.bind;

    opens icons to javafx.fxml, javafx.graphics;

    exports hospital;
    exports hospital.controller;
    exports hospital.view;
}
