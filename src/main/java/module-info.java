module ProyectoProgramacion3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires jakarta.xml.bind;
    requires javafx.base;
    requires javafx.graphics;

    opens hospital.view to javafx.fxml;
    opens hospital.view.busqueda to javafx.fxml;
    opens hospital.controller to javafx.fxml;
    opens hospital.view.registro to javafx.fxml;

    opens hospital.datos.conector to jakarta.xml.bind;
    opens hospital.datos.entidades to jakarta.xml.bind, org.glassfish.jaxb.runtime;
    opens hospital.model to jakarta.xml.bind;
    opens hospital.datos to org.glassfish.jaxb.core, org.glassfish.jaxb.runtime;

    opens icons to javafx.fxml, javafx.graphics;

    exports hospital;
    exports hospital.controller;
    exports hospital.model;
    exports hospital.view;
    exports hospital.view.busqueda;
}
