module hospital {
    requires javafx.controls;
    requires javafx.fxml;

    opens hospital to javafx.fxml;
    exports hospital;
}
