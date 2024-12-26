module org.example.todofe {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires com.fasterxml.jackson.databind;
    requires java.desktop;

    opens org.example.todofe to javafx.fxml;
    exports org.example.todofe;
}