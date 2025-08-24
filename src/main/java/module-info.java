module ProyectoProgramacion3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens hospital.view to javafx.fxml;
    opens hospital.controller to javafx.fxml;
    exports hospital;
    exports hospital.controller;
    exports hospital.view;
}
