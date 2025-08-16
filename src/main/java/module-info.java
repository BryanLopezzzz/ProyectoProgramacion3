module hospital {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;
    requires java.desktop;

    opens hospital to javafx.fxml;
    exports hospital;
}