module ProyectoProgramacion3 {
    requires javafx.controls;
    requires javafx.fxml;

    opens hospital.controller to javafx.fxml;
    exports hospital;
    exports hospital.controller;
    exports hospital.view;
}
