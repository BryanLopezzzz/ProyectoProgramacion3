module hospital {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;

    opens hospital to javafx.fxml;
    exports hospital;
}